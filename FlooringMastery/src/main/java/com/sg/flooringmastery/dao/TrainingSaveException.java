/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.dao;

/**
 *
 * @author Keller Martin
 */
public class TrainingSaveException extends FlooringPersistenceException {

    public TrainingSaveException(String message) {
        super(message);
    }

    public TrainingSaveException(String message, Throwable cause) {
        super(message, cause);
    }

}
