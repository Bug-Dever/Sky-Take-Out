package com.OrderMate.service.impl;

import com.OrderMate.context.BaseContext;
import com.OrderMate.dto.ShoppingCartDTO;
import com.OrderMate.entity.Dish;
import com.OrderMate.entity.Setmeal;
import com.OrderMate.entity.ShoppingCart;
import com.OrderMate.mapper.DishMapper;
import com.OrderMate.mapper.SetmealMapper;
import com.OrderMate.mapper.ShoppingCartMapper;
import com.OrderMate.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * ClassName: ShoppingCartServiceImpl
 * Package: com.OrderMate.service.impl
 * Description:
 *
 * @Author Gush
 * @Create 2024-03-02 16:34
 */
@Service
@Slf4j
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;
    /*
    * 添加购物车
    * */
    public void addShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        // 判断当前加入到购物车中的商品是否已经存在了
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);

        // 如果已经存在了，只需要将数量加一
        if(list != null && list.size() > 0) {
            ShoppingCart cart = list.get(0);
            cart.setNumber(cart.getNumber() + 1);
            shoppingCartMapper.updateNumberById(cart);
        } else {
            // 如果不存在，需要插入一条购物车数据

            if(shoppingCart.getDishId() != null) {
                // 添加到购物车的是菜品
                Dish dish = dishMapper.getById(shoppingCart.getDishId());
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());
                shoppingCart.setNumber(1);
                shoppingCart.setCreateTime(LocalDateTime.now());
            } else {
                // 添加到购物车的是套餐
                Setmeal setmeal = setmealMapper.getById(shoppingCart.getSetmealId());
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setAmount(setmeal.getPrice());
                shoppingCart.setNumber(1);
                shoppingCart.setCreateTime(LocalDateTime.now());
            }

            shoppingCartMapper.insert(shoppingCart);
        }

    }

    /*
    * 查看购物车
    * */
    public List<ShoppingCart> showShoppingCart() {
        ShoppingCart cart = new ShoppingCart();
        cart.setUserId(BaseContext.getCurrentId());
        List<ShoppingCart> list = shoppingCartMapper.list(cart);
        return list;
    }

    /*
    * 清空购物车
    * */
    public void cleanShoppingCart() {
        Long userID = BaseContext.getCurrentId();
        shoppingCartMapper.deleteByUserId(userID);
    }

    /*
    * 删除购物车中一个商品
    * */
    public void subShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart cart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,cart);
        cart.setUserId(BaseContext.getCurrentId());
        List<ShoppingCart> list = shoppingCartMapper.list(cart);
        ShoppingCart shoppingCart = list.get(0);
        if(shoppingCart.getNumber() > 1) {
            // 商品大于1件，数量减1即可
            shoppingCart.setNumber(shoppingCart.getNumber() - 1);
            shoppingCartMapper.updateNumberById(shoppingCart);
        } else {
            // 直接删除商品
            shoppingCartMapper.deleteById(shoppingCart.getId());
        }
    }
}
