package com.zmbdp.alibaba;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class SpringAlibabaApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringAlibabaApplication.class, args);
        log.info("SpringAlibabaApplication 启动成功.....");
    }
}
