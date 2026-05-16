package com.zmbdp.alibaba.controller;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.zmbdp.alibaba.tool.DateTimeTools;
import com.zmbdp.alibaba.tool.WeatherTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.tool.ToolCallingChatOptions;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.method.MethodToolCallback;
import org.springframework.ai.tool.support.ToolDefinitions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/chat")
public class ChatController {

    // ----------------------------------- 通过 ChatClient 添加工具 -----------------------------------
    private final ChatClient chatClient;

    public ChatController(DashScopeChatModel chatModel) {
        this.chatClient = ChatClient
                .builder(chatModel)
//                .defaultTools(new DateTimeTools(), new WeatherTools()) // 设置默认工具
                .defaultOptions(
                        DashScopeChatOptions.builder()
                                .model("qwen-max-latest")
                                .build()
                )
                .build();
    }

    @GetMapping(value = "/callByChatClient", produces = "text/html;charset=utf-8")
    public Flux<String> callByChatClient(@RequestParam("message") String message) {
        return chatClient.prompt()
                .user(message)
                .tools(new DateTimeTools(), new WeatherTools())
                .toolContext(Map.of("time", "chat-call111"))
                .stream()
                .content();
    }
    // ----------------------------------- 通过 ChatClient 添加工具 -----------------------------------

    // ----------------------------------- 通过 ChatModel 添加工具 -----------------------------------
    @Autowired
    private DashScopeChatModel chatModel;

    @GetMapping(value = "/callByChatModel", produces = "text/html;charset=utf-8")
    public Flux<String> callByChatModel(@RequestParam("message") String message) {
        // 创建一个工具列表
//        List<ToolCallback> toolCallbacks = Arrays.asList(MethodToolCallbackProvider.builder()
//                .toolObjects(new DateTimeTools(), new WeatherTools())
//                .build()
//                .getToolCallbacks());

        ToolCallback[] toolCallbacks = ToolCallbacks.from(new DateTimeTools(), new WeatherTools());

        // 设置模型的各种参数
        DashScopeChatOptions options = DashScopeChatOptions.builder()
                .model("qwen-max-latest") // 设置模型名称
                .toolCallbacks(Arrays.stream(toolCallbacks).toList()) // 设置工具列表
                .internalToolExecutionEnabled(true) // 允许模型内部执行工具
                .toolContext(Map.of("chatId", "chat-call222")) // 设置工具需要的参数
                .build();
        // 进行模型调用
        return chatModel.stream(
                new Prompt(
                        List.of(new UserMessage(message)),
                        options
                )
        ).mapNotNull(
                response ->
                        response.getResult().getOutput().getText()
        );
    }
    // ----------------------------------- 通过 ChatModel 添加工具 -----------------------------------

    // ----------------------------------- 通过 编程式 调用工具 -----------------------------------

    @RequestMapping("/call2")
    public String call2(String message){
        Method method = ReflectionUtils.findMethod(WeatherTools.class,
                "getCurrentWeatherByCityName", String.class);
        ToolCallback toolCallback = MethodToolCallback.builder()
                .toolDefinition(ToolDefinitions
                        .builder(method)
                        .description("根据给定的城市名称, 获取城市当前的天气")
                        .build())
                .toolMethod(method)
                .toolObject(new WeatherTools())
                .build();

        return chatClient.prompt()
                .user(message)
                .toolCallbacks(toolCallback)  //编程式
                .call()
                .content();
    }

    @RequestMapping("/callByTool2")
    public String callByTool2(String message){
        Method method = ReflectionUtils.findMethod(WeatherTools.class,
                "getCurrentWeatherByCityName", String.class);
        ToolCallback toolCallback = MethodToolCallback.builder()
                .toolDefinition(ToolDefinitions
                        .builder(method)
                        .description("根据给定的城市名称, 获取城市当前的天气")
                        .build())
                .toolMethod(method)
                .toolObject(new WeatherTools())
                .build();

        ChatOptions chatOptions = ToolCallingChatOptions.builder()
                .toolCallbacks(toolCallback)
                .build();
        Prompt prompt = new Prompt(message, chatOptions);

        return chatModel.call(prompt).getResult().getOutput().getText();
    }
}