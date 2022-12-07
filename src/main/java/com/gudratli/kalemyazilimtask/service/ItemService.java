package com.gudratli.kalemyazilimtask.service;

import com.gudratli.kalemyazilimtask.entity.Item;
import com.gudratli.kalemyazilimtask.exception.DuplicateException;

import java.util.List;

public interface ItemService
{
    Item addItem (Item item) throws DuplicateException;

    Item getItemById (Long id);

    Item getItemByBarcode (String barcode);

    Item getItemByCode (String code);

    List<Item> getAll ();
}
