package vn.intracom.chuongtrinhdaotao.service;

import vn.intracom.chuongtrinhdaotao.dto.request.RegisterRequest;
import vn.intracom.chuongtrinhdaotao.dto.request.UserUpdateRequest;
import vn.intracom.chuongtrinhdaotao.dto.response.JwtResponse;
import vn.intracom.chuongtrinhdaotao.dto.response.PageResponse;
import vn.intracom.chuongtrinhdaotao.dto.response.UserResponse;


public interface IUserService {
    PageResponse<UserResponse> getAll(String keyword, int page, int size); 
    UserResponse getById(Long id);
    UserResponse getByUsername(String username);
    void register(RegisterRequest request);
    UserResponse create(RegisterRequest request);
    UserResponse update(Long id, UserUpdateRequest request);
    JwtResponse buildJwtResponse(String username, String token);
    void delete(Long id);
    void changePassword(String username, String currentPassword, String newPassword);
}