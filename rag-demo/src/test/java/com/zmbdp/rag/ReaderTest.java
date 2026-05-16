package com.zmbdp.rag;

import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.JsonReader;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.reader.jsoup.JsoupDocumentReader;
import org.springframework.ai.reader.jsoup.config.JsoupDocumentReaderConfig;
import org.springframework.ai.reader.markdown.MarkdownDocumentReader;
import org.springframework.ai.reader.markdown.config.MarkdownDocumentReaderConfig;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.ParagraphPdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import java.util.List;

@SpringBootTest
public class ReaderTest {
    @Test
    void testJsonReader(@Value("classpath:/file/web.json") Resource resource) throws InterruptedException {
        JsonReader reader = new JsonReader(resource); // 全部读出来
        JsonReader readerSites = new JsonReader(resource, "sites"); // 指定读取的 key
        JsonReader readerSites2 = new JsonReader(resource, "sites2"); // 指定读取的 key
        List<Document> documents = reader.get();
        List<Document> documentsSites = readerSites.get();
        List<Document> documentsSites2 = readerSites2.get();

        for (Document document : documents) {
            System.out.println(document.getText());
        }
        for (Document document : documentsSites) {
            System.out.println(document.getText());
        }
        for (Document document : documentsSites2) {
            System.out.println(document.getText());
        }
    }

    @Test
    void testJsonReader2(@Value("classpath:/file/webArray.json") Resource resource) {
//        JsonReader reader = new JsonReader(resource);
        JsonReader reader = new JsonReader(resource, "id", "brand");
        List<Document> documents = reader.get();
        System.out.printf("document size: %d\n", documents.size());
        for (Document document : documents) {
            System.out.println(document.getText());
        }
    }

    @Test
    void testTextReader(@Value("classpath:/file/text.txt") Resource resource) {
        TextReader reader = new TextReader(resource);
        reader.getCustomMetadata().put("filename", "重要文档");
        List<Document> documents = reader.get();
        System.out.printf("document size: %d \n", documents.size());
        for (Document document : documents) {
            System.out.println(document.getText());
        }
    }

    @Test
    void testTextReader() {
        TextReader reader = new TextReader("classpath:/file/text.txt");
        List<Document> documents = reader.get();
        System.out.printf("document size: %d \n", documents.size());
        for (Document document : documents) {
            System.out.println(document.getText());
        }
    }

    @Test
    void testHtmlReader(@Value("classpath:/file/my-page.html") Resource resource) {
        JsoupDocumentReaderConfig config = JsoupDocumentReaderConfig.builder()
                .selector("article p") // 提取 <article> 标签内的段落
                .charset("ISO-8859-1")  // 使用 ISO-8859-1 编码
                .includeLinkUrls(true) // 在元数据中包含链接 URL
                .metadataTags(List.of("author", "date")) // 提取作者和日期元标签
                .additionalMetadata("source", "my-page.html") // 添加自定义元数据
                .build();

        JsoupDocumentReader reader = new JsoupDocumentReader(resource, config);
        List<Document> documents = reader.get();
        System.out.printf("document size: %d \n", documents.size());
        for (Document document : documents) {
            System.out.println(document.getText());
            System.out.println(document.getMetadata());
        }
    }

    @Test
    void testMarkdownReader(@Value("classpath:/file/code.md") Resource resource) {
        MarkdownDocumentReaderConfig config = MarkdownDocumentReaderConfig.builder()
                .withHorizontalRuleCreateDocument(true)
                .withIncludeCodeBlock(false)
                .withIncludeBlockquote(false)
                .withAdditionalMetadata("filename", "code.md")
                .build();

        MarkdownDocumentReader reader = new MarkdownDocumentReader(resource, config);
        List<Document> documents = reader.get();
        System.out.printf("document size: %d \n", documents.size());
        for (Document document : documents) {
            System.out.println(document.getText());
            System.out.println(document.getMetadata());
        }
    }

    @Test
    void testPdfReader() {
        PagePdfDocumentReader pdfReader = new PagePdfDocumentReader("classpath:/file/sample1.pdf",
                PdfDocumentReaderConfig.builder()
                        .withPageTopMargin(0)
                        .withPageExtractedTextFormatter(ExtractedTextFormatter.builder()
                                .withNumberOfTopTextLinesToDelete(0)
                                .build())
                        .withPagesPerDocument(1)
                        .build());

//        PagePdfDocumentReader pdfReader = new PagePdfDocumentReader("classpath:/file/sample1.pdf");
        List<Document> documents = pdfReader.get();
        System.out.printf("document size: %d \n", documents.size());
        for (Document document : documents) {
            System.out.println(document.getText());
            System.out.println(document.getMetadata());
        }
    }

    @Test
    void testPdfReader2() {
        ParagraphPdfDocumentReader pdfReader = new ParagraphPdfDocumentReader("classpath:/file/sample1.pdf",
                PdfDocumentReaderConfig.builder()
                        .withPageTopMargin(0)
                        .withPageExtractedTextFormatter(ExtractedTextFormatter.builder()
                                .withNumberOfTopTextLinesToDelete(0)
                                .build())
                        .withPagesPerDocument(1)
                        .build());

//        ParagraphPdfDocumentReader pdfReader = new ParagraphPdfDocumentReader("classpath:/file/sample1.pdf");
        List<Document> documents = pdfReader.get();
        System.out.printf("document size: %d \n", documents.size());
        for (Document document : documents) {
            System.out.println(document.getText());
            System.out.println(document.getMetadata());
        }
    }

    @Test
    void testTikaReader(@Value("classpath:/file/tika.docx") Resource resource) {
        TikaDocumentReader reader = new TikaDocumentReader(resource);
        List<Document> documents = reader.get();
        System.out.printf("document size: %d \n", documents.size());
        for (Document document : documents) {
            System.out.println(document.getText());
            System.out.println(document.getMetadata());
        }
    }

    @Test
    void testTikaReader2(@Value("classpath:/file/ppt-sample.pptx") Resource resource) {
        TikaDocumentReader reader = new TikaDocumentReader(resource);
        List<Document> documents = reader.get();
        System.out.printf("document size: %d \n", documents.size());
        for (Document document : documents) {
            System.out.println(document.getText());
            System.out.println(document.getMetadata());
        }
    }

    @Test
    void testTikaReader3(@Value("classpath:/file/sample1.pdf") Resource resource) {
        TikaDocumentReader reader = new TikaDocumentReader(resource);
        List<Document> documents = reader.get();
        System.out.printf("document size: %d \n", documents.size());
        for (Document document : documents) {
            System.out.println(document.getText());
            System.out.println(document.getMetadata());
        }
    }
}
