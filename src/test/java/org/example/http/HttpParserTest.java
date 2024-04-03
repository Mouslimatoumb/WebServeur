package org.example.http;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HttpParserTest {

    private HttpParser httpParser;

    @BeforeAll
    public void beforeClass(){
        httpParser = new HttpParser();
    }

    @Test
    void parseHttpRequest() {
        HttpRequest request = null;

        try {
            request = httpParser.parseHttpRequest(
                    generateValidGetTestCase()
            );
        } catch (HttpParsingException e) {
            fail(e);
        }


        assertNotNull(request);
        assertEquals(request.getMethod(), HttpMethod.GET);
        assertEquals(request.getRequestTarget(), "/");
    }

    @Test
    void parseHttpRequestBadMethod1() {
        try {
            HttpRequest request = httpParser.parseHttpRequest(
                    generateBadGetTestCaseMethodName1()
            );
            fail();
        } catch (HttpParsingException e) {
            assertEquals(e.getErrorCode(), HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED);
        }


    }

    @Test
    void parseHttpRequestBadMethod2() {
        try {
            HttpRequest request = httpParser.parseHttpRequest(
                    generateBadGetTestCaseMethodName2()
            );
            fail();
        } catch (HttpParsingException e) {
            assertEquals(e.getErrorCode(), HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED);
        }


    }

    @Test
    void parseHttpRequestInvNumItems1() {
        try {
            HttpRequest request = httpParser.parseHttpRequest(
                    generateBadGetTestCaseRequestLineInvNumItems1()
            );
            fail();
        } catch (HttpParsingException e) {
            assertEquals(e.getErrorCode(), HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
        }


    }

    @Test
    void parseHttpEmptyRequestLine() {
        try {
            HttpRequest request = httpParser.parseHttpRequest(
                    generateBadGetTestCaseEmptyRequestLine()
            );
            fail();
        } catch (HttpParsingException e) {
            assertEquals(e.getErrorCode(), HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED);
        }


    }

    @Test
    void parseHttpRequestLineCRnoLF() {
        try {
            HttpRequest request = httpParser.parseHttpRequest(
                    generateBadGetTestCaseRequestLineOnlyCRnoLF()
            );
            fail();
        } catch (HttpParsingException e) {
            assertEquals(e.getErrorCode(), HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
        }


    }

    private InputStream generateValidGetTestCase(){
        String donnee = "GET / HTTP/1.1\r\n" +
                "Host: localhost:80\r\n" +
                "Connection: keep-alive\r\n" +
                "Cache-Control: max-age=0\r\n" +
                "sec-ch-ua: \"Google Chrome\";v=\"123\", \"Not:A-Brand\";v=\"8\", \"Chromium\";v=\"123\"\r\n" +
                "sec-ch-ua-mobile: ?0\r\n" +
                "sec-ch-ua-platform: \"Windows\"\r\n" +
                "Upgrade-Insecure-Requests: 1\r\n" +
                "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36\r\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7\r\n" +
                "Sec-Fetch-Site: none\r\n" +
                "Sec-Fetch-Mode: navigate\r\n" +
                "Sec-Fetch-User: ?1\r\n" +
                "Sec-Fetch-Dest: document\r\n" +
                "Accept-Encoding: gzip, deflate, br, zstd\r\n" +
                "Accept-Language: fr-FR,fr;q=0.9,en-US;q=0.8,en;q=0.7\r\n"+
                "\r\n";

        InputStream inputStream = new ByteArrayInputStream(
                donnee.getBytes(
                        StandardCharsets.US_ASCII
                )
        );
        return inputStream;
    }

    private InputStream generateBadGetTestCaseMethodName1(){
        String donnee = "GeT / HTTP/1.1\r\n" +
                "Host: localhost:80\r\n" +
                "Accept-Language: fr-FR,fr;q=0.9,en-US;q=0.8,en;q=0.7\r\n"+
                "\r\n";

        InputStream inputStream = new ByteArrayInputStream(
                donnee.getBytes(
                        StandardCharsets.US_ASCII
                )
        );
        return inputStream;
    }

    private InputStream generateBadGetTestCaseMethodName2(){
        String donnee = "GETTTTTT / HTTP/1.1\r\n" +
                "Host: localhost:80\r\n" +
                "Accept-Language: fr-FR,fr;q=0.9,en-US;q=0.8,en;q=0.7\r\n"+
                "\r\n";

        InputStream inputStream = new ByteArrayInputStream(
                donnee.getBytes(
                        StandardCharsets.US_ASCII
                )
        );
        return inputStream;
    }

    private InputStream generateBadGetTestCaseRequestLineInvNumItems1(){
        String donnee = "GET / AAAA HTTP/1.1\r\n" +
                "Host: localhost:80\r\n" +
                "Accept-Language: fr-FR,fr;q=0.9,en-US;q=0.8,en;q=0.7\r\n"+
                "\r\n";

        InputStream inputStream = new ByteArrayInputStream(
                donnee.getBytes(
                        StandardCharsets.US_ASCII
                )
        );
        return inputStream;
    }

    private InputStream generateBadGetTestCaseEmptyRequestLine(){
        String donnee = "\r\n" +
                "Host: localhost:80\r\n" +
                "Accept-Language: fr-FR,fr;q=0.9,en-US;q=0.8,en;q=0.7\r\n"+
                "\r\n";

        InputStream inputStream = new ByteArrayInputStream(
                donnee.getBytes(
                        StandardCharsets.US_ASCII
                )
        );
        return inputStream;
    }

    private InputStream generateBadGetTestCaseRequestLineOnlyCRnoLF(){
        String donnee = "GET / HTTP/1.1\r" +
                "Host: localhost:80\r\n" +
                "Accept-Language: fr-FR,fr;q=0.9,en-US;q=0.8,en;q=0.7\r\n"+
                "\r\n";

        InputStream inputStream = new ByteArrayInputStream(
                donnee.getBytes(
                        StandardCharsets.US_ASCII
                )
        );
        return inputStream;
    }

}