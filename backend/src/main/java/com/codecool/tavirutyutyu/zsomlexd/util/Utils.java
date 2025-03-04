package com.codecool.tavirutyutyu.zsomlexd.util;

import org.springframework.web.multipart.MultipartFile;

public class Utils {

    public static boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }

    public static boolean isAudioFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && (contentType.equals("audio/mpeg") || contentType.equals("audio/mp3"));
    }
}
