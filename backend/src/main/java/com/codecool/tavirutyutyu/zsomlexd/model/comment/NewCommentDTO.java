package com.codecool.tavirutyutyu.zsomlexd.model.comment;

public record NewCommentDTO(
        Long id,
        Long userId,
        Long songId,
        String text
) {
}
