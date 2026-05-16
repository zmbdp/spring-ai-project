package com.zmbdp.alibaba;

import com.alibaba.cloud.ai.dashscope.image.DashScopeImageModel;
import com.alibaba.cloud.ai.dashscope.image.DashScopeImageOptions;
import org.junit.jupiter.api.Test;
import org.springframework.ai.image.ImageGeneration;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

/**
 * 文生图模型测试
 *
 * @author 稚名不带撇
 */
@SpringBootTest
public class ImageModelTest {

    @Autowired
    private DashScopeImageModel imageModel;

    @Test
    void text2Img() {
        //默认模型为万相v1 wanx-v1
        DashScopeImageOptions options = DashScopeImageOptions.builder()
                .style("<watercolor>") //
                .build();
        ImageResponse imageResponse = imageModel.call(new ImagePrompt("一只猫", options));
        String imgUrl = imageResponse.getResult().getOutput().getUrl();
        System.out.println(imgUrl);
    }

    @Test
    void text2Img2() {
        //模型为通义千问
        String prompt = "中国女孩, 圆脸, 看着镜头, 优雅的民族服装, 商业摄影, 室外, 电影级光照, 半身特写, 精致的淡妆, 锐利的边缘. ";
        DashScopeImageOptions options = DashScopeImageOptions.builder()
                .model("qwen-image-plus") // 1.1.2.0 版本这个模型会有问题，使用下面的模型才行，待修复
//                .model("wan2.2-t2i-flash")
                .build();
        ImageResponse imageResponse = imageModel.call(new ImagePrompt(prompt, options));
        String imgUrl = imageResponse.getResult().getOutput().getUrl();
        System.out.println(imgUrl);
    }

    @Test
    void text2Img3() {
        //模型为通义万相v2
        String prompt = "中国女孩, 圆脸, 看着镜头, 优雅的民族服装, 商业摄影, 室外, 电影级光照, 半身特写, 精致的淡妆, 锐利的边缘. ";
        DashScopeImageOptions options = DashScopeImageOptions.builder()
                .model("wan2.2-t2i-flash")
                .watermark(true) // 添加水印
                .width(1280)
                .height(1280)
                .build();
        ImageResponse imageResponse = imageModel.call(new ImagePrompt(prompt, options));
        String imgUrl = imageResponse.getResult().getOutput().getUrl();
        System.out.println(imgUrl);
    }

    @Test
    void text2Img4() {
        //模型为通义万相v2  生成多张图片
        String prompt = "一只泰迪在玩电脑";
        DashScopeImageOptions options = DashScopeImageOptions.builder()
                .model("wan2.2-t2i-flash")
                .n(3)
                .build();
        ImageResponse imageResponse = imageModel.call(new ImagePrompt(prompt, options));
        List<ImageGeneration> results = imageResponse.getResults();
        List<String> urls = new ArrayList<>(3);
        for (ImageGeneration imageGeneration : results) {
            urls.add(imageGeneration.getOutput().getUrl());
        }
        System.out.println(urls);
    }
}
