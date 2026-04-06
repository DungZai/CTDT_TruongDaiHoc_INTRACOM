package vn.intracom.chuongtrinhdaotao.dto.request;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class UserUpdateRequest {
    private String email;
    private Long   roleId;
}