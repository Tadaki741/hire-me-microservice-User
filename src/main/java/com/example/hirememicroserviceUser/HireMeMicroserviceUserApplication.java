package com.example.hirememicroserviceUser;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;

@SpringBootApplication
public class HireMeMicroserviceUserApplication {

    public static void main(String[] args) throws IOException {

        try {
            //Initialize the firebase SDK
            ClassLoader classLoader = HireMeMicroserviceUserApplication.class.getClassLoader();
            File file = new File(Objects.requireNonNull(classLoader.getResource("")).getFile());
            FileInputStream serviceAccount = new FileInputStream(file.getAbsolutePath());

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            FirebaseApp.initializeApp(options);
        } catch (FileNotFoundException e) {
            System.out.println("JSON FILE NOT FOUND !!!");
        }


        SpringApplication.run(HireMeMicroserviceUserApplication.class, args);
    }

}
