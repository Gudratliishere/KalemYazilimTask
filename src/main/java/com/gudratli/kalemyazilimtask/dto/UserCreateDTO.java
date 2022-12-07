package com.gudratli.kalemyazilimtask.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "User Create DTO", description = "DTO that used to register.")
public class UserCreateDTO
{
    @ApiModelProperty(required = true)
    private String name;

    @ApiModelProperty(required = true)
    private String surname;

    @ApiModelProperty(required = true)
    private String email;

    @ApiModelProperty(required = true)
    private String username;

    @ApiModelProperty(required = true)
    private String password;
}
