package com.gudratli.kalemyazilimtask.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "Response DTO.", description = "Common DTO for all responses.")
public class ResponseDTO<T>
{
    @ApiModelProperty(value = "If any error occurs, code of that error.")
    private Integer errorCode;

    @ApiModelProperty(value = "If any error occurs, then information about that error")
    private String errorMessage;

    @ApiModelProperty(value = "If no error occur, then information about success.")
    private String successMessage;

    @ApiModelProperty(value = "Object that sends back to user.")
    private T object;
}
