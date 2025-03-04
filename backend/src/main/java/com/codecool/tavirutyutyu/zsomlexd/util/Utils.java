package com.codecool.tavirutyutyu.zsomlexd.util;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

    public static UserDetails getCurrentUsername(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof UserDetails){
            return (UserDetails) principal;
        }else{
            throw new EntityNotFoundException("User not found");
        }
    }

}
