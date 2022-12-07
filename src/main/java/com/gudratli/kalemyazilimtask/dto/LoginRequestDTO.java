package com.gudratli.kalemyazilimtask.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "Login Request DTO", description = "DTO that used to login.")
public class LoginRequestDTO
{
    @ApiModelProperty(required = true)
    private String username;

    @ApiModelProperty(required = true)
    private String password;
}
