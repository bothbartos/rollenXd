package com.codecool.tavirutyutyu.zsomlexd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.AbstractEnvironment;

@SpringBootApplication
public class ZsomlexdApplication {
    public static void main(String[] args) {
        if (System.getProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME) == null) {
            System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, "dev");
        }
        SpringApplication.run(ZsomlexdApplication.class, args);
    }
}
