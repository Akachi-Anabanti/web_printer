package com.example.printer_management;

import org.apache.catalina.startup.Tomcat;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.apache.catalina.connector.Connector;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class UnixSocketConfiguration {
    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> tomcatSocketCustomizer() {
        return (factory) -> {
            factory.addConnectorCustomizers((connector) -> {
                // Get the project root directory
                String projectRoot = System.getProperty("user.dir");
                Path socketPath = Paths.get(projectRoot, "printerapp.sock");
                
                // Delete existing socket file if it exists
                File socketFile = socketPath.toFile();
                if (socketFile.exists()) {
                    socketFile.delete();
                }

                // Configure the Unix domain socket
                connector.setProtocol("org.apache.coyote.http11.Http11NioProtocol");
                connector.setScheme("http");
                connector.setSecure(false);
                connector.setPort(0); // Use port 0 instead of -1
                connector.setProperty("unixDomain", "true");
                connector.setProperty("unixDomainSocket", socketPath.toString());
            });
        };
    }
}
