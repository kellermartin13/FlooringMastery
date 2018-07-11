/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.dto.Product;
import com.sg.flooringmastery.dto.Tax;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @author Keller Martin
 */
public class FlooringDaoImpl implements FlooringDao {

    private final Map<String, List<Order>> ordersByDate = new HashMap<>();
    private final Map<String, Tax> stateTaxes = new HashMap<>();
    private final Map<String, Product> productCosts = new HashMap<>();
    private static final String TAXES_FILE = "taxes.txt";
    private static final String PRODUCT_FILE = "products.txt";
    private static final String TRAINING_FILE = "trainingToggle.txt";
    private static final String DELIMITER = ",";
    private boolean isTraining = false;
    private Scanner sc;
    private final DateTimeFormatter formatter
            = DateTimeFormatter.ofPattern("MMddyyyy");

    @Override
    public List<Order> getAllOrders() {
        List<Order> all = new ArrayList<>();
        ordersByDate.values().forEach((list) -> {
            list.forEach((currentOrder) -> {
                all.add(currentOrder);
            });
        });
        return all;
    }

    @Override
    public Order getOrder(Integer orderNum, LocalDate date)
            throws NoSuchDateException, NoSuchOrderException {
        String stringDate = date.format(formatter);
        List<Order> list = ordersByDate.get(stringDate);
        if (list == null) {
            throw new NoSuchDateException("-ERROR: No orders for the --given "
                    + "date are in the system. -Please select a new "
                    + "date --or add a new order.");
        }
        Order order = null;
        for (Order currentOrder : list) {
            if (Objects.equals(currentOrder.getOrderNum(), orderNum)) {
                order = currentOrder;
            }
        }
        if (order == null) {
            throw new NoSuchOrderException("ERROR: Your order number is not "
                    + "scheduled for given date. Please make sure your order "
                    + "number is associated with the given date.");
        }
        return order;
    }

    @Override
    public Order addNewOrder(Order order) {
        String formattedDate = order.getDateNeeded().format(formatter);
        if (ordersByDate.get(formattedDate) == null) {
            List<Order> newList = new ArrayList<>();
            newList.add(order);
            ordersByDate.put(formattedDate, newList);
        } else {
            List<Order> oldOrderList = ordersByDate.get(formattedDate);
            List<Order> newOrderList = new ArrayList<>(oldOrderList);
            newOrderList.add(order);
            ordersByDate.replace(formattedDate, oldOrderList, newOrderList);
        }
        return order;
    }

    @Override
    public Order editOrder(Order updatedOrder, Order oldOrder) {
        String oldDate = oldOrder.getDateNeeded().format(formatter);
        String newDate = updatedOrder.getDateNeeded().format(formatter);
        List<Order> orderList = new ArrayList<>(ordersByDate.get(oldDate));
        List<Order> newList = new ArrayList<>();

        //OLD ORDER REMOVED FROM LIST
        orderList.remove(oldOrder);
        //KEY & VALUES ASSOCIATED WITH OLD ORDER REMOVED FROM MAP
        this.ordersByDate.remove(oldDate);

        //IF DATE WAS NOT CHANGED BY USER
        //ADDS UPDATED ORDER TO LIST
        //ADDS LIST TO MAP
        if (oldDate.equals(newDate)) {
            orderList.add(updatedOrder);
            this.ordersByDate.put(newDate, orderList);

            //IF DATE WAS CHANGED BY USER
            //OLD DATE KEY PUT BACK IN MAP WITHOUT OLD ORDER
            //NEW DATE KEY ADD WITH UPDATED ORDER
        } else {
            this.ordersByDate.put(oldDate, orderList);
            newList.add(updatedOrder);
            this.ordersByDate.put(newDate, newList);
        }
        return updatedOrder;
    }

    @Override
    public Order removeOrder(Integer orderNum, LocalDate date) {
        String stringDate = date.format(formatter);
        Order removedOrder = null;
        List<Order> orderList = new ArrayList<>(ordersByDate.get(stringDate));
        if (orderList.isEmpty()) {
            //throw NoSuchDate exception here
        } else {
            for (Order currentOrder : orderList) {
                if (Objects.equals(currentOrder.getOrderNum(), orderNum)) {
                    removedOrder = currentOrder;
                    orderList.remove(currentOrder);
                    ordersByDate.remove(stringDate);
                    ordersByDate.put(stringDate, orderList);
                    break;
                }

            }
        }
        return removedOrder;
    }

