package com.fitnesscommerce.post.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FileMetaInfo {
    private String fileName;
    private String url;

    public FileMetaInfo(String fileName, String url) {
        this.fileName = fileName;
        this.url = url;
    }
}
