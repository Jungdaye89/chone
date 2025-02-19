package com.chone.server.domains.user.dto.request;

import com.chone.server.domains.user.domain.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequestDto {
    private String username;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

    @NotBlank(message = "이메일 주소를 입력해주세요")
    @Email(message = "올바른 형식의 이메일 주소를 입력해주세요.")
    private String email;

    private Role role;
}
