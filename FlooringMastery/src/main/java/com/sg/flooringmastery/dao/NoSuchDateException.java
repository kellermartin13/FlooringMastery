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
public class NoSuchDateException extends Exception {
    
    public NoSuchDateException(String message) {
        super(message);
    }
    
    public NoSuchDateException(String message, Throwable cause) {
        super(message, cause);
    }
}
