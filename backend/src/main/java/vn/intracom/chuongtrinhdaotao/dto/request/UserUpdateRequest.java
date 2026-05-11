package vn.intracom.chuongtrinhdaotao.dto.request;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class UserUpdateRequest {
    private String username;
    private String email;
    private Long   roleId;
}