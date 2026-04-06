package vn.intracom.chuongtrinhdaotao.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.intracom.chuongtrinhdaotao.exception.BadRequestException;
import vn.intracom.chuongtrinhdaotao.service.IFileUploadService;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class FileUploadServiceImpl implements IFileUploadService {

    @Value("${file.upload-dir:uploads/decuong}")
    private String uploadDir;

    private static final long MAX_SIZE   = 200L * 1024 * 1024; // 200MB
    private static final String MIME_PDF = "application/pdf";

    @Override
    public String upload(MultipartFile file) {
        if (file.isEmpty())
            throw new BadRequestException("File không được để trống.");

        if (!MIME_PDF.equals(file.getContentType()))
            throw new BadRequestException("Chỉ chấp nhận file PDF.");

        if (file.getSize() > MAX_SIZE)
            throw new BadRequestException("File không được vượt quá 200MB.");

        try {
            Path dir = Paths.get(uploadDir);
            Files.createDirectories(dir);

            String original = file.getOriginalFilename()
                    .replaceAll("[^a-zA-Z0-9._-]", "_");
            String fileName = UUID.randomUUID() + "_" + original;
            Path target     = dir.resolve(fileName);

            Files.copy(file.getInputStream(), target,
                    StandardCopyOption.REPLACE_EXISTING);

            return "/api/files/decuong/" + fileName;

        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi lưu file: " + e.getMessage());
        }
    }

    @Override
    public Path load(String fileName) {
        return Paths.get(uploadDir).resolve(fileName).normalize();
    }
}