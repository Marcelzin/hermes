package com.tcc.pdv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

@SpringBootApplication
public class PdvApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(PdvApplication.class);
        app.addListeners(new ApplicationListener<ApplicationReadyEvent>() {
            @Override
            public void onApplicationEvent(ApplicationReadyEvent event) {
                String port = event.getApplicationContext().getEnvironment().getProperty("server.port", "8080");
                System.out.println("Aplicação rodando! Acesso: http://localhost:" + port);
            }
        });
        app.run(args);
    }
}