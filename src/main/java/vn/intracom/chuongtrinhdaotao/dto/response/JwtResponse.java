package vn.intracom.chuongtrinhdaotao.dto.response;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class JwtResponse {

    private String token;

    @Builder.Default  // ✅
    private String type = "Bearer";

    private Long id;
    private String username;
    private String email;
    private String role;
}