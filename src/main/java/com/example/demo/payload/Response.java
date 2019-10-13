package com.example.demo.payload;

import lombok.Data;

@Data
public class Response {
    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private long size;

    public Response(String fileName, String fileDownloadUrl, String fileType, long size) {
        this.fileName = fileName;
        this.fileDownloadUri = fileDownloadUrl;
        this.fileType = fileType;
        this.size = size;
    }
}
