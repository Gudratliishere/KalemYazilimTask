package com.gudratli.kalemyazilimtask.repository;

import com.gudratli.kalemyazilimtask.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long>
{
    Item findByBarcode (String barcode);

    Item findByCode (String code);
}