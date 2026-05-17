package com.zmbdp.rag;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.MetadataMode;
import org.springframework.ai.model.transformer.KeywordMetadataEnricher;
import org.springframework.ai.model.transformer.SummaryMetadataEnricher;
import org.springframework.ai.model.transformer.SummaryMetadataEnricher.SummaryType;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import java.util.List;

@SpringBootTest
public class TransformersTest {

    private final ChatModel chatModel;

    @Autowired
    public TransformersTest(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @Test
    void testSplitter(@Value("classpath:/file/rule.txt") Resource resource) {
        // 加载文档
        TextReader reader = new TextReader(resource);
        List<Document> documents = reader.get();
        System.out.println("文档加载 size:" + documents.size());
        // 文本拆分
//        TokenTextSplitter splitter = new TokenTextSplitter();
        TokenTextSplitter splitter = new TokenTextSplitter(500, 300, 5, 5000, true);
        List<Document> apply = splitter.apply(documents);
        System.out.println("文本拆分 size:" + apply.size());
        apply.forEach(doc -> {
            System.out.println(doc.getText());
            System.out.println(doc.getMetadata());
        });
    }

    @Test
    void testEnricher(@Value("classpath:/file/rule.txt") Resource resource) {
        // 加载文档
        TextReader reader = new TextReader(resource);
        List<Document> documents = reader.get();
        System.out.println("文档加载 size:" + documents.size());
        // 文本拆分
        TokenTextSplitter splitter = new TokenTextSplitter(500, 300, 5, 5000, true);
        List<Document> splitterDoc = splitter.apply(documents);
        System.out.println("文本拆分 size:" + splitterDoc.size());

        // 关键词生成
        KeywordMetadataEnricher enricher = KeywordMetadataEnricher.builder(chatModel)
                .keywordCount(3)
                .build();

//        KeywordMetadataEnricher enricher = KeywordMetadataEnricher.builder(chatModel)
//                .keywordsTemplate(new PromptTemplate("""
//                        根据给定的文本: {context_str}, 生成关键字, 只允许以下关键字
//                        [会员宗旨, 会员类型, 会员注册, 积分制度, 会员权益, 会员行为, 会员服务, 公告, 隐私保护]
//                        只返回关键字, 其他信息不返回
//                        """))
//                .keywordCount(3)
//                .build();

        List<Document> enricherDoc = enricher.apply(splitterDoc);
        System.out.println("关键词生成 size:" + enricherDoc.size());

        enricherDoc.forEach(doc -> {
            System.out.println(doc.getText());
            System.out.println(doc.getMetadata());
        });
    }

    @Test
    void testSummaryEnricher(@Value("classpath:/file/rule.txt") Resource resource) {
        // 加载文档
        TextReader reader = new TextReader(resource);
        List<Document> documents = reader.get();
        // 文本拆分
        TokenTextSplitter splitter = new TokenTextSplitter(500, 300, 5, 5000, true);
        List<Document> splitterDoc = splitter.apply(documents);

        //摘要生成
//        SummaryMetadataEnricher summaryMetadataEnricher = new SummaryMetadataEnricher(chatModel,
//                List.of(SummaryType.PREVIOUS, SummaryType.CURRENT, SummaryType.NEXT));

        String summaryTemplate = """
                根据给定的文本:
                {context_str}
                生成摘要信息, 限制在50字以内. 只返回摘要信息, 其他信息不返回
                """;
        SummaryMetadataEnricher summaryMetadataEnricher = new SummaryMetadataEnricher(chatModel,
                List.of(SummaryType.PREVIOUS, SummaryType.CURRENT, SummaryType.NEXT),
                summaryTemplate, MetadataMode.NONE);

        List<Document> enricherDoc = summaryMetadataEnricher.apply(splitterDoc);

        enricherDoc.forEach(doc -> {
            System.out.println(doc.getText());
            System.out.println(doc.getMetadata());
        });
    }
}
