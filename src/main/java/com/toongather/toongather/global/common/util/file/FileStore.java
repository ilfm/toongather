package com.toongather.toongather.global.common.util.file;

/*
 *  FileName : FileStore.java
 *  - 파일 업로드
 * */

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Component
public class FileStore {

  @Value("${file.dir}")
  private String fileDir;

  public String getFullPath(String filename) {
    return fileDir + filename;
  }

  public List<UploadFile> storeFiles(List<MultipartFile> multipartFiles) throws IOException {
    List<UploadFile> storeFileResult = new ArrayList<>();
    for(MultipartFile file:multipartFiles) {
      if (!file.isEmpty()) {
        storeFileResult.add(storeFile(file));
      }
    }
    return storeFileResult;
  }

  public UploadFile storeFile(MultipartFile multipartFile) throws IOException {

    if (multipartFile.isEmpty()) {
      return null;
    }

    String originalFilename = multipartFile.getOriginalFilename();
    String storeFileName = createStorefileName(originalFilename);
    String fullPath = getFullPath(storeFileName);
    multipartFile.transferTo(new File(fullPath));
    return new UploadFile(originalFilename, storeFileName, fullPath);
  }

  private String createStorefileName(String originalFilename) {
    String ext = extractExt(originalFilename);
    String uuid = UUID.randomUUID().toString();
    return uuid + "." + ext;
  }

  private String extractExt(String originalFilename) {
    int pos = originalFilename.lastIndexOf(".");
    return originalFilename.substring(pos + 1);
  }

}
