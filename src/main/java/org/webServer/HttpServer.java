package org.webServer;


import org.config.Config;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by cybaek on 15. 5. 22..
 */
public class HttpServer {
    private static final Logger logger = Logger.getLogger(HttpServer.class.getCanonicalName());
    private static final int NUM_THREADS = 50;
    private static final String INDEX_FILE = "index.html";
    private final File rootDirectory;
    private final int port;

    private static HashMap<String,String> configMap;

    public HttpServer(File rootDirectory, int port) throws IOException
    {
        if (!rootDirectory.isDirectory()) {
            throw new IOException(rootDirectory + " does not exist as a directory");
        }

        this.rootDirectory = rootDirectory;
        this.port = port;
    }

    public void start() throws IOException
    {
        ExecutorService pool = Executors.newFixedThreadPool(NUM_THREADS);

        try (ServerSocket server = new ServerSocket(port))
        {
            logger.info("Accepting connections on port : " + server.getLocalPort());

            while (true)
            {
                try {
                    Socket request = server.accept();

                    Runnable r = new RequestProcessor(rootDirectory, INDEX_FILE, request, configMap);
                    pool.submit(r);
                }
                catch (IOException ex) {
                    logger.log(Level.WARNING, "Error accepting connection", ex);
                }
            }
        }
    }

    public static void main(String[] args) throws IOException
    {
        Properties properties = new Properties();
        Config config = new Config();
        ClassLoader cl;
//        cl = Thread.currentThread().getContextClassLoader();
        cl = HttpServer.class.getClassLoader();
        if( cl == null )
            cl = ClassLoader.getSystemClassLoader();
//        URL url = cl.getResource( "info.properties" );    https://sthyun.tistory.com/entry/java%EC%97%90%EC%84%9C-property%ED%8C%8C%EC%9D%BC-%EC%89%BD%EA%B2%8C-%EC%B0%BE%EA%B8%B0-ClassLoader
//        filepath = url.getFile();   https://www.techiedelight.com/ko/read-properties-files-java/
        logger.info("cl : " + cl);
//        logger.info("url : " + url);

        try (InputStream inputStream = cl.getResourceAsStream("info.properties");
             Reader reader = new InputStreamReader(inputStream)) {

            properties.load(reader);
        }

        configMap = config.loopConvert(properties);
        logger.info("configMap.get(\"server.HTTP_DOC_ROOT.localhost\" ) : " + configMap.get("server.HTTP_DOC_ROOT.localhost" ));
        File docRoot = new File(configMap.get("server.HTTP_DOC_ROOT.localhost" ));

        int port = Integer.parseInt(configMap.get("server.port"));

        try {
            HttpServer webserver = new HttpServer(docRoot, port);
            webserver.start();
        }
        catch (IOException ex) {
            logger.log(Level.SEVERE, "Server could not start", ex);
        }
    }
}