    @Override
    public Tax getStateTaxes(String state) {
        return stateTaxes.get(state);
    }

    @Override
    public Product getProductCosts(String product) {
        return productCosts.get(product);
    }

    @Override
    public void saveCurrentWork()
            throws FlooringPersistenceException, TrainingSaveException {
        if (isTraining) {
            throw new TrainingSaveException("ERROR: You are currently in "
                    + "TRAINING mode. Please close and switch to PROD in "
                    + "order to persist your data.");
        } else {
            this.writeOrders();
        }
    }

    @Override
    public void loadInitial()
            throws FlooringPersistenceException {
        this.loadOrders();
        this.loadCosts();
        this.loadStateTaxes();
        this.loadTrainingToggle();
    }

    private List<Order> getOrdersByDate(LocalDate date) {
        List<Order> allOrders = this.getAllOrders();
        return allOrders.stream().filter(order
                -> order.getDateNeeded().equals(date))
                .collect(Collectors.toList());
    }

    //OrderNum, CustName, State, TaxRate, ProductType, Area, CostPerSqFt,
    //LaborCostPerSqFt, MaterialCost, LaborCost, Tax, Total
    private void writeOrders() throws FlooringPersistenceException {
        PrintWriter out;
        Set<String> keySet = ordersByDate.keySet();
        for (String currentDate : keySet) {
            if (currentDate == null) {

            } else {
                LocalDate ld = LocalDate.parse(currentDate, formatter);
                List<Order> ordersByDate = this.getOrdersByDate(ld);

                String formatted = ld.format(formatter);
                try {
                    out = new PrintWriter(new FileWriter("Orders\\Orders_"
                            + (formatted) + ".txt"));
                } catch (IOException e) {
                    throw new FlooringPersistenceException("ERROR: Could not save "
                            + "order data.", e);
                }
                for (Order currentOrder : ordersByDate) {
                    out.println(currentOrder.getOrderNum() + DELIMITER
                            + currentOrder.getCustomerName().replace(",", "~") + DELIMITER
                            + currentOrder.getState() + DELIMITER
                            + currentOrder.getTaxRate() + DELIMITER
                            + currentOrder.getProductType() + DELIMITER
                            + currentOrder.getArea() + DELIMITER
                            + currentOrder.getCostPerSqFt() + DELIMITER
                            + currentOrder.getLaborCostPerSqFt() + DELIMITER
                            + currentOrder.getMaterialCost() + DELIMITER
                            + currentOrder.getLaborCost() + DELIMITER
                            + currentOrder.getTax() + DELIMITER
                            + currentOrder.getTotal());
                    out.flush();
                }
                out.close();
            }
        }
    }

    private void loadOrders()
            throws FlooringPersistenceException {
        File folder = new File("Orders_Medium");
        File[] listOfFiles = folder.listFiles();

        String dateOnly = null;
        for (File currentFile : listOfFiles) {
            if (currentFile.length() == 0) {
                currentFile.delete();
            } else {
                List<Order> currentList = new ArrayList<>();
                if (currentFile.getName().endsWith(".txt")) {
                    try {
                        sc = new Scanner(new BufferedReader(new FileReader(currentFile)));
                    } catch (FileNotFoundException e) {
                        throw new FlooringPersistenceException("ERROR: "
                                + "Flooring data could not be read.", e);
                    }
                    String currentLine;
                    String[] currentToken;
                    while (sc.hasNextLine()) {
                        currentLine = sc.nextLine();
                        currentToken = currentLine.split(DELIMITER);
                        Order currentOrder = new Order();
                        currentOrder.setOrderNum(Integer.parseInt(currentToken[0]));
                        currentOrder.setCustomerName(currentToken[1].replace("~", ","));
                        currentOrder.setState(currentToken[2]);
                        currentOrder.setTaxRate(new BigDecimal(currentToken[3]));
                        currentOrder.setProductType(currentToken[4]);
                        currentOrder.setArea(new BigDecimal(currentToken[5]));
                        currentOrder.setCostPerSqFt(new BigDecimal(currentToken[6]));
                        currentOrder.setLaborCostPerSqFt(new BigDecimal(currentToken[7]));
                        currentOrder.setMaterialCost(new BigDecimal(currentToken[8]));
                        currentOrder.setLaborCost(new BigDecimal(currentToken[9]));
                        currentOrder.setTax(new BigDecimal(currentToken[10]));
                        currentOrder.setTotal(new BigDecimal(currentToken[11]));

                        String[] fullFileName = currentFile.getName().split("_");
                        String dateTxt = fullFileName[1];
                        dateOnly = dateTxt.replace(".txt", "");
                        LocalDate date = LocalDate.parse(dateOnly, formatter);
                        currentOrder.setDateNeeded(date);

                        currentList.add(currentOrder);
                    }
                }
                ordersByDate.put(dateOnly, currentList);
            }
        }
    }

