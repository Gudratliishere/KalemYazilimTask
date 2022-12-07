package com.gudratli.kalemyazilimtask.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "Item Response DTO", description = "DTO that used to show information about item.")
public class ItemResponseDTO
{
    @ApiModelProperty(value = "ID of the item.")
    private Long id;

    @ApiModelProperty(value = "Created date of the item")
    private Date createdAt;

    @ApiModelProperty(value = "Code of the item.")
    private String code;

    @ApiModelProperty(value = "Name of the item.")
    private String name;

    @ApiModelProperty(value = "Barcode of the item.")
    private String barcode;

    @ApiModelProperty(value = "Price of the item")
    private Double price;
}
