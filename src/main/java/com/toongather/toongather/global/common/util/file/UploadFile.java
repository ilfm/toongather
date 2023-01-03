package com.toongather.toongather.global.common.util.file;

import lombok.Data;

/*
 *  FileName : UploadFile.java
 *  - 업로드 파일정보 저장
 * */

@Data
public class UploadFile {

    private String uploadFileName;  // 사용자가 업로드한 파일명
    private String storeFileName;   //  서버내부에서 관리하는 파일명

    public UploadFile(String uploadFileName, String storeFileName) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
    }
}
