package com.crewwork.application.file;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UploadFile {

    private String originalFileName;
    private String storeFileName;
    private Long fileSize;

}
