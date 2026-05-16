package com.zmbdp.mcp.config;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAiConfig {

    @Bean
    DashScopeChatOptions getChatOptions() {
        return DashScopeChatOptions.builder()
                .model("qwen-max-latest")
                .build();
    }
}
