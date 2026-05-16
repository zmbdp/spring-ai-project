package com.zmbdp.rag;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class SpringRagApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringRagApplication.class, args);
        log.info("SpringRagApplication 启动成功.....");
    }
}