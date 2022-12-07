package com.gudratli.kalemyazilimtask.service.impl;

import com.gudratli.kalemyazilimtask.entity.Item;
import com.gudratli.kalemyazilimtask.exception.DuplicateException;
import com.gudratli.kalemyazilimtask.repository.ItemRepository;
import com.gudratli.kalemyazilimtask.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService
{
    private final ItemRepository itemRepository;

    @Override
    public Item addItem (Item item) throws DuplicateException
    {
        checkForDuplicate(item);

        return itemRepository.save(item);
    }

    @Override
    public Item getItemById (Long id)
    {
        return itemRepository.findById(id).orElse(null);
    }

    @Override
    public Item getItemByBarcode (String barcode)
    {
        return itemRepository.findByBarcode(barcode);
    }

    @Override
    public Item getItemByCode (String code)
    {
        return itemRepository.findByCode(code);
    }

    @Override
    public List<Item> getAll ()
    {
        return itemRepository.findAll();
    }

    private void checkForDuplicate (Item item) throws DuplicateException
    {
        if (getItemByBarcode(item.getBarcode()) != null)
            throw new DuplicateException("Barcode is already taken.");
        if (getItemByCode(item.getCode()) != null)
            throw new DuplicateException("Code is already taken.");
    }
}
