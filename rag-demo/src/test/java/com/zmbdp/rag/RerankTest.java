package com.zmbdp.rag;

import com.alibaba.cloud.ai.advisor.RetrievalRerankAdvisor;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.rerank.DashScopeRerankModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import java.util.List;

@SpringBootTest
public class RerankTest {

    private SimpleVectorStore simpleVectorStore;

    @Autowired
    public RerankTest(EmbeddingModel embeddingModel) {
        this.simpleVectorStore = SimpleVectorStore.builder(embeddingModel).build();
    }

    @BeforeEach
    void testSimpleVectorStore(@Value("classpath:/file/rule.txt") Resource resource) {
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

        TextReader reader = new TextReader(resource);
        List<Document> documents = reader.get();
        TokenTextSplitter splitter = new TokenTextSplitter(200, 50, 5, 1000, true);
        List<Document> apply = splitter.apply(documents);
        simpleVectorStore.add(apply);
        System.out.println("向量存储写入完成");
    }

    @Test
    void testRerank(@Autowired DashScopeRerankModel rerankModel,
                    @Autowired DashScopeChatModel chatModel) {
        ChatClient client = ChatClient.builder(chatModel).build();
        // 1. 定义一个 advisor, 提供 rerank 模型
        RetrievalRerankAdvisor rerankAdvisor = new RetrievalRerankAdvisor(simpleVectorStore,
                rerankModel, SearchRequest.builder().topK(10).build());
        // 2. 把这个 advisor 绑定给 chatclient
        String content = client.prompt()
                .user("金卡会员打几折？")
//                .user("奥运会什么时候举行？")
                .advisors(rerankAdvisor)
                .call()
                .content();
        System.out.println(content);

    }
}
