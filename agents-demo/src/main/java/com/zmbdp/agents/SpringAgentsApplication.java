package com.zmbdp.agents;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class SpringAgentsApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringAgentsApplication.class, args);
        log.info("SpringAgentsApplication 启动成功.....");
    }
}