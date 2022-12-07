package com.gudratli.kalemyazilimtask.dto;

import com.gudratli.kalemyazilimtask.enums.RoleType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "User Response DTO", description = "DTO that used to give information about User.")
public class UserResponseDTO
{
    @ApiModelProperty
    private Long id;

    @ApiModelProperty
    private Date createdAt;

    @ApiModelProperty
    private String name;

    @ApiModelProperty
    private String surname;

    @ApiModelProperty
    private String email;

    @ApiModelProperty
    private String username;

    @ApiModelProperty
    private RoleType role;
}
