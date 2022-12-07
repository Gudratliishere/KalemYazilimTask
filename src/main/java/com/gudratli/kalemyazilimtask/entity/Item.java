package com.gudratli.kalemyazilimtask.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "items")
@Data
@EqualsAndHashCode(callSuper = true)
public class Item extends BaseEntity
{
    private String code;
    private String name;
    private String barcode;
    private Double price;
}
