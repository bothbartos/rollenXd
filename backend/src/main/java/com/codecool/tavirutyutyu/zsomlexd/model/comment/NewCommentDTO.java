package com.codecool.tavirutyutyu.zsomlexd.model.comment;

public record NewCommentDTO(
        String user,
        Long songId,
        String text
) {
}
