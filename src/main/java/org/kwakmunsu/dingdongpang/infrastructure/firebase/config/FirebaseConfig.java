package org.kwakmunsu.dingdongpang.infrastructure.firebase.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import java.io.InputStream;
import lombok.extern.slf4j.Slf4j;
import org.kwakmunsu.dingdongpang.global.exception.InternalServerException;
import org.kwakmunsu.dingdongpang.global.exception.dto.ErrorStatus;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Slf4j
@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void initFirebase() {
        try {
            String path = "firebase.json";
            ClassPathResource resource = new ClassPathResource(path);
            InputStream serviceAccount = resource.getInputStream();

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                log.info("FirebaseApp has been initialized.");
            }
        } catch (Exception e) {
            throw new InternalServerException(ErrorStatus.FAIL_FIREBASE_INITIALIZE, e);// 파이어베이스 등록이 안됐다면 과연 에러를 던져야할까?
        }
    }

}