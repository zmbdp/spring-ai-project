package com.zmbdp.qianfan.controller;

import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiImageModel;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/qianfan")
public class QianfanController {
    @Autowired
    private OpenAiChatModel openAiChatModel;

    @Autowired
    private OpenAiImageModel openAiImageModel;

    @RequestMapping(value = "/chat", produces = "text/html;charset=utf-8")
    public Flux<String> chat(String message) {
//        return openAiChatModel.stream(message);
        Flux<ChatResponse> stream = openAiChatModel.stream(new Prompt(new SystemMessage(""), new UserMessage(message)));
        return stream.mapNotNull(result -> result.getResult().getOutput().getText());
    }

    @RequestMapping("/image")
    public void image() {
        ImageResponse response = openAiImageModel.call(
                new ImagePrompt("Generate a picture of an owl playing guitar. The owl should wear sunglasses, and the background should be cool and scientific",
                        OpenAiImageOptions.builder()
                                .quality("hd")
                                .N(1)
                                .height(1024)
                                .width(1024).build())

        );
        String imageUrl = response.getResult().getOutput().getUrl();
        System.out.println(imageUrl);
    }
}
