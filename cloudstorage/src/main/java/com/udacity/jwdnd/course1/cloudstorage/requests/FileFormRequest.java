package com.udacity.jwdnd.course1.cloudstorage.requests;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class FileFormRequest {
    private MultipartFile file;
}
