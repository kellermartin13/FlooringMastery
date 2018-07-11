/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.controller;

import com.sg.flooringmastery.dao.FlooringPersistenceException;
import com.sg.flooringmastery.dao.NoSuchDateException;
import com.sg.flooringmastery.dao.NoSuchOrderException;
import com.sg.flooringmastery.dao.TrainingSaveException;
import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.dto.Product;
import com.sg.flooringmastery.dto.Tax;
import com.sg.flooringmastery.service.FlooringServiceLayer;
import com.sg.flooringmastery.ui.FlooringView;
import com.sg.flooringmastery.ui.InvalidDateException;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Keller Martin
 */
public class FlooringController {

    private final FlooringServiceLayer service;
    private final FlooringView view;

    public FlooringController(FlooringServiceLayer service, FlooringView view) {
        this.service = service;
        this.view = view;
    }

    public void run() {
        boolean keepGoing = true;
        int userMenuChoice = 0;
        try {
            this.loadInitial();
            while (keepGoing) {
                try {
                    userMenuChoice = showMenuOptions();
                    switch (userMenuChoice) {
                        case 1:
                            this.displayOrders();
                            break;
                        case 2:
                            this.addOrder();
                            break;
                        case 3:
                            this.editOrder();
                            break;
                        case 4:
                            this.removeOrder();
                            break;
                        case 5:
                            this.saveCurrentWork();
                            break;
                        case 6:
                            keepGoing = false;
                            this.exitMessage();
                            break;
                    }

                } catch (InvalidDateException e) {
                    view.displayErrorMessage(e.getMessage());
                }
            }
        } catch (FlooringPersistenceException e) {
            view.displayErrorMessage(e.getMessage());
        }
    }

    public int showMenuOptions() {
        return view.showMenuOptions();
    }

    public void displayOrders() throws FlooringPersistenceException,
            InvalidDateException {
        LocalDate date = view.getDate();
        try {
            List<Order> orderList = service.getOrdersByDate(date);
            view.displayAllOrders(orderList);
        } catch (NoSuchDateException e) {
            view.displayErrorMessage(e.getMessage());
        }
    }

    public void addOrder() throws FlooringPersistenceException,
            InvalidDateException {
        List<Product> productList = service.getAllProducts();
        List<Tax> taxList = service.getAllTaxes();
        Order newOrder = view.addOrder(taxList, productList);
        newOrder = service.calculateTotalCost(newOrder);
        view.displayOneOrder(newOrder);
        String userChoice = view.getUserConfirmation("Enter 'Y' to confirm, "
                + "or 'N' to "
                + "discard and return to menu.");
        if (userChoice.equalsIgnoreCase("Y")) {
            service.addNewOrder(newOrder);
            view.displayNewOrderSuccess();
        } else {
            view.displayNewOrderDiscarded();
        }
    }

    public void editOrder() throws FlooringPersistenceException,
            InvalidDateException {
        LocalDate date = view.getDate();
        Integer orderNum = view.getOrderNum();
        List<Product> productList = service.getAllProducts();
        List<Tax> taxList = service.getAllTaxes();
        try {
            Order toBeEdited = service.getOrder(orderNum, date);
            Order updatedOrder = view.editOrder(toBeEdited, productList, 
                    taxList);
            updatedOrder = service.calculateTotalCost(updatedOrder);
            view.displayOneOrder(updatedOrder);
            String userChoice = view.getUserConfirmation("Press 'Y' to confirm "
                    + "update, or press 'N' "
                    + "to keep previous order.");
            if (userChoice.equalsIgnoreCase("Y")) {
                service.editOrder(updatedOrder, toBeEdited);
                view.displayEditSuccess();
            } else {
                view.displayOrderNotEdited();
            }
        } catch (NoSuchOrderException | NoSuchDateException e) {
            view.displayErrorMessage(e.getMessage());
        }

    }

    public void removeOrder() throws FlooringPersistenceException,
            InvalidDateException {
        LocalDate date = view.getDate();
        Integer orderNum = view.getOrderNum();
        try {
            Order toBeDeleted = service.getOrder(orderNum, date);
            view.displayOneOrder(toBeDeleted);
            String userChoice = view.getUserConfirmation("Are you sure you "
                    + "want to delete this order? "
                    + "This cannot be undone. (Y/N)");
            if (userChoice.equalsIgnoreCase("Y")) {
                service.removeOrder(orderNum, date);
                view.displayDeleteSuccess();
            } else {
                view.displayOrderNotDeleted();
            }
        } catch (NoSuchOrderException | NoSuchDateException e) {
            view.displayErrorMessage(e.getMessage());
        }
    }

    public void loadInitial() throws FlooringPersistenceException {
        service.loadInitial();
    }

    public void saveCurrentWork() throws FlooringPersistenceException {
        try {
            service.saveCurrentWork();
            view.displaySaveSuccess();
        } catch (TrainingSaveException e) {
            view.displayErrorMessage(e.getMessage());
        }
    }

    public void exitMessage() throws FlooringPersistenceException {
        String userChoice = view.getUserConfirmation("Would you like to save "
                + "your work before you exit? You will lose any unsaved work. "
                + "(Y/N)");
        if (userChoice.equalsIgnoreCase("Y")) {
            this.saveCurrentWork();
        }
        view.displayExitMessage();
    }
}
