package vn.intracom.chuongtrinhdaotao.service;

import vn.intracom.chuongtrinhdaotao.dto.response.RolesResponse;

import java.util.List;

public interface IRolesService {
    List<RolesResponse> getAll();
    RolesResponse getById(Long id);
    RolesResponse create(String roleName);
    void delete(Long id);
}