package com.zmbdp.rag;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootTest
public class VectorStoreTest {

//    @Autowired
//    private DashScopeEmbeddingModel embeddingModel;

    @Autowired
    private SimpleVectorStore simpleVectorStore;

    @Autowired
    private ChatModel chatModel;

    @BeforeEach
    void setUp() {
        Document doc = Document.builder()
                .text("2025年夏季奥运会将于巴黎举行, 预计吸引全球数百万观众")
                .build();
        Document doc2 = Document.builder()
                .text("对比学习框架下多语言BERT模型的语义表示分析")
                .build();
        Document doc3 = Document.builder()
                .text("暮色中的老槐树在风中摇曳, 枯枝划破绯红的晚霞")
                .build();
        Document doc4 = Document.builder()
                .text("基于Transformer的预训练模型在机器翻译中的迁移学习研究")
                .build();
        Document doc5 = Document.builder()
                .text("机器学习是未来趋势")
                .build();
        Document doc6 = Document.builder()
                .text("机器学习!")
                .build();
        List<Document> documentList = List.of(doc, doc2, doc3, doc4, doc5, doc6);
        simpleVectorStore.add(documentList);
        System.out.println("向量数据库存储成功");
    }

    @Test
    void save() {
    }

    @Test
    void search() {
//        List<Document> documents = simpleVectorStore.similaritySearch("机器学习");
//        documents.forEach(System.out::println);
        SearchRequest request = SearchRequest.builder()
                .query("机器学习")
                .similarityThreshold(0.6)
                .topK(30)
                .build();
        simpleVectorStore.similaritySearch(request).forEach(System.out::println);
    }

    @Test
    void testRag() {
        ChatClient chatClient = ChatClient.builder(chatModel).build();
        QuestionAnswerAdvisor advisor = QuestionAnswerAdvisor.builder(simpleVectorStore).build();
        String content = chatClient
//                .prompt(new Prompt("奥运会什么时间举行？",
                .prompt(new Prompt("未来的趋势是啥？",
                                DashScopeChatOptions.builder()
                                        .model("qwen-max-latest")
                                        .build()
                        )
                )
                .advisors(advisor)
                .call()
                .content();
        System.out.println(content);
    }

    @Test
    void testRag3() {
        ChatClient chatClient = ChatClient.builder(chatModel).build();
//        String message = "中国的首都是哪里";
        String message = "奥运会什么时候举行？"; // 正常他会说不可能 2025 年
        PromptTemplate template = new PromptTemplate("""
                {query}
                         下面是上下文信息
                ---------------------
                {question_answer_context}
                ---------------------
                         根据给定的上下文信息进行回复, 并遵守以下规则:
                         1. 如果上下文信息中有相关的知识, 使用上下文的信息进行回复
                         2. 如果上下文信息中没有相关的知识, 直接回复"不知道"
                         3. 在回复时, 尽量避免使用"根据提供的信息...", "根据上下文信息..."类似的话语
                """);
        QuestionAnswerAdvisor advisor = QuestionAnswerAdvisor
                .builder(simpleVectorStore)
                .promptTemplate(template)
                .searchRequest(SearchRequest
                        .builder()
                        .query(message)
                        .similarityThreshold(0.5)
                        .topK(5)
                        .build())
                .build();

        String content = chatClient
                .prompt(new Prompt(message,
                                DashScopeChatOptions.builder()
                                        .model("qwen-max-latest")
                                        .build()
                        )
                )
                .advisors(advisor)
                .call()
                .content();
        System.out.println(content);
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        public SimpleVectorStore simpleVectorStore(EmbeddingModel embeddingModel) {
            return SimpleVectorStore.builder(embeddingModel).build();
        }
    }
}
