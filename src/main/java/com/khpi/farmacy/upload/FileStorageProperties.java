package com.khpi.farmacy.upload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="file")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileStorageProperties {

    private String uploadDir;
}
