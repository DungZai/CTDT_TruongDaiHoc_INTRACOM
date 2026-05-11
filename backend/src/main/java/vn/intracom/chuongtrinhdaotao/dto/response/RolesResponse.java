package vn.intracom.chuongtrinhdaotao.dto.response;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class RolesResponse {

    private Long id;
    private String roleName;
}