    //State, TaxRate
    private void loadStateTaxes() throws FlooringPersistenceException {
        Scanner sc;
        try {
            sc = new Scanner(new BufferedReader(new FileReader(TAXES_FILE)));
        } catch (FileNotFoundException e) {
            throw new FlooringPersistenceException("ERROR: Could not load "
                    + "flooring data into memory.", e);
        }
        String currentLine;
        String[] currentToken;
        while (sc.hasNextLine()) {
            currentLine = sc.nextLine();
            currentToken = currentLine.split(DELIMITER);
            Tax tax = new Tax(currentToken[0],
                    new BigDecimal(currentToken[1]));
            stateTaxes.put(tax.getStateName(), tax);
        }
        sc.close();
    }

    //ProductType, CostPerSqFt, LaborCostPerSqFt
    private void loadCosts() throws FlooringPersistenceException {
        Scanner sc;
        try {
            sc = new Scanner(new BufferedReader(new FileReader(PRODUCT_FILE)));
        } catch (FileNotFoundException e) {
            throw new FlooringPersistenceException("ERROR: Could not load "
                    + "flooring data into memory.", e);
        }
        String currentLine;
        String[] currentToken;
        while (sc.hasNextLine()) {
            currentLine = sc.nextLine();
            currentToken = currentLine.split(DELIMITER);
            Product productCost = new Product(currentToken[0],
                    new BigDecimal(currentToken[1]), new BigDecimal(currentToken[2]));
            productCosts.put(productCost.getProductType(), productCost);
        }
        sc.close();
    }

    private void loadTrainingToggle() throws FlooringPersistenceException {
        Scanner sc;
        try {
            sc = new Scanner(new BufferedReader(new FileReader(TRAINING_FILE)));
        } catch (FileNotFoundException e) {
            throw new FlooringPersistenceException("ERROR: Could not load "
                    + "training data into memory.", e);
        }
        String currentLine;
        while (sc.hasNextLine()) {
            currentLine = sc.nextLine();
            if (currentLine.equalsIgnoreCase("TRAINING")) {
                isTraining = true;
            } else if (currentLine.equalsIgnoreCase("PROD")) {
                isTraining = false;
            }
        }
        sc.close();
    }

    @Override
    public List<Tax> getAllStateTaxes() {
        return new ArrayList<>(this.stateTaxes.values());
    }

    @Override
    public Tax addNewStateTax(Tax stateTax) {
        this.stateTaxes.put(stateTax.getStateName(), stateTax);
        return stateTax;
    }

    @Override
    public Tax editStateTax(Tax oldStateTax,
            Tax newStateTax) {

        if (this.stateTaxes.containsKey(oldStateTax.getStateName())) {
            stateTaxes.remove(oldStateTax.getStateName());
            stateTaxes.put(newStateTax.getStateName(), newStateTax);
        }
        return newStateTax;
    }

    @Override
    public Tax removeStateTax(String state) {
        Tax toBeRemoved = this.stateTaxes.get(state);
        this.stateTaxes.remove(state);
        return toBeRemoved;
    }

    @Override
    public List<Product> getAllProductCosts() {
        return new ArrayList<>(this.productCosts.values());
    }

    @Override
    public Product addNewProductCosts(Product productCosts) {
        this.productCosts.put(productCosts.getProductType(), productCosts);
        return productCosts;
    }

    @Override
    public Product editProductCosts(Product oldProductCosts,
            Product newProductCosts) {
        if (this.productCosts.containsKey(oldProductCosts.getProductType())) {
            this.productCosts.remove(oldProductCosts.getProductType());
            this.productCosts.put(newProductCosts.getProductType(),
                    newProductCosts);
        }
        return newProductCosts;
    }

    @Override
    public Product removeProductCosts(String productType) {
        Product toBeRemoved = this.productCosts.get(productType);
        this.productCosts.remove(productType);
        return toBeRemoved;
    }

}
