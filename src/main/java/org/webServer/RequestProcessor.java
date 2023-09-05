package org.webServer;

import org.header.Header;

import java.io.*;
import java.net.Socket;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
//import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RequestProcessor implements Runnable
{
    private final static Logger logger = Logger.getLogger(RequestProcessor.class.getCanonicalName());
    private File rootDirectory;
    private String indexFileName = "index.html";
    private final Socket connection;
    private HashMap<String,String> configMap;

    public  RequestProcessor(Socket connection){

        this.connection = connection;
    }
    public RequestProcessor(File rootDirectory, String indexFileName, Socket connection, HashMap<String,String> configMap)
    {
        if (rootDirectory.isFile()) {
            throw new IllegalArgumentException(
                    "rootDirectory must be a directory, not a file");
        }

        try {
            rootDirectory = rootDirectory.getCanonicalFile();
        }
        catch (IOException ex) {
            logger.severe( ex.getLocalizedMessage() );
        }

        if (indexFileName != null) {
            this.indexFileName = indexFileName;
        }

        this.rootDirectory = rootDirectory;
        this.connection = connection;
        this.configMap = configMap;
    }


    @Override
    public void run()
    {
        // for security checks
        String root = rootDirectory.getPath();
        logger.info("RequestProcessor Start!!!!!!!");
        try (OutputStream raw = new BufferedOutputStream(connection.getOutputStream());
             Writer out = new OutputStreamWriter(raw);
             InputStream inputStream = connection.getInputStream();  // getInputStream()는 한번만 호출 가능
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
             )
        {
            StringBuilder builder = new StringBuilder();
            String line = null;
            String host = "";

            while (!(line = reader.readLine()).isEmpty())
            {
                builder.append(line).append("\n");
            }

            String headers = builder.toString();
            logger.info("headers : " + builder.toString());
            String[] headerProperties = headers.split("\\n");
            Header header = new Header(headerProperties);
            host = header.getHost();

//            logger.info("connection.getRemoteSocketAddress" + connection.getRemoteSocketAddress() + " " + get);
            String method = header.getMethod();
            String version = header.getVersion();

            try {
                if (method.equals("GET")) {
                    String fileName = header.getAccessFile();

                    // 호스트별 경로 설정
                    if (fileName.endsWith("/")) {
                        root = getHostRoot(host, configMap);
                        fileName += "index.html";
                    } else {
                        root = getHostRoot(host, configMap);
                    }
                    String contentType = URLConnection.getFileNameMap().getContentTypeFor(fileName);
                    File theFile = new File(root, fileName.substring(1, fileName.length()));

                    // 허용되지 않은 mime type
                    String fileSubs = fileName.substring(fileName.indexOf(".") + 1, fileName.length());
                    String[] blockMimeType = configMap.get("blockMimeType").split("\\,");

                    if (theFile.canRead() && theFile.getCanonicalPath().startsWith(root)) {
                        logger.info("HTTP/1.0 200 OK!!!!");
                        byte[] theData = Files.readAllBytes(theFile.toPath());
                        if (version.startsWith("HTTP/")) { // send a MIME header
                            sendHeader(out, "HTTP/1.0 200 OK", contentType, theData.length);
                        }
                        raw.write(theData);
                        raw.flush();
                    }

                    // 403 error
                    // .properties에 저장한 허용되지 않은 mime type 확장자를 검사
                    // HTTP_DOC_ROOT인지 검사
                    else if (Arrays.asList(blockMimeType).contains(fileSubs) || !theFile.getCanonicalPath().startsWith(root)) {
                        logger.info("HTTP/1.0 403 forbidden!!!!");
                        String body = responseErrBody(host, configMap, ".403");
                        if (version.startsWith("HTTP/")) {
                            sendHeader(out, "HTTP/1.0 403 Forbidden", "text/html; charset=utf-8", body.length());
                        }
                        out.write(body);
                        out.flush();
                    }

                    // 404 error
                    // HTTP_DOC_ROOT 상에 없는지 검사
                    else if (!theFile.exists() && theFile.getCanonicalPath().startsWith(root)) {
                        logger.info("HTTP/1.0 404 file not found!!!!");

                        String body = responseErrBody(host, configMap, ".404");
                        if (version.startsWith("HTTP/")) { // send a MIME header
                            sendHeader(out, "HTTP/1.0 404 File Not Found", "text/html; charset=utf-8", body.length());
                        }
                        out.write(body);
                        out.flush();
                    }

                    else {
                        throw new IOException();
                    }

                } else {
                    // method does not equal "GET"
                    String body = new StringBuilder("<HTML>\r\n").append("<HEAD><TITLE>Not Implemented</TITLE>\r\n").append("</HEAD>\r\n")
                            .append("<BODY>")
                            .append("<H1>HTTP Error 501: Not Implemented</H1>\r\n")
                            .append("</BODY></HTML>\r\n").toString();
                    if (version.startsWith("HTTP/")) { // send a MIME header
                        sendHeader(out, "HTTP/1.0 501 Not Implemented",
                                "text/html; charset=utf-8", body.length());
                    }
                    out.write(body);
                    out.flush();
                }
            }

            // 500 error
            // 내부 로직 오류 발생시
            catch (IOException e){
                String body = responseErrBody(host, configMap, ".500");
                sendHeader(out, "HTTP/1.0 500 Internal Server Error", "text/html; charset=utf-8", body.length());
                out.write(body);
                out.flush();
            }

        }
        catch (IOException ex) {
            logger.log(Level.WARNING, "Error talking to " + connection.getRemoteSocketAddress(), ex);
        }
        finally {
            try {
                connection.close();
            }
            catch (IOException ex) {
            }
        }
    }

    private void sendHeader(Writer out, String responseCode, String contentType, int length)
            throws IOException {
        out.write(responseCode + "\r\n");
        Date now = new Date();
        out.write("Date: " + now + "\r\n");
        out.write("Server: JHTTP 2.0\r\n");
        out.write("Content-length: " + length + "\r\n");
        out.write("Content-type: " + contentType + "\r\n\r\n");
        out.flush();
    }

    public String getHostRoot(String host, HashMap<String,String> configMap){
        String root;
        if(host.indexOf(".") > -1){
            root = configMap.get("server.HTTP_DOC_ROOT." + host.substring(0, host.indexOf(".")));
        } else {
            root = configMap.get("server.HTTP_DOC_ROOT.localhost");
        }
            return root;
    }
    public String responseErrBody(String host, HashMap<String,String> configMap, String errCode) throws IOException {
        String subhost = host.substring(0, host.indexOf(".") > -1 ? host.indexOf(".") : host.length());
        String hostDocRoot = "server.HTTP_DOC_ROOT." + subhost;
        byte[] theData = Files.readAllBytes(Paths.get(configMap.get(hostDocRoot) + configMap.get("server.HTTP_DOC_ROOT" + errCode)));
        String body = new String(theData, StandardCharsets.UTF_8);
        return body;
    }
}