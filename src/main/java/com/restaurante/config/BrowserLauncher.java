package com.restaurante.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.awt.Desktop;
import java.net.URI;

@Configuration
@Profile("!prod") // Solo en desarrollo, no en producción
public class BrowserLauncher {

    @Bean
    public CommandLineRunner openBrowser() {
        return args -> {
            String url = "http://localhost:9090/login.html";
            System.out.println(">>> Abriendo navegador en: " + url);
            
            try {
                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    Desktop.getDesktop().browse(new URI(url));
                } else {
                    Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
                }
            } catch (Exception e) {
                System.out.println(">>> No se pudo abrir el navegador automáticamente. Abre manualmente: " + url);
            }
        };
    }
}
