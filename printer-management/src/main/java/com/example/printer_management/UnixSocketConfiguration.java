import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class UnixSocketConfiguration {

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> tomcatSocketCustomizer() {
        return (factory) -> {
            factory.addConnectorCustomizers((connector) -> {
                String projectRoot = System.getProperty("user.dir");
                Path socketPath = Paths.get(projectRoot, "printerapp.sock").toAbsolutePath().normalize();
                connector.setProperty("address", "unix:" + socketPath);
                connector.setProperty("port", "-1"); // Disable TCP port binding
                connector.setProperty("unixDomainSocketPath", socketPath.toString());
            });
        };
    }
}
