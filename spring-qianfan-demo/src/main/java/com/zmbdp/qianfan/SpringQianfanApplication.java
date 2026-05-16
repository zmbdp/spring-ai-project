package com.zmbdp.qianfan;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class SpringQianfanApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringQianfanApplication.class, args);
        log.info("SpringQianfanApplication 启动成功.....");
    }
}
