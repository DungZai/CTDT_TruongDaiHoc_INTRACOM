package vn.intracom.chuongtrinhdaotao.service;

import vn.intracom.chuongtrinhdaotao.dto.request.RegisterRequest;
import vn.intracom.chuongtrinhdaotao.dto.response.JwtResponse;
import vn.intracom.chuongtrinhdaotao.dto.response.UserResponse;

import java.util.List;

public interface IUserService {
    List<UserResponse> getAll();
    UserResponse getById(Long id);
    UserResponse getByUsername(String username);
    void register(RegisterRequest request);
    JwtResponse buildJwtResponse(String username, String token);
    void delete(Long id);
}