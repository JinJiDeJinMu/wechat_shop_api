package com.chundengtai.base.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication(scanBasePackages = {"com.chundengtai.base"})
public class CdtWxServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CdtWxServerApplication.class, args);
    }
}
