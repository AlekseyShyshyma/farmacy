package com.khpi.farmacy;

import com.khpi.farmacy.upload.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableConfigurationProperties({
        FileStorageProperties.class
})
@EnableJpaAuditing
public class FarmacyApplication {

    public static void main(String[] args) {
        SpringApplication.run(FarmacyApplication.class, args);
    }
}
