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
import com.sg.flooringmastery.dto.Product;
import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.dto.Tax;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 *
 * @author Keller Martin
 */
public class FlooringServiceLayerImpl implements FlooringServiceLayer {

    private final FlooringDao dao;

    public FlooringServiceLayerImpl(FlooringDao dao) {
        this.dao = dao;
    }

    @Override
    public Order calculateTotalCost(Order order)
            throws FlooringPersistenceException {
        
        Tax stateTax = dao.getStateTaxes(order.getState());
        Product productCosts = dao.getProductCosts(order.getProductType());
        BigDecimal materialCost = (productCosts.getCostPerSqFt()
                .multiply(order.getArea()));
        BigDecimal laborCost = (productCosts.getLaborCostPerSqFt()
                .multiply(order.getArea()));
        BigDecimal totalNoTax = laborCost.add(materialCost);
        BigDecimal tax = ((stateTax.getTaxRate().
                divide(new BigDecimal("100.00"))).multiply(totalNoTax));
        BigDecimal taxRounded = tax.setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = totalNoTax.add(taxRounded);
        order.setTaxRate(stateTax.getTaxRate());
        order.setCostPerSqFt(productCosts.getCostPerSqFt());
        order.setLaborCostPerSqFt(productCosts.getLaborCostPerSqFt());
        order.setMaterialCost(materialCost);
        order.setLaborCost(laborCost);
        order.setTax(taxRounded);
        order.setTotal(total);
        return order;
    }

    @Override
    public List<Order> getAllOrders() throws FlooringPersistenceException {
        return dao.getAllOrders();
    }

    @Override
    public List<Order> getOrdersByDate(LocalDate date)
            throws FlooringPersistenceException, NoSuchDateException {
        List<Order> allOrders = dao.getAllOrders();
        List<Order> ordersByDate = allOrders.stream().filter(order -> 
                order.getDateNeeded().equals(date))
                .collect(Collectors.toList());
        if (ordersByDate.isEmpty()) {
            throw new NoSuchDateException("-ERROR: No orders for the --given "
                    + "date are in the system. -Please select a new "
                    + "date --or add a new order.");
        }
        return ordersByDate;
    }

    @Override
    public Order getOrder(Integer orderNum, LocalDate date)
            throws FlooringPersistenceException, NoSuchDateException, 
            NoSuchOrderException {
        return dao.getOrder(orderNum, date);
    }

    @Override
    public Order addNewOrder(Order order)
            throws FlooringPersistenceException {
        List<Order> allOrders = dao.getAllOrders();
        Integer orderNum = allOrders.size() + 1;
        order.setOrderNum(orderNum);
        for (Order currentOrder : allOrders) {
            if (Objects.equals(orderNum, currentOrder.getOrderNum())) {
                orderNum++;
                order.setOrderNum(orderNum);
            }
        }
        dao.addNewOrder(order);
        return order;
    }

    @Override
    public Order editOrder(Order updatedOrder, Order oldOrder)
            throws FlooringPersistenceException {
        return dao.editOrder(updatedOrder, oldOrder);
    }

    @Override
    public Order removeOrder(Integer orderNum, LocalDate date)
            throws FlooringPersistenceException {
        return dao.removeOrder(orderNum, date);
    }

    @Override
    public void saveCurrentWork()
            throws FlooringPersistenceException {
        dao.saveCurrentWork();
    }

    @Override
    public void loadInitial() throws FlooringPersistenceException {
        dao.loadInitial();
    }

    @Override
    public List<Product> getAllProducts() {
        return dao.getAllProductCosts();
    }

    @Override
    public List<Tax> getAllTaxes() {
        return dao.getAllStateTaxes();
    }

}
