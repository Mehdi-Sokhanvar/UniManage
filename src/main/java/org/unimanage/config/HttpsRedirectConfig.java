package org.unimanage.config;

import org.apache.catalina.connector.Connector;
import org.springframework.boot.web.embedded.tomcat.TomcatReactiveWebServerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpsRedirectConfig {
//
//    private Connector createHttpsRedirectConnector() {
//        Connector connector = new Connector("org.apache.coyote.http11.Http11Protocol");
//        connector.setPort(8080);
//        connector.setSecure(false);
//        connector.setScheme("https");
//        connector.setRedirectPort(8443);
//        return connector;
//    }
//
//    @Bean
//    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> containerCustomizer() {
//        return server -> server.addAdditionalTomcatConnectors(createHttpsRedirectConnector());
//    }
}
