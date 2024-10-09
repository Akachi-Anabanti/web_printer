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
                connector.setProperty("address", "unix:./printerapp.sock");
            });
        };
    }
}
