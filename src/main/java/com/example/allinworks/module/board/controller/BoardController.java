package com.example.allinworks.module.board.controller;

import com.example.allinworks.module.board.domain.Board;
import com.example.allinworks.module.board.domain.Comment;
import com.example.allinworks.module.board.domain.Post;
import com.example.allinworks.module.board.dto.CommentDTO;
import com.example.allinworks.module.board.dto.PostDTO;
import com.example.allinworks.module.board.service.BoardService;
import com.example.allinworks.module.user.domain.User;
import com.example.allinworks.module.user.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;
    private final com.example.allinworks.module.board.repository.CommentRepository commentRepository;
    private final com.example.allinworks.module.board.repository.PostRepository postRepository;
    private final UserRepository userRepository;

    public BoardController(BoardService boardService,
                          com.example.allinworks.module.board.repository.CommentRepository commentRepository,
                          com.example.allinworks.module.board.repository.PostRepository postRepository,
                          UserRepository userRepository) {
        this.boardService = boardService;
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/department-board")
    public String showDepartmentBoardPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        String deptNo = user.getDepartment().getDeptNo();
        System.out.println("deptNo = " + deptNo);
        Board board = boardService.getBoardByDeptNo(deptNo);
        int boardNo = board.getBoardNo();
        List<PostDTO> postList = boardService.selectAllByBoardNo(boardNo);

        // 공지사항, 일반 게시글 분리
        List<PostDTO> noticeList = postList.stream()
            .filter(PostDTO::isNotice)
            .toList();
        List<PostDTO> normalList = postList.stream()
            .filter(post -> !post.isNotice())
            .toList();

        model.addAttribute("noticeList", noticeList);
        model.addAttribute("normalList", normalList);
        return "board/department-board";
    }

    @GetMapping("/department-board/createBoard")
    public String showCreateBoardPage() {
        return "board/create-board";
    }

    @PostMapping("/department-board/createBoard/insert")
    public String createDepartmentBoard(@ModelAttribute PostDTO postDTO, HttpSession session) {
        User user = (User) session.getAttribute("user");
        String deptNo = user.getDepartment().getDeptNo();
        Board board = boardService.getBoardByDeptNo(deptNo);
        int boardNo = board.getBoardNo();
        if (user != null) {
            postDTO.setUserNo(user.getUserNo());
        }
        postDTO.setPostNo(null);
        postDTO.setBoardNo(boardNo);
        postDTO.setCreatedAt(LocalDateTime.now());
        postDTO.setUpdatedAt(LocalDateTime.now());
        System.out.println("isNotice = " + postDTO.isNotice());
        postDTO.setNotice(postDTO.isNotice());
        boardService.savePost(postDTO);
        return "redirect:/boards/department-board";
    }

    @GetMapping("/department-board/viewPost")
    public String viewDepartmentPost(@RequestParam("postNo") Integer postNo, Model model) {
        boardService.increaseViewCount(postNo);
        PostDTO post = boardService.getPostByPostNo(postNo);

        model.addAttribute("post", post);
        model.addAttribute("commentList", post.getComments());

        return "board/viewPost";
    }

    @PostMapping("/department-board/deletePost")
    public String deleteDepartmentPost(@RequestParam("postNo") String postNo) {
        boardService.deletePost(Integer.parseInt(postNo));
        return "redirect:/boards/department-board";
    }

    @PostMapping("/department-board/updatePost")
    public String updateDepartmentPost(@ModelAttribute PostDTO postDTO) {
        // 기존 게시글 조회
        PostDTO origin = boardService.getPostByPostNo(postDTO.getPostNo());

        origin.setTitle(postDTO.getTitle());
        origin.setContent(postDTO.getContent());
        origin.setUpdatedAt(LocalDateTime.now());
        origin.setNotice(postDTO.isNotice());

        boardService.updatePost(origin);
        return "redirect:/boards/department-board";
    }

    @PostMapping("/department-board/addComment")
    public String addComment(@RequestParam("postNo") Integer postNo,
                         @RequestParam("comment") String content,
                         HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            // 로그인 안 했을 때 처리
            return "redirect:/login";
        }
        boardService.addComment(postNo, user, content);
        return "redirect:/boards/department-board/viewPost?postNo=" + postNo;
    }

    @PostMapping("/department-board/updateComment")
    public String updateComment(@RequestParam("commentNo") Integer commentNo,
                                @RequestParam("content") String content,
                                HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        Comment comment = commentRepository.findById(commentNo).orElse(null);
        if (comment != null && comment.getUser().getUserNo().equals(user.getUserNo())) {
            comment.setContent(content);
            comment.setUpdatedAt(java.time.LocalDateTime.now());
            commentRepository.save(comment);
        }
        // 댓글이 속한 게시글 번호로 리다이렉트
        Integer postNo = comment != null ? comment.getPost().getPostNo() : null;
        return "redirect:/boards/department-board/viewPost?postNo=" + (postNo != null ? postNo : "");
    }

    @PostMapping("/department-board/deleteComment")
    public String deleteComment(@RequestParam("commentNo") Integer commentNo,
                                HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        Comment comment = commentRepository.findById(commentNo).orElse(null);
        Integer postNo = null;
        if (comment != null && comment.getUser().getUserNo().equals(user.getUserNo())) {
            postNo = comment.getPost().getPostNo();
            commentRepository.deleteById(commentNo);
        }
        return "redirect:/boards/department-board/viewPost?postNo=" + (postNo != null ? postNo : "");
    }



    // ===================== 익명 게시판 =====================
    @GetMapping("/anonymous-board")
    public String showAnonymousBoardPage(Model model) {
        // 익명게시판(boardNo=0) 게시글만 조회
        List<PostDTO> postList = boardService.getAnonymousPosts();
        postList.forEach(post -> post.setUserName("익명"));
        model.addAttribute("postList", postList);
        return "board/anonymous-board";
    }

    @PostMapping("/anonymous-board/addComment")
    public String addAnonymousComment(@RequestParam("postNo") Integer postNo,
                                  @RequestParam("comment") String content,
                                  HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        boardService.addComment(postNo, user, content);
        // 항상 anonymous=true 파라미터를 붙여서 리다이렉트
        return "redirect:/boards/anonymous-board/viewPost?postNo=" + postNo + "&anonymous=true";
    }

    @PostMapping("/anonymous-board/updateComment")
    public String updateAnonymousComment(@RequestParam("commentNo") Integer commentNo,
                                @RequestParam("content") String content,
                                HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        Comment comment = commentRepository.findById(commentNo).orElse(null);
        if (comment != null && comment.getUser().getUserNo().equals(user.getUserNo())) {
            comment.setContent(content);
            comment.setUpdatedAt(java.time.LocalDateTime.now());
            commentRepository.save(comment);
        }
        Integer postNo = comment != null ? comment.getPost().getPostNo() : null;
        return "redirect:/boards/anonymous-board/viewPost?postNo=" + (postNo != null ? postNo : "") + "&anonymous=true";
    }

    @PostMapping("/anonymous-board/deleteComment")
    public String deleteAnonymousComment(@RequestParam("commentNo") Integer commentNo,
                                HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        Comment comment = commentRepository.findById(commentNo).orElse(null);
        Integer postNo = null;
        if (comment != null && comment.getUser().getUserNo().equals(user.getUserNo())) {
            postNo = comment.getPost().getPostNo();
            commentRepository.deleteById(commentNo);
        }
        return "redirect:/boards/anonymous-board/viewPost?postNo=" + (postNo != null ? postNo : "") + "&anonymous=true";
    }

    @GetMapping("/anonymous-board/viewPost")
    public String viewAnonymousPost(@RequestParam("postNo") Integer postNo, Model model, @RequestParam(value = "anonymous", required = false) Boolean anonymous) {
        boardService.increaseViewCount(postNo);
        PostDTO post = boardService.getPostByPostNo(postNo);

        post.setUserName("익명");
        model.addAttribute("post", post);
        model.addAttribute("commentList", post.getComments());
        model.addAttribute("anonymous", true);
        return "board/viewPost";
    }

    @GetMapping("/list")
    @ResponseBody
    public List<Board> getAllBoards() {
        return boardService.getAllBoards();
    }

    // @PostMapping
    // @ResponseBody
    // public Board createBoard(@RequestBody Board board) {
    // return boardService.saveBoard(board);
    // }

    @GetMapping("/{id}")
    @ResponseBody
    public Board getBoardById(@PathVariable Integer id) {
        return boardService.getBoardById(id);
    }

    @PostMapping("/anonymous-board/deletePost")
    public String deleteAnonymousPost(@RequestParam("postNo") String postNo) {
        boardService.deletePost(Integer.parseInt(postNo));
        return "redirect:/boards/anonymous-board";
    }

    @PostMapping("/anonymous-board/updatePost")
    public String updateAnonymousPost(@ModelAttribute PostDTO postDTO) {
        PostDTO origin = boardService.getPostByPostNo(postDTO.getPostNo());
        origin.setTitle(postDTO.getTitle());
        origin.setContent(postDTO.getContent());
        origin.setUpdatedAt(LocalDateTime.now());
        origin.setNotice(false); // 익명게시판은 공지 없음
        boardService.updatePost(origin);
        return "redirect:/boards/anonymous-board";
    }

    @GetMapping("/anonymous-board/create")
    public String showAnonymousCreatePage(Model model) {
        model.addAttribute("anonymousWrite", true);
        return "board/create-board";
    }

    @PostMapping("/anonymous-board/create")
    public String createAnonymousPost(@ModelAttribute PostDTO postDTO, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            postDTO.setUserNo(user.getUserNo());
        }
        postDTO.setPostNo(null);
        postDTO.setBoardNo(0); // 익명게시판 boardNo=0 지정
        postDTO.setCreatedAt(LocalDateTime.now());
        postDTO.setUpdatedAt(LocalDateTime.now());
        postDTO.setNotice(false); // 익명게시판은 공지 없음
        boardService.savePost(postDTO);
        return "redirect:/boards/anonymous-board";
    }
}