package com.gudratli.kalemyazilimtask.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "Item Create DTO", description = "DTO that used to create new item.")
public class ItemCreateDTO
{
    @ApiModelProperty(value = "Code of the item.", example = "2088", required = true)
    private String code;

    @ApiModelProperty(value = "Name of the item.", example = "Winston XS", required = true)
    private String name;

    @ApiModelProperty(value = "Barcode of the item.", example = "6948910007372", required = true)
    private String barcode;

    @ApiModelProperty(value = "Price of the item", example = "0.59", required = true)
    private Double price;
}
