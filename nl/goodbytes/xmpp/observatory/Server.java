package nl.goodbytes.xmpp.observatory;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.*;
import java.io.*;
import java.net.*;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Server {

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/scan", new MyHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
    }

    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            String domain = t.getRequestURI().toString().substring("/scan/".length());
            String response = "";
            try {
                InetAddress.getByName(domain); // Input 'validation'
                response = scan(domain);
                t.sendResponseHeaders(200, response.getBytes().length);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                response = "Unable to complete request.";
                t.sendResponseHeaders(400, response.length());
            }
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    public static String scan(final String domain) throws IOException {
        Runtime rt = Runtime.getRuntime();
        String[] commands = {"/bin/sh", "-c", "docker run --rm coroner/cryptolyzer --output-format=markdown tls all xmpp://" + domain + " | pandoc --from markdown --to html --standalone --metadata title=\"Analysis of: "+domain+"\"" };
        Process proc = rt.exec(commands);

        BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));

        StringBuffer sb = new StringBuffer();
        String s;
        while ((s = stdInput.readLine()) != null) {
            sb.append(s);
        }

        return sb.toString();
    }
}
