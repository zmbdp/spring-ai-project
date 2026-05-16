package com.zmbdp.mcp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class SpringMCPApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringMCPApplication.class, args);
        log.info("SpringMCPApplication 启动成功.....");
    }
}