/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.service;

import com.sg.flooringmastery.dao.FlooringPersistenceException;
import com.sg.flooringmastery.dao.NoSuchDateException;
import com.sg.flooringmastery.dao.NoSuchOrderException;
import com.sg.flooringmastery.dao.TrainingSaveException;
import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.dto.Product;
import com.sg.flooringmastery.dto.Tax;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Keller Martin
 */
public interface FlooringServiceLayer {
    
    Order calculateTotalCost(Order order)
            throws FlooringPersistenceException;
    
    List<Order> getOrdersByDate(LocalDate date)
            throws FlooringPersistenceException, NoSuchDateException;
    
    void saveCurrentWork()
            throws FlooringPersistenceException, TrainingSaveException;

    List<Order> getAllOrders()
            throws FlooringPersistenceException ;
    
    Order getOrder(Integer orderNum, LocalDate date)
            throws FlooringPersistenceException, NoSuchDateException, 
            NoSuchOrderException;
    
    Order addNewOrder(Order order)
            throws FlooringPersistenceException;
    
    Order editOrder(Order updatedOrder, Order oldOrder)
            throws FlooringPersistenceException;
    
    Order removeOrder(Integer orderNum, LocalDate date)
            throws FlooringPersistenceException;
    
    void loadInitial() throws FlooringPersistenceException;
    
    List<Product> getAllProducts();
    
    List<Tax> getAllTaxes();
    
    
    
    
    
    
}
