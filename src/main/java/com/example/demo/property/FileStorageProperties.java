package com.example.demo.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

// Fileをアップロードするディレクトリを設定するクラス
@Component
@ConfigurationProperties(prefix = "file") // application.propertiesと自動的にバインディングする(prefixで対象の内容を指定することも可能)
public class FileStorageProperties {
    private String uploadDir;

    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }
}
