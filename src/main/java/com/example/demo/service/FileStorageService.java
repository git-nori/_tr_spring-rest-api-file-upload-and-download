package com.example.demo.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.exception.FileNotFoundException;
import com.example.demo.exception.FileStorageException;
import com.example.demo.property.FileStorageProperties;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    // FileStoragePropertiesクラスからファイルの格納先ディレクトリを取得し、ディレクトリの作成を行う
    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception e) {
            throw new FileStorageException("Could not create the directory => " + this.fileStorageLocation, e);
        }
    }

    public String storeFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename()); // 「path / ..」などのシーケンスと内部の単純なドットを抑制して、パスを正規化

        try {
            if (fileName.contains("..")) {
                throw new FileStorageException("FileName contains invalid path sequence => " + fileName);
            }

            /*
             *  ex.
             *  /Users/satouhironori/Desktop/ProjectAll/java/spring-rest-api-fileupload-upload-download-example/uploads/${fileName}
             */
            Path tagetLocation = this.fileStorageLocation.resolve(fileName); // fileの絶対パスを取得
            Files.copy(file.getInputStream(), tagetLocation, StandardCopyOption.REPLACE_EXISTING); // fileのコピー(fileが存在していたら上書きをするようOptionで設定)

            return fileName;
        } catch (IOException e) {
            throw new FileStorageException("Could not store file" + fileName);
        }
    }

    // 引数で渡したfileNameを元にResourceオブジェクトを返す
    public Resource loadFileAsResouce(String fileName) {
        Path filePath = this.fileStorageLocation.resolve(fileName); // fileの絶対パスを取得
        try {
            Resource resource = new UrlResource(filePath.toUri()); // 引数のURIを元にURIオブジェクトを格納
            if (!resource.exists()) {
                throw new FileNotFoundException("File not found => " + fileName);
            }
            return resource;
        } catch (MalformedURLException e) {
            throw new FileNotFoundException("File not found => " + fileName);
        }
    }
}
