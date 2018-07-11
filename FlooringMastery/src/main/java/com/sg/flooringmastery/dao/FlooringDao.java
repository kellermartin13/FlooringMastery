/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.dto.Product;
import com.sg.flooringmastery.dto.Tax;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Keller Martin
 */
public interface FlooringDao {
    
    List<Order> getAllOrders();
    
    Order getOrder(Integer orderNum, LocalDate date)
            throws NoSuchDateException, NoSuchOrderException;
    
    Order addNewOrder(Order order);
    
    Order editOrder(Order updatedOrder, Order oldOrder);
    
    Order removeOrder(Integer orderNum, LocalDate date);
    
    List<Tax> getAllStateTaxes();
    
    Tax getStateTaxes(String state);
    
    Tax addNewStateTax(Tax stateTax);
    
    Tax editStateTax(Tax oldStateTax, 
            Tax newStateTax);
    
    Tax removeStateTax(String state);
    
    List<Product> getAllProductCosts();
    
    Product getProductCosts(String product);
    
    Product addNewProductCosts(Product productCosts);
    
    Product editProductCosts(Product oldProductCosts,
            Product newProductCosts);
    
    Product removeProductCosts(String productType);
    
    void saveCurrentWork() 
            throws FlooringPersistenceException, TrainingSaveException;
    
    void loadInitial()
            throws FlooringPersistenceException;
}
