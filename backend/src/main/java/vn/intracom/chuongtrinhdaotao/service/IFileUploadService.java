package vn.intracom.chuongtrinhdaotao.service;

import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface IFileUploadService {
    String upload(MultipartFile file);
    Path load(String fileName);
}