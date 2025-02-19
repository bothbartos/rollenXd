package com.codecool.tavirutyutyu.zsomlexd.model.comment;

public record CommentDto(
        Long id,
        Long songId,
        Long userId,
        String username,
        String profilePicture,
        String text
) {
}
