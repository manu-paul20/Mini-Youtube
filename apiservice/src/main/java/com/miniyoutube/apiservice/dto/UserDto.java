package com.miniyoutube.apiservice.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    @NotEmpty
    private String userName;

    @NotEmpty
    private String password;
}
