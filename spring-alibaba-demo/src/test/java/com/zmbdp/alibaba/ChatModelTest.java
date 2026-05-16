package com.zmbdp.alibaba;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * 聊天模型测试
 *
 * @author 稚名不带撇
 */
@SpringBootTest
public class ChatModelTest {

    @Autowired
    private DashScopeChatModel chatModel;

    @Test
    public void chat() {
        // 使用默认的 qwen 模型
        String message = chatModel
                .call(new Prompt(
                        new UserMessage("你是谁？模型叫什么名字？是叫做qwen-plus么？？？"))
                ).getResult().getOutput().getText();
        System.out.println(message);
    }

    @Test
    public void chat2() {
        // 使用指定的 qwen-plus 模型
        DashScopeChatOptions options = DashScopeChatOptions.builder()
                .model("qwen-plus")
                .build();
        String message = chatModel.call(new Prompt(List.of(
                        new SystemMessage("你是小明，是稚名不带撇开发的ai平台"),
                        new UserMessage("你是谁？模型叫什么名字？是叫做qwen-plus么？？？")
                ), options)
        ).getResult().getOutput().getText();
        System.out.println(message);
    }
}
