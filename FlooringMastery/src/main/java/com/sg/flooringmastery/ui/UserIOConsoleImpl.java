/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.ui;

import java.math.BigDecimal;
import java.util.Scanner;

/**
 *
 * @author Keller Martin
 */
public class UserIOConsoleImpl implements UserIO {

    Scanner sc = new Scanner(System.in);

    @Override
    public void println(String message) {
        System.out.println(message);

    }

    @Override
    public int readInt(String promptMsg) {
        System.out.println(promptMsg);
        boolean isInt = false;
        int num = 0;

        while (!isInt) {
            while (!sc.hasNextInt()) {
                System.out.println(promptMsg);
                sc.next();
            }
            num = sc.nextInt();
            sc.nextLine();
            isInt = true;
        }
        return num;

    }

    @Override
    public int readInt(String promptMsg, int minVal, int maxVal) {
        System.out.println(promptMsg);
        boolean isInt = false;
        int num = 0;

        while (!isInt) {
            do {
                while (!sc.hasNextInt()) {
                    System.out.println(promptMsg);
                    sc.next();
                    isInt = true;
                }
                num = sc.nextInt();
                sc.nextLine();
                if (num < minVal || num > maxVal) {
                    System.out.println("Please input a number between "
                            + minVal + " and " + maxVal + ".");
                    num = 0;
                    isInt = false;
                }

            } while (num < minVal || num > maxVal);

            isInt = true;
        }
        return num;
    }

    @Override
    public String readString(String promptMsg) {
        System.out.println(promptMsg);
        String string = sc.nextLine();
        return string;
    }

    @Override
    public BigDecimal readBigDecimal(String promptMsg) {
        System.out.println(promptMsg);

        boolean isBd = false;
        BigDecimal bd = new BigDecimal("0.00");
        while (!isBd) {
            do {
                while (!sc.hasNextInt()) {
                    System.out.println(promptMsg);
                    sc.next();
                    isBd = true;
                }
                bd = sc.nextBigDecimal();
                sc.nextLine();
                
                if (bd.compareTo(new BigDecimal("0.00")) < 0) {
                    System.out.println("Please input a number "
                            + "greater than 0.");
                    sc.nextLine();
                    bd = sc.nextBigDecimal();
                    sc.nextLine();
                }

            } while (bd.compareTo(new BigDecimal("0.00")) <= 0);

            isBd = true;
        }
        return bd;
    }
}
