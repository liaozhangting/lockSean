package com.lssm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@MapperScan("com.lssm.mapper")
@SpringBootApplication
@EnableScheduling
public class LockSeanSocialMediaApplication {

    public static void main(String[] args) {
        SpringApplication.run(LockSeanSocialMediaApplication.class, args);
    }

}
