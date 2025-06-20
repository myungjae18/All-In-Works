package com.example.allinworks.module.board.mapper;

import com.example.allinworks.module.board.domain.Post;
import com.example.allinworks.module.board.dto.PostDTO;
import com.example.allinworks.module.user.domain.User;

public class PostMapper {

    public static Post dtoToPost(PostDTO dto) {
        return Post.builder()
                .postNo(dto.getPostNo())
                .boardNo(dto.getBoardNo())
//                .userNo(dto.getUserNo())
                .user(
                        User.builder()
                                .userNo(dto.getUserNo())
                                .build()
                )
                .fileNo(dto.getFileNo())
                .title(dto.getTitle())
                .content(dto.getContent())
                .views(dto.getViews())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .notice(dto.isNotice())
                .build();
    }

    public static PostDTO postToDto(Post post, String userName) {
        return new PostDTO(
                post.getPostNo(),
                post.getBoardNo(),
                post.getUser().getUserNo(),
                post.getFileNo(),
                post.getTitle(),
                post.getContent(),
                post.getViews(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                post.isNotice(),
                null,
                userName // userName은 나중에 설정할 수 있도록 null로 초기화
        );
    }


}
