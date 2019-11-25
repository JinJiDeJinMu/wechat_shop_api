package com.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ImportResource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.ControllerAdvice;

@EnableTransactionManagement
@ControllerAdvice
@EnableCaching
@SpringBootApplication
@ImportResource(value = "kapa.xml")
public class CdtWxServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CdtWxServerApplication.class, args);
    }
}
