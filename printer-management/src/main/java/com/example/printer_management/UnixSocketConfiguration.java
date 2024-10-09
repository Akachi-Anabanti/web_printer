import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UnixSocketConfiguration {

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> tomcatSocketCustomizer() {
        return (factory) -> {
            factory.addConnectorCustomizers((connector) -> {
                String projectRoot = System.getProperty("user.dir");
                String socketPath = StringUtils.cleanPath(projectRoot + "/printerapp.sock");
                connector.setProperty("address", "unix:" + socketPath);
            });
        };
    }
}
