package com.zmbdp.mcp.controller;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

//@RestController
//@RequestMapping("/chat")
//public class ChatController {
//
//    private final ChatClient chatClient;
//
//    public ChatController(DashScopeChatModel chatModel, ToolCallbackProvider toolCallbackProvider) {
//        this.chatClient = ChatClient.builder(chatModel)
////                .defaultToolCallbacks(toolCallbackProvider)
//                .build();
//    }
//
//    @RequestMapping(value = "/generate", produces = "text/html;charset=utf-8")
//    public Flux<String> generate(String message) {
//        SystemMessage systemMessage = new SystemMessage("你是小稚，稚名不带撇开发的一个多功能ai平台");
//        UserMessage userMessage = new UserMessage(message);
//        Prompt prompt = new Prompt(systemMessage, userMessage);
//        return chatClient.prompt(prompt)
//                .stream()
//                .content();
//    }
//}

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatClient chatClient;

    public ChatController(DashScopeChatModel chatModel, ToolCallbackProvider toolCallbackProvider) {
        this.chatClient = ChatClient.builder(chatModel)
                .defaultToolCallbacks(toolCallbackProvider)
                .defaultOptions(
                        DashScopeChatOptions.builder()
                                .model("qwen-max-latest")
                                .build()
                )
                .build();
    }

    @RequestMapping(value = "/generate", produces = "text/html;charset=utf-8")
    public Flux<String> generate(String message) {
        return chatClient.prompt()
                .system("你是小稚，稚名不带撇开发的一个多功能ai平台")
                .user(message)
                .stream()
                .content();
    }
}
