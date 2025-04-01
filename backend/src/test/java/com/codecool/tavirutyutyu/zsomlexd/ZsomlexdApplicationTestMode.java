package com.codecool.tavirutyutyu.zsomlexd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
class ZsomlexdApplicationTestMode {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ZsomlexdApplicationTestMode.class);
        app.setAdditionalProfiles("test");
        app.run(args);
    }
}