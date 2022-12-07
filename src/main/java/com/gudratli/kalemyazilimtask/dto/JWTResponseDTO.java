package com.gudratli.kalemyazilimtask.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@ApiModel(value = "JWT Response DTO", description = "DTO that used to get access and refresh tokens.")
public class JWTResponseDTO
{
    @ApiModelProperty(value = "Access token that needed to access APIs.")
    private String accessToken;

    @ApiModelProperty(value = "Refresh token that used to refresh our access token and get new one.")
    private String refreshToken;
}
