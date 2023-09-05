package org.webServer;

import org.config.Config;
import org.junit.Before;  // junit5에서 삭제
import org.junit.jupiter.api.BeforeEach; // @Before와 차이
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Properties;

class RequestProcessorTest {

    public RequestProcessor requestProcessor;
    public HashMap<String,String> configMap;

    @BeforeEach
    public void setup() throws IOException {
        InputStream inputStream = new FileInputStream("src/main/resources/info.properties");
        Properties properties = new Properties();
        properties.load(inputStream);
        Config config = new Config();
        requestProcessor = new RequestProcessor(new Socket());
        configMap = config.loopConvert(properties);
    }

    // 호스트별 HTTP_DOC_ROOT 정보 테스트
    @Test
    public void getHostRoot(){
        String hostRoot = requestProcessor.getHostRoot("hhh.com", configMap); // 호스트정보 입력
        String propRoot = "C:\\workspace\\JavaWebServer\\src\\main\\webapp1";
        assertEquals(hostRoot, propRoot);
    }

    // 에러코드 403
    @Test
    public void responseErrBody403() throws IOException {
        String response = requestProcessor.responseErrBody("localhost", configMap, ".403");
        String body = new StringBuilder("<!DOCTYPE html>\r\n")
                .append("<html lang=\"en\">\r\n")
                .append("<head>\r\n")
                .append("    <meta charset=\"UTF-8\">\r\n")
                .append("    <title>Forbidden</title>\r\n")
                .append("</head>\r\n")
                .append("<body>\r\n")
                .append("    <H1>HTTP Error 403: Forbidden</H1>\r\n")
                .append("</body>\r\n")
                .append("</html>").toString();
        assertEquals(response, body);
    }

    @Test
    void responseErrBody404() throws IOException {
        String response = requestProcessor.responseErrBody("localhost", configMap, ".404");
        String body = new StringBuilder("<!DOCTYPE html>\r\n")
                .append("<html lang=\"en\">\r\n")
                .append("<head>\r\n")
                .append("    <meta charset=\"UTF-8\">\r\n")
                .append("    <title>File Not Found</title>\r\n")
                .append("</head>\r\n")
                .append("<body>\r\n")
                .append("    <H1>HTTP Error 404: File Not Found</H1>\r\n")
                .append("</body>\r\n")
                .append("</html>").toString();
        assertEquals(response, body);
    }

    @Test
    void responseErrBody500() throws IOException {
        String response = requestProcessor.responseErrBody("localhost", configMap, ".500");
        String body = new StringBuilder("<!DOCTYPE html>\r\n")
                .append("<html lang=\"en\">\r\n")
                .append("<head>\r\n")
                .append("    <meta charset=\"UTF-8\">\r\n")
                .append("    <title>Internal Server Error</title>\r\n")
                .append("</head>\r\n")
                .append("<body>\r\n")
                .append("    <H1>HTTP Error 500: Internal Server Error</H1>\r\n")
                .append("</body>\r\n")
                .append("</html>").toString();
        assertEquals(response, body);
    }
}