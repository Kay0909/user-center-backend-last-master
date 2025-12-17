package com.zkg.usercenter.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupLogger implements CommandLineRunner {

    @Value("${server.port:8080}")
    private String serverPort;

    @Value("${server.servlet.context-path:/}")
    private String contextPath;

    @Override
    public void run(String... args) throws Exception {
        String localAddress = "http://localhost:" + serverPort;
        String swaggerUiUrl = localAddress + contextPath + "/doc.html";

        System.out.println("----------------------------------------------------------");
        System.out.println("Local Address: " + localAddress);
        System.out.println("Context Path: " + contextPath);
        System.out.println("Swagger UI URL: " + swaggerUiUrl);
        System.out.println("----------------------------------------------------------");
    }
}
