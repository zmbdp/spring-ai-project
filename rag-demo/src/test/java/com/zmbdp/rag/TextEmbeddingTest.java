package com.zmbdp.rag;

import com.alibaba.cloud.ai.dashscope.embedding.DashScopeEmbeddingModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class TextEmbeddingTest {

    @Autowired
    private DashScopeEmbeddingModel embeddingModel;

    @Test
    void textEmbed() {
        float[] embed = embeddingModel.embed("稚名不带撇");
        System.out.println(embed.length);
        System.out.println(Arrays.toString(embed));
    }

    @Test
    void batchTextEmbed() {
        List<String> list = List.of(
                "稚名不带撇", "稚名不带撇", "稚名不带撇", "稚名不带撇", "稚名不带撇",
                "稚名不带撇", "稚名不带撇", "稚名不带撇", "稚名不带撇", "稚名不带撇"
                , "稚名不带撇"
        );
        List<float[]> embed = embeddingModel.embed(list);
        System.out.println(embed.size());
    }
}