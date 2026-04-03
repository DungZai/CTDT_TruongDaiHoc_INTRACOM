package vn.intracom.chuongtrinhdaotao.dto.response;

import lombok.*;
import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class UserResponse {

    private Long id;
    private String username;
    private String email;
    private String roleName;
    private LocalDate createdAt;
}
