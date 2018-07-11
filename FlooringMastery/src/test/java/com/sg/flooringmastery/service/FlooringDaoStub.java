/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.service;

import com.sg.flooringmastery.dao.FlooringDao;
import com.sg.flooringmastery.dao.FlooringPersistenceException;
import com.sg.flooringmastery.dao.NoSuchDateException;
import com.sg.flooringmastery.dao.NoSuchOrderException;
import com.sg.flooringmastery.dao.TrainingSaveException;
import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.dto.Product;
import com.sg.flooringmastery.dto.Tax;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author Keller Martin
 */
public class FlooringDaoStub implements FlooringDao {
    
    private final Order onlyOrder;
    private final Tax onlyTax;
    private final Product onlyProduct;
    private final Map<Integer, Order> orders = new HashMap<>();
    private final Map<String, Tax> stateTaxes = new HashMap<>();
    private final Map<String, Product> productCosts = new HashMap<>();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddyyyy");
    
    public FlooringDaoStub() {
        Integer orderNum = 1;
        String name = "Keller";
        String state = "OH";
        String product = "Wood";
        LocalDate date = LocalDate.parse(("12122018"), formatter);
        BigDecimal area = new BigDecimal("100");
        onlyOrder = new Order(orderNum, name, state, product, date, area);
        onlyOrder.setCostPerSqFt(new BigDecimal("5.15"));
        onlyOrder.setLaborCostPerSqFt(new BigDecimal("4.75"));
        onlyOrder.setTaxRate(new BigDecimal("6.25"));
        onlyOrder.setMaterialCost(new BigDecimal("515"));
        onlyOrder.setLaborCost(new BigDecimal("475"));
        onlyOrder.setTax(new BigDecimal("61.88"));
        onlyOrder.setTotal(new BigDecimal("1051.88"));
        orders.put(orderNum, onlyOrder);
        
        BigDecimal costPerSqFt = new BigDecimal("5.15");
        BigDecimal laborCostPerSqFt = new BigDecimal("4.75");
        onlyProduct = new Product(product, costPerSqFt, laborCostPerSqFt);
        productCosts.put(product, onlyProduct);
        
        BigDecimal taxRate = new BigDecimal("6.25");
        onlyTax = new Tax(state, taxRate);
        stateTaxes.put(state, onlyTax);
        
    }
    

    @Override
    public List<Order> getAllOrders() {
        return orders.values().stream().collect(Collectors.toList());
        
    }

    @Override
    public Order getOrder(Integer orderNum, LocalDate date) throws 
            NoSuchDateException, NoSuchOrderException {
        return orders.get(orderNum);
    }

    @Override
    public Order addNewOrder(Order order) {
        orders.put(order.getOrderNum(), order);
        return order;
    }

    @Override
    public Order editOrder(Order updatedOrder, Order oldOrder) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Order removeOrder(Integer orderNum, LocalDate date) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Tax> getAllStateTaxes() {
        return stateTaxes.values().stream().collect(Collectors.toList());
    }

    @Override
    public List<Product> getAllProductCosts() {
        return productCosts.values().stream().collect(Collectors.toList());
    }

    @Override
    public void saveCurrentWork() throws FlooringPersistenceException, TrainingSaveException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void loadInitial() throws FlooringPersistenceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Tax getStateTaxes(String state) {
        return stateTaxes.get(state);
    }

    @Override
    public Tax addNewStateTax(Tax stateTax) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Tax editStateTax(Tax oldStateTax, Tax newStateTax) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Tax removeStateTax(String state) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Product getProductCosts(String product) {
        return productCosts.get(product);
    }

    @Override
    public Product addNewProductCosts(Product productCosts) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Product editProductCosts(Product oldProductCosts, Product newProductCosts) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Product removeProductCosts(String productType) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
