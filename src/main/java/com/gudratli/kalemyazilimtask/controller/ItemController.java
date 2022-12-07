package com.gudratli.kalemyazilimtask.controller;

import com.gudratli.kalemyazilimtask.dto.ItemCreateDTO;
import com.gudratli.kalemyazilimtask.dto.ItemResponseDTO;
import com.gudratli.kalemyazilimtask.dto.ResponseDTO;
import com.gudratli.kalemyazilimtask.entity.Item;
import com.gudratli.kalemyazilimtask.exception.DuplicateException;
import com.gudratli.kalemyazilimtask.service.ItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/item")
@Api("Item Controller")
public class ItemController
{
    private final ItemService itemService;
    private final ModelMapper modelMapper;

    @GetMapping("/getAllItems")
    @ApiOperation(value = "Get all", notes = "Get all items stored in database.")
    public ResponseEntity<ResponseDTO<List<ItemResponseDTO>>> getAll ()
    {
        List<Item> items = itemService.getAll();

        ResponseDTO<List<ItemResponseDTO>> responseDTO = new ResponseDTO<>();
        responseDTO.setObject(getResponseWithList(items));
        responseDTO.setSuccessMessage("Successfully fetched.");

        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/getItemById/{id}")
    @ApiOperation(value = "Get item by Id", notes = "Get specific item with Id.")
    public ResponseEntity<ResponseDTO<ItemResponseDTO>> getItemById (@PathVariable
    @ApiParam(name = "ID", value = "ID of item", required = true) Long id)
    {
        Item item = itemService.getItemById(id);

        ResponseDTO<ItemResponseDTO> responseDTO = new ResponseDTO<>();

        if (item != null)
        {
            responseDTO.setObject(modelMapper.map(item, ItemResponseDTO.class));
            responseDTO.setSuccessMessage("Item successfully fetched.");
        } else
        {
            responseDTO.setErrorCode(404);
            responseDTO.setErrorMessage("There is not any item with this id.");
        }

        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/getItemByBarcode/{barcode}")
    @ApiOperation(value = "Get item by barcode", notes = "Get specific item with barcode.")
    public ResponseEntity<ResponseDTO<ItemResponseDTO>> getItemByBarcode (@PathVariable
    @ApiParam(name = "Barcode", value = "Barcode of item", required = true) String barcode)
    {
        Item item = itemService.getItemByBarcode(barcode);

        ResponseDTO<ItemResponseDTO> responseDTO = new ResponseDTO<>();
        if (item != null)
        {
            responseDTO.setObject(modelMapper.map(item, ItemResponseDTO.class));
            responseDTO.setSuccessMessage("Item successfully fetched by barcode.");
        } else
        {
            responseDTO.setErrorCode(404);
            responseDTO.setErrorMessage("There is not any item with this barcode.");
        }

        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/createItem")
    @ApiOperation(value = "Create item", notes = "Create new item.")
    public ResponseEntity<ResponseDTO<ItemResponseDTO>> createItem (@RequestBody
    @ApiParam(name = "Item create DTO", value = "DTO for creating new item",
            required = true) ItemCreateDTO itemCreateDTO)
    {
        Item item = modelMapper.map(itemCreateDTO, Item.class);
        ResponseDTO<ItemResponseDTO> responseDTO = new ResponseDTO<>();

        try
        {
            item.setCreatedAt(new Date());
            item = itemService.addItem(item);
        } catch (DuplicateException e)
        {
            responseDTO.setErrorCode(304);
            responseDTO.setErrorMessage(e.getMessage());
            return ResponseEntity.ok(responseDTO);
        }

        responseDTO.setObject(modelMapper.map(item, ItemResponseDTO.class));
        responseDTO.setSuccessMessage("Successfully added new item.");

        return ResponseEntity.ok(responseDTO);
    }

    private List<ItemResponseDTO> getResponseWithList (List<Item> items)
    {
        List<ItemResponseDTO> itemResponseDTOs = new ArrayList<>();

        items.forEach(item -> itemResponseDTOs.add(modelMapper.map(item, ItemResponseDTO.class)));

        return itemResponseDTOs;
    }
}
