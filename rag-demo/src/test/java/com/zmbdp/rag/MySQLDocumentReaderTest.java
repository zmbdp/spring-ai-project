package com.zmbdp.rag;

import com.alibaba.cloud.ai.reader.mysql.MySQLDocumentReader;
import com.alibaba.cloud.ai.reader.mysql.MySQLResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class MySQLDocumentReaderTest {
    private MySQLResource mysqlResource;

    private MySQLDocumentReader reader;

    @BeforeEach
    void setUp() {

        List<String> contentColumns = List.of("User", "Host"); //文档的字段
        List<String> metadataColumns = List.of("Select_priv", "Insert_priv");  //元数据的字段

        String query = "SELECT * FROM user LIMIT 10;";
        // 创建 MySQLResource 对象，设置你的数据库信息（比如 ip，端口，用户名，密码，查询语句，文档的字段，元数据的字段）
        mysqlResource = new MySQLResource(
                "localhost", 3306, "mysql",
                "root", "123456",
                query, contentColumns, metadataColumns
        );

        reader = new MySQLDocumentReader(mysqlResource);
    }

    @Test
    void testGetDocuments() {
        List<Document> documents = reader.get();
        System.out.printf("document size: %d \n", documents.size());
        for (Document document : documents) {
            System.out.println(document.getText());
            System.out.println(document.getMetadata());
        }
    }

    @Test
    void testInvalidConnection() {
        // Test with invalid credentials
        MySQLResource invalidResource = new MySQLResource("invalid_host", 3306, "invalid_db", "invalid_user",
                "invalid_pass", "SELECT * FROM test_table", null, null);

        MySQLDocumentReader invalidReader = new MySQLDocumentReader(invalidResource);

        // Should throw RuntimeException
        assertThrows(RuntimeException.class, invalidReader::get);
    }
}
