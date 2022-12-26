package com.example.hirememicroserviceUser;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.io.IOException;
import java.io.InputStream;

@SpringBootApplication
@EnableConfigurationProperties
public class HireMeMicroserviceUserApplication {

	public static void main(String[] args) throws IOException {

		//Initialize firebase sdk
		ClassLoader classLoader = HireMeMicroserviceUserApplication.class.getClassLoader();
        InputStream serviceAccount = classLoader.getResourceAsStream("serviceAccountKey.json");

		//Firebase
		FirebaseOptions options = FirebaseOptions.builder()
						.setCredentials(GoogleCredentials.fromStream(serviceAccount))
								.build();

		FirebaseApp.initializeApp(options);

		SpringApplication.run(HireMeMicroserviceUserApplication.class, args);
	}

}
