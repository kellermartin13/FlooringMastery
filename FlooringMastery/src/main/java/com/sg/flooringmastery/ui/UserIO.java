/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.ui;

import java.math.BigDecimal;

/**
 *
 * @author Keller Martin
 */
public interface UserIO {
    
    void println(String msg);

    int readInt(String prompt);

    int readInt(String prompt, int min, int max);

    String readString(String prompt);
    
    BigDecimal readBigDecimal(String prompt);
}
