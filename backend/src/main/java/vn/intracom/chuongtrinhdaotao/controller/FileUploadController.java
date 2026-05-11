package vn.intracom.chuongtrinhdaotao.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.intracom.chuongtrinhdaotao.dto.response.ApiResponse;
import vn.intracom.chuongtrinhdaotao.service.IFileUploadService;

import java.nio.file.Path;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
@Tag(name = "File Upload", description = "Upload và xem tài liệu PDF")
public class FileUploadController {

    private final IFileUploadService fileUploadService;

    @Operation(summary = "Upload file PDF tài liệu đề cương")
    @PostMapping("/upload")
    @PreAuthorize("hasAnyAuthority('ADMIN','GIANG_VIEN')")
    public ResponseEntity<ApiResponse<String>> upload(
            @RequestParam("file") MultipartFile file) {
        String url = fileUploadService.upload(file);
        return ResponseEntity.ok(ApiResponse.success("Upload thành công", url));
    }

    @Operation(summary = "Xem/tải file PDF")
    @GetMapping("/decuong/{fileName}")
    public ResponseEntity<Resource> serveFile(@PathVariable String fileName) {
        Path filePath = fileUploadService.load(fileName);
        try {
            Resource resource = new UrlResource(filePath.toUri());
            if (!resource.exists())
                return ResponseEntity.notFound().build();
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + fileName + "\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}