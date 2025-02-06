package com.toongather.toongather.global.common.api;


import com.toongather.toongather.global.common.util.file.FileStore;
import java.net.MalformedURLException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class FileController {

      @Autowired
      private FileStore file;

      /*
      * 이미지 불러오기
      * */
      @GetMapping("/images/{filename}")
      public Resource showImage(@PathVariable String filename) throws MalformedURLException {

        log.debug("fullpath"+file.getFullPath(filename));
        Resource resource = new FileSystemResource(file.getFullPath(filename)+".jpg");

        // 파일을 존재하지않으면 404
        if(!resource.exists()){
          log.debug("404");
        }

        return resource;
      }
}
