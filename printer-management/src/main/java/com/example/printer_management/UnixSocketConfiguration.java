import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class UnixSocketConfiguration {

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> tomcatSocketCustomizer() {
        return (factory) -> {
            factory.addConnectorCustomizers((connector) -> {
                String projectRoot = System.getProperty("user.dir");
                Path socketPath = Paths.get(projectRoot + "/printerapp.sock").normalize();
                connector.setProperty("address", "unix:" + socketPath);
            });
        };
    }
}
