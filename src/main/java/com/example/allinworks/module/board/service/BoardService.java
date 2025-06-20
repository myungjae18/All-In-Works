package com.example.allinworks.module.board.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.example.allinworks.module.board.dto.CommentDTO;
import com.example.allinworks.module.user.domain.User;
import lombok.extern.log4j.Log4j2;

import org.springframework.stereotype.Service;

import com.example.allinworks.module.board.domain.Board;
import com.example.allinworks.module.board.domain.Comment;
import com.example.allinworks.module.board.domain.Post;
import com.example.allinworks.module.board.dto.PostDTO;
import com.example.allinworks.module.board.mapper.PostMapper;
import com.example.allinworks.module.board.repository.BoardRepository;
import com.example.allinworks.module.board.repository.CommentRepository;
import com.example.allinworks.module.board.repository.PostRepository;
import com.example.allinworks.module.user.repository.UserRepository;


@Service
@Log4j2
public class BoardService {

    private final BoardRepository boardRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    //private static final Logger log = LoggerFactory.getLogger(getClass());
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    public BoardService(BoardRepository boardRepository, PostRepository postRepository, UserRepository userRepository,
                        CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.boardRepository = boardRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;

    }

    public List<Board> getAllBoards() {
        return boardRepository.findAll();
    }

    public Post savePost(PostDTO post) {
        Post postEntity = PostMapper.dtoToPost(post);
        return postRepository.save(postEntity);
    }

    public Board getBoardById(Integer id) {
        return boardRepository.findById(id).orElse(null);
    }

    public List<PostDTO> selectAllByBoardNo(int boardNo) {
        String userName = null;
        List<Post> posts = postRepository.findAllByBoardNo(boardNo);
        List<PostDTO> postDTOs = new ArrayList<>();
        for (Post post : posts) {
//            userName = userRepository.findById(post.getUserNo()).get().getUserName();
            userName = post.getUser().getUserName();
            PostDTO postDTO = PostMapper.postToDto(post, userName);
            postDTOs.add(postDTO);
        }

        return postDTOs;
    }

    public Board getBoardByDeptNo(String deptNo) {
        return boardRepository.findByDeptNo(deptNo);
    }

    public PostDTO getPostByPostNo(int postNo) {
        log.info("1 : {}", LocalDateTime.now().format(FORMATTER));
        Post post = postRepository.findById(postNo).orElse(null);
        log.info("2 : {}", LocalDateTime.now().format(FORMATTER));
        if (post != null) {
//            String userName = userRepository.findById(post.getUserNo()).get().getUserName();
            String userName = post.getUser().getUserName();
            log.info("3 : {}", LocalDateTime.now().format(FORMATTER));
            PostDTO postDTO = PostMapper.postToDto(post, userName);
            postDTO.setComments(getComments(postNo));
            return postDTO;
        }
        return null;
    }

    public void deletePost(int postNo) {
        postRepository.deleteById(postNo);
    }

    public void updatePost(PostDTO postDTO) {
        Post post = PostMapper.dtoToPost(postDTO);
        // 기존 댓글 유지
        Post origin = postRepository.findById(post.getPostNo()).orElse(null);
        if (origin != null) {
            post.setComments(origin.getComments());
        }
        postRepository.save(post);
    }

    public void addComment(Integer postNo, User user, String content) {
        Post post = postRepository.findById(postNo).orElseThrow();
        Comment comment = Comment.builder()
                .post(post)
                .user(user)
                .content(content)
                .createdAt(LocalDateTime.now())
                .build();
        commentRepository.save(comment);
    }

    public List<PostDTO> getAnonymousPosts() {
        List<Post> posts = postRepository.findAllByBoardNo(0);
        List<PostDTO> postDTOs = new ArrayList<>();
        for (Post post : posts) {
            PostDTO postDTO = PostMapper.postToDto(post, "익명");
            postDTOs.add(postDTO);
        }
        return postDTOs;
    }


    public void increaseViewCount(Integer postNo) {
        // 조회수 증가
        Post postEntity = postRepository.findById(postNo).orElse(null);
        if (postEntity != null) {
            postEntity.setViews(postEntity.getViews() + 1);
            postRepository.save(postEntity);
        }
    }

    public List<CommentDTO> getComments(Integer postNo) {
        List<Comment> comments = commentRepository.findAllByPost(postRepository.findById(postNo).orElseThrow());
        List<CommentDTO> commentList = comments.stream()
                .map(comment -> {
                    CommentDTO dto = new CommentDTO();
                    dto.setCommentNo(comment.getCommentNo());
                    dto.setPostNo(comment.getPost().getPostNo().toString());
                    dto.setUserNo(comment.getUser().getUserNo());
                    dto.setContent(comment.getContent());
                    dto.setCreatedAt(comment.getCreatedAt() != null ? comment.getCreatedAt().toLocalDate() : null);
                    dto.setUpdatedAt(comment.getUpdatedAt() != null ? comment.getUpdatedAt().toLocalDate() : null);
                    dto.setUserName(comment.getPost().getBoardNo() == 0 ? "익명" : comment.getUser().getUserName());


                    return dto;
                })
                .collect(Collectors.toList());
        return commentList;
    }

}