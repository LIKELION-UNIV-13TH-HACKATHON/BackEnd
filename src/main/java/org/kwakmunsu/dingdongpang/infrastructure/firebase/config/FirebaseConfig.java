package org.kwakmunsu.dingdongpang.infrastructure.firebase.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import java.io.InputStream;
import lombok.extern.slf4j.Slf4j;
import org.kwakmunsu.dingdongpang.global.exception.InternalServerException;
import org.kwakmunsu.dingdongpang.global.exception.dto.ErrorStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Slf4j
@Configuration
public class FirebaseConfig {

    @Bean
    public FirebaseApp initFirebase() {
        try {
            // FirebaseApp이 이미 초기화되어 있으면 기존 것 반환
            if (!FirebaseApp.getApps().isEmpty()) {
                return FirebaseApp.getInstance();
            }
            String path = "firebase.json";
            ClassPathResource resource = new ClassPathResource(path);
            InputStream serviceAccount = resource.getInputStream();

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            FirebaseApp app = FirebaseApp.initializeApp(options);
            log.info("FirebaseApp has been initialized.");

            return app;
        } catch (Exception e) {
            throw new InternalServerException(ErrorStatus.FAIL_FIREBASE_INITIALIZE, e);// 파이어베이스 등록이 안됐다면 과연 에러를 던져야할까?
        }
    }

    @Bean
    public FirebaseMessaging firebaseMessaging(FirebaseApp firebaseApp) {
        return FirebaseMessaging.getInstance(firebaseApp);
    }

}