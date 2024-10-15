package com.OrderMate.service;

import com.OrderMate.dto.ShoppingCartDTO;
import com.OrderMate.entity.ShoppingCart;

import java.util.List;

/**
 * ClassName: ShoppingCartService
 * Package: com.OrderMate.service
 * Description:
 *
 * @Author Gush
 * @Create 2024-03-02 16:34
 */
public interface ShoppingCartService {


    void addShoppingCart(ShoppingCartDTO shoppingCartDTO);


    List<ShoppingCart> showShoppingCart();

    void cleanShoppingCart();

    void subShoppingCart(ShoppingCartDTO shoppingCartDTO);
}
