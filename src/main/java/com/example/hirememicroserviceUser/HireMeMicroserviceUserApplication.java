package com.example.hirememicroserviceUser;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;


import java.io.*;

import java.util.Objects;
import java.util.logging.Logger;

@SpringBootApplication
@EnableConfigurationProperties
public class HireMeMicroserviceUserApplication {

    private static final Logger logger = Logger.getLogger(HireMeMicroserviceUserApplication.class.getName());

    public static void main(String[] args) {
        SpringApplication.run(HireMeMicroserviceUserApplication.class, args);
        logger.info(" --> @SpringBootApplication main function running");
    }

    //Initialize the firebase prior to starting the springboot app
    @Bean
    CommandLineRunner commandLineRunner() {
        return args -> {
            try {
                //Initialize the firebase SDK
                ClassLoader classLoader = HireMeMicroserviceUserApplication.class.getClassLoader();
                InputStream serviceAccount = classLoader.getResourceAsStream("serviceAccountKey.json");


                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .build();

                FirebaseApp.initializeApp(options);
                logger.info(" --> Firebase SDK Initialized");
            } catch (FileNotFoundException e) {
                logger.severe(" --> JSON resource file not found !!!");
            }
        };
    }

}
