package com.example.hirememicroserviceUser;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Logger;

@SpringBootApplication
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
                File file = new File(Objects.requireNonNull(classLoader.getResource("serviceAccountKey.json")).getFile());
                FileInputStream serviceAccount = new FileInputStream(file.getAbsolutePath());

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
