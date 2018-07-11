/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.ui;

import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.dto.Product;
import com.sg.flooringmastery.dto.Tax;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 *
 * @author Keller Martin
 */
public class FlooringView {

    UserIO io;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddyyyy");

    public FlooringView(UserIO io) {
        this.io = io;
    }

    public int showMenuOptions() {
        //MENU HEADER
        io.println(this.paddingHeader("==", "=", 56));
        io.println("||" + this.paddingHeader("SWG Corp", "-", 52) + "||");
        io.println("||" + this.paddingHeader("Flooring  Orders", "-", 52) + "||");
        io.println(this.paddingHeader("==", "=", 56));

        //MENU BODY
        io.println("||" + this.paddingBody(" ", " ", 52) + "||");
        io.println("||  " + this.paddingBody("1. Display Orders", " ", 50) + "||");
        io.println("||  " + this.paddingBody("2. Add an Order", " ", 50) + "||");
        io.println("||  " + this.paddingBody("3. Edit an Order", " ", 50) + "||");
        io.println("||  " + this.paddingBody("4. Remove an Order", " ", 50) + "||");
        io.println("||  " + this.paddingBody("5. Save Current Work", " ", 50) + "||");
        io.println("||  " + this.paddingBody("6. Quit", " ", 50) + "||");
        io.println("||" + this.paddingBody(" ", " ", 52) + "||");

        io.println(this.paddingHeader("==", "=", 56));
        io.println("");
        return io.readInt("Please pick from one of the above options.", 1, 6);
    }

    public LocalDate getDate() throws InvalidDateException {
        String date = io.readString("What date is the order for? (MM/dd/yyyy)");
        String formatted = date.replace("/", "");
        LocalDate ld;
        try {
            ld = LocalDate.parse(formatted, formatter);
        } catch (DateTimeParseException e) {
            throw new InvalidDateException("-ERROR: The date entered is not recognized "
                    + "as a valid date. Please follow the given format.", e);
        }
        return ld;
    }

    public Integer getOrderNum() {
        return io.readInt("Please enter your order number.");
    }

    public void displayAllOrders(List<Order> orderList) {
        for (Order currentOrder : orderList) {
            io.println("Order #" + currentOrder.getOrderNum() + ": "
                    + currentOrder.getCustomerName() + ", "
                    + currentOrder.getState());
            io.println("Material: " + currentOrder.getProductType());
            io.println("Area: " + currentOrder.getArea() + " sq ft");
            io.println("Total Cost: $" + currentOrder.getTotal());
            io.println("");
        }
    }

    public void displayOneOrder(Order order) {

        //PADDING HEADER
        io.println(this.paddingHeader("==", "=", 56));
        io.println("||" + this.paddingHeader("==", "=", 52) + "||");
        io.println("||" + this.paddingHeader("Order Overview", "-", 52) + "||");
        io.println("||" + this.paddingHeader("==", "=", 52) + "||");
        io.println(this.paddingHeader("==", "=", 56));
        io.println("||" + this.paddingHeader("  ", " ", 52) + "||");

        //PADDING BODY
        io.println("||  " + this.paddingBody("Name", ".",
                (48 - order.getCustomerName().length()))
                + order.getCustomerName() + "  ||");
        io.println("||  " + this.paddingBody("State", ".",
                (48 - order.getState().length()))
                + order.getState() + "  ||");
        io.println("||  " + this.paddingBody("Area", ".",
                (43 - order.getArea().toString().length()))
                + order.getArea() + "sq ft  ||");
        io.println("||  " + this.paddingBody("Product Type", ".",
                (48 - order.getProductType().length()))
                + order.getProductType() + "  ||");
        io.println("||  " + this.paddingBody("Material Cost", ".",
                (47 - order.getMaterialCost().toString().length()))
                + "$" + order.getMaterialCost() + "  ||");
        io.println("||  " + this.paddingBody("Labor Cost", ".",
                (47 - order.getLaborCost().toString().length()))
                + "$" + order.getLaborCost() + "  ||");
        io.println("||  " + this.paddingBody("Tax", ".",
                (47 - order.getTax().toString().length()))
                + "$" + order.getTax() + "  ||");
        io.println("||  " + this.paddingBody("Tax", ".",
                (47 - order.getTotal().toString().length()))
                + "$" + order.getTotal() + "  ||");

        //PADDING FOOTER
        io.println("||" + this.paddingHeader("  ", " ", 52) + "||");
        io.println(this.paddingHeader("==", "=", 56));
        io.println("");
    }

    public boolean checkIfStateExists(String state, List<Tax> stateTaxes) {
        boolean stateExists = false;
        for (Tax currentTax : stateTaxes) {
            if (currentTax.getStateName().equalsIgnoreCase(state)) {
                stateExists = true;
            }
        }
        return stateExists;
    }

    public boolean checkIfProductExists(String productType,
            List<Product> products) {
        boolean productExists = false;
        for (Product currentProduct : products) {
            if (currentProduct.getProductType().equalsIgnoreCase(productType)) {
                productExists = true;
            }
        }
        return productExists;
    }

    //MAKES 'STATE' READABLE REGARDLESS OF CAPITALIZATION
    public String validateState(String state, List<Tax> stateTaxes) {
        String properStateName = null;
        for (Tax currentTax : stateTaxes) {
            if (currentTax.getStateName().equalsIgnoreCase(state)) {
                properStateName = currentTax.getStateName();
            }
        }
        return properStateName;
    }

    //MAKES 'PRODUCT' READABLE REGARDLESS OF CAPITALIZATION
    public String validateProduct(String product, List<Product> products) {
        String properName = null;
        for (Product currentProduct : products) {
            if (currentProduct.getProductType().equalsIgnoreCase(product)) {
                properName = currentProduct.getProductType();
            }
        }
        return properName;
    }

    public Order addOrder(List<Tax> stateTaxes, List<Product> productTypes)
            throws InvalidDateException {
        Order newOrder = new Order();
        LocalDate dateNeeded;

        //GET CUSTOMER NAME
        String customerName = (io.readString("Please enter "
                + "your first and last name."));
        newOrder.setCustomerName(customerName);

        //GET STATE
        this.displayStateMenuHeader();
        this.displayStateMenu(stateTaxes);
        String state;
        do {
            state = io.readString("Please enter one of the above states");
        } while (!this.checkIfStateExists(state, stateTaxes));
        //ENSURES PROPER STATE CAPITALIZATION
        newOrder.setState(this.validateState(state, stateTaxes));

        //GET PRODUCT TYPE
        this.displayProductTypeMenuHeader();
        this.displayProductTypeMenu(productTypes);
        String product;
        do {
            product = io.readString("Please pick from above options");
        } while (!this.checkIfProductExists(product, productTypes));
        //ENSURES PROPER PRODUCT CAPITALIZATION
        newOrder.setProductType(this.validateProduct(product, productTypes));

        //GET AREA
        newOrder.setArea(io.readBigDecimal("Please enter the "
                + "required square footage"));

        //GET DATE NEEDED
        String dateString = io.readString("Please enter the date you will "
                + "need the project completed (MM/dd/yyyy)");
        String formattedDate = dateString.replace("/", "");
        try {
            dateNeeded = LocalDate.parse(formattedDate, formatter);
        } catch (DateTimeParseException e) {
            throw new InvalidDateException("-ERROR: The date entered is not recognized "
                    + "as a valid date. Please follow the given format.", e);
        }
        newOrder.setDateNeeded(dateNeeded);
        return newOrder;
    }

    public String getUserConfirmation(String message) {
        String userChoice = null;
        do {
            userChoice = io.readString(message);
        } while (!(userChoice.equalsIgnoreCase("Y")
                || userChoice.equalsIgnoreCase("N")));
        return userChoice;
    }

    public Order editOrder(Order oldOrder, List<Product> products, 
            List<Tax> taxes)
            throws InvalidDateException {
        Order updatedOrder = new Order();
        LocalDate newDate;

        //CHANGE NAME
        io.println("Customer Name: " + oldOrder.getCustomerName());
        String customerName = (io.readString("Please enter a new name, "
                + "or press Enter to remain unchanged."));
        if (!customerName.equals("")) {
            updatedOrder.setCustomerName(customerName);
        } else {
            updatedOrder.setCustomerName(oldOrder.getCustomerName());
        }

        //CHANGE STATE
        io.println("State: " + oldOrder.getState());
        String state;
        this.displayStateMenuHeader();
        this.displayStateMenu(taxes);
        do {
            state = io.readString("Please enter one of the above states");
        } while (!this.checkIfStateExists(state, taxes));
        //ENSURES PROPER STATE CAPITALIZATION
        if (!state.equals("")) {
            updatedOrder.setState(state);
        } else {
            updatedOrder.setState(oldOrder.getState());
        }

        //CHANGE PRODUCT TYPE
        io.println("Product type: " + oldOrder.getProductType());
        this.displayProductTypeMenuHeader();
        this.displayProductTypeMenu(products);
        String product;
        do {
            product = io.readString("Please pick from above options");
        } while (!this.checkIfProductExists(product, products));
        
        if (!product.equals("")) {
            updatedOrder.setProductType(product);
        } else {
            updatedOrder.setProductType(oldOrder.getProductType());
        }

        //CHANGE AREA
        io.println("Area: " + oldOrder.getArea() + " sq ft");
        String areaString = io.readString("Please enter a new area, "
                + "or press Enter to remain unchanged.");
        if (!areaString.equals("")) {
            updatedOrder.setArea(new BigDecimal(areaString));
        } else {
            updatedOrder.setArea(oldOrder.getArea());
        }

        //CHANGE DATE
        String date = io.readString("Please enter a new date (MM/dd/yyyy), "
                + "or press enter to remain unchanged.");
        try {
            if (!date.equals("")) {
                String formattedDate = date.replace("/", "");
                newDate = LocalDate.parse(formattedDate, formatter);
                updatedOrder.setDateNeeded(newDate);
            } else {
                updatedOrder.setDateNeeded(oldOrder.getDateNeeded());
            }
        } catch (DateTimeParseException e) {
            throw new InvalidDateException("-ERROR: The date entered is not recognized "
                    + "as a valid date. Please follow the given format.", e);
        }
        updatedOrder.setOrderNum(oldOrder.getOrderNum());
        return updatedOrder;
    }

    private String paddingHeader(String message, String filler, int padLength) {
        String paddedString = message;
        while (paddedString.length() < padLength) {
            paddedString = filler + paddedString + filler;
        }
        return paddedString;
    }

    private String paddingBody(String message, String filler, int padLength) {
        String paddedString = message;
        while (paddedString.length() < padLength) {
            paddedString = paddedString + filler;
        }
        return paddedString;
    }

    public void displayStateMenuHeader() {
        io.println(this.paddingHeader("==", "=", 56));
        io.println("||" + this.paddingHeader("States that we Currently Serve", "-", 52) + "||");
        io.println("||" + this.paddingHeader("And Their Tax Rate", "-", 52) + "||");
        io.println("||" + this.paddingHeader("(Sorry California)", "-", 52) + "||");
        io.println(this.paddingHeader("==", "=", 56));
    }

    public void displayStateMenu(List<Tax> taxes) {
        int headerLength = 40;
        io.println("||  " + this.paddingBody("STATE", ".", headerLength)
                + "TAX RATE" + "  ||");
        io.println("||" + this.paddingBody(" ", " ", 52) + "||");
        for (Tax currentTax : taxes) {
            int stringLength = 48;
            int taxRate = currentTax.getTaxRate().toString().length();
            stringLength = stringLength - taxRate;
            String paddedString = this.paddingBody(currentTax.getStateName(),
                    ".", stringLength);
            io.println("||  " + paddedString + currentTax.getTaxRate() + "  ||");
        }
        io.println("||" + this.paddingBody(" ", " ", 52) + "||");
        io.println(this.paddingBody("=", "=", 56));

    }

    public void displayProductTypeMenuHeader() {
        io.println(this.paddingHeader("==", "=", 76));
        io.println("||"
                + this.paddingHeader("Here are your Lovely", "-", 72) + "||");
        io.println("||"
                + this.paddingHeader("Flooring Options", "-", 72) + "||");
        io.println(this.paddingHeader("==", "=", 76));
    }

    public void displayProductTypeMenu(List<Product> productList) {

        int headerLength = 40;
        io.println("||  " + this.paddingBody("MATERIAL TYPE", ".", headerLength)
                + "Cost/Sq Ft" + "||" + "Labor Cost/Sq Ft" + "  ||");
        io.println("||" + this.paddingBody(" ", " ", 72) + "||");
        for (Product currentProduct : productList) {
            int stringLength = 63;
            int costPerSqFtLength
                    = currentProduct.getCostPerSqFt().toString().length();
            int laborLength
                    = currentProduct.getLaborCostPerSqFt().toString().length();
            stringLength = stringLength - costPerSqFtLength - laborLength;
            String paddedString
                    = this.paddingBody(currentProduct.getProductType(),
                            ".", stringLength);
            io.println("||  " + paddedString + "$"
                    + currentProduct.getCostPerSqFt() + " | "
                    + "$" + currentProduct.getLaborCostPerSqFt() + "  ||");

        }
        io.println("||" + this.paddingBody(" ", " ", 72) + "||");
        io.println(this.paddingBody("=", "=", 76));

    }

    public void displayErrorMessage(String message) {
        io.println("");
        String[] splitMessage = message.split(" ");
        String line1 = null;
        String line2 = null;
        String line3 = null;
        String line4 = null;
        String line5 = null;
        for (String currentWord : splitMessage) {
            if (line1 == null) {
                line1 = currentWord;
            } else if (line1.length() < 25) {
                line1 = line1 + " " + currentWord;
            } else if (line2 == null) {
                line2 = currentWord;
            } else if (line2.length() < 25) {
                line2 = line2 + " " + currentWord;
            } else if (line3 == null) {
                line3 = currentWord;
            } else if (line3.length() < 25) {
                line3 = line3 + " " + currentWord;
            } else if (line4 == null) {
                line4 = currentWord;
            } else if (line4.length() < 25) {
                line4 = line4 + " " + currentWord;
            } else if (line5 == null) {
                line5 = currentWord;
            } else if (line5.length() < 25) {
                line5 = line5 + " " + currentWord;
            }
        }

        io.println(this.paddingHeader("=", "=", 43));
        io.println("||" + this.paddingHeader("You screwed up!", "-", 38) + "||");
        io.println(this.paddingHeader("=", "=", 43));

        io.println("||" + this.paddingHeader(" ", " ", 39) + "||");
        io.println("||" + this.paddingHeader(line1, "-", 38) + "||");
        io.println("||" + this.paddingHeader(line2, "-", 38) + "||");
        io.println("||" + this.paddingHeader(line3, "-", 38) + "||");
        if (line4 == null) {
        } else {
            io.println("||" + this.paddingHeader(line4, "-", 38) + "||");
        }
        if (line5 == null) {
        } else {
            io.println("||" + this.paddingHeader(line5, "-", 38) + "||");
        }
        io.println("||" + this.paddingHeader(" ", " ", 39) + "||");
        io.println(this.paddingHeader("=", "=", 43));
        io.println("");
        io.readString("Please hit enter to return to menu.");
    }

    public void displayNewOrderSuccess() {
        io.println("                      ___ _ _  _ ____\n"
                + "                       |  | |\\/| |___\n"
                + "     ...               |  | |  | |___\n"
                + "   ,'   '.               ___ ____\n"
                + "  ;       ;               |  |  |\n"
                + "  |       |               |  |__|\n"
                + "  |       |         _ _ _ ____ ____ _  _\n"
                + "  | _, ,_ |         | | | |  | |__/ |_/\n"
                + "  '´ | | `'         |_|_| |__| |  \\ | \\_\n"
                + "     | |    _    _              \n"
                + "     | |  .|_|_,' |         ,--.\n"
                + "     | |  |       |        :   |\n"
                + "     | |  `|`'`-._|        :   |\n"
                + "     | |   | |             :   |\n"
                + "     | |   | |             :   |\n"
                + "     | |   | |  .-------.  :   |\n"
                + "     | |   | | ,|   _O  |  :   |\n"
                + "     | |   | | ||_,'    |  :   |\n"
                + "     | |   | | `|       |  ;...:fsr\n"
                + "     | |   | |  |       | /.--. \\\n"
                + "     | |  |` |  |       | |'--' |\n"
                + "     '-'  `--'  '-------' '-----' ");
        io.println("Order successfully added.");
        io.println("");
    }

    public void displayNewOrderDiscarded() {
        io.println("\n"
                + "    jgs   ./ |  \n"
                + "         /  / \n"
                + "       /'  /  \n"
                + "      /   /    \n"
                + "     /    \\     \n"
                + "    |      `\\\n"
                + "    |        |                                ___________________\n"
                + "    |        |___________________...-------'''- - -  =- - =  - = `.\n"
                + "   /|        |                   \\-  =  = -  -= - =  - =-   =  - =|\n"
                + "  ( |        |                    |= -= - = - = - = - =--= = - = =|\n"
                + "   \\|        |___________________/- = - -= =_- =_-=_- -=_=-=_=_= -|\n"
                + "    |        |                   ```-------...___________________.'\n"
                + "    |________|      \n"
                + "      \\    /     \n"
                + "      |    |      \n"
                + "    ,-'    `-,      \n"
                + "    |        |     \n"
                + "    `--------'");
        io.println("Order discarded.");
        io.println("");
    }

    public void displayDeleteSuccess() {
        io.println("\n"
                + "    jgs   ./ |  \n"
                + "         /  / \n"
                + "       /'  /  \n"
                + "      /   /    \n"
                + "     /    \\     \n"
                + "    |      `\\\n"
                + "    |        |                                ___________________\n"
                + "    |        |___________________...-------'''- - -  =- - =  - = `.\n"
                + "   /|        |                   \\-  =  = -  -= - =  - =-   =  - =|\n"
                + "  ( |        |                    |= -= - = - = - = - =--= = - = =|\n"
                + "   \\|        |___________________/- = - -= =_- =_-=_- -=_=-=_=_= -|\n"
                + "    |        |                   ```-------...___________________.'\n"
                + "    |________|      \n"
                + "      \\    /     \n"
                + "      |    |      \n"
                + "    ,-'    `-,      \n"
                + "    |        |     \n"
                + "    `--------'");
        io.println("Order successfully deleted.");
        io.println("");
    }

    public void displayOrderNotDeleted() {
        io.println("Your order remains!");
        io.println("");
    }

    public void displayEditSuccess() {
        io.println("Order successfully edited!");
        io.println("");
    }

    public void displayOrderNotEdited() {
        io.println("Your original order is unchanged.");
        io.println("");
    }

    public void displayExitMessage() {
        io.println("Good Bye!");
    }

    public void displaySaveSuccess() {
        io.println("                 /~~~~~~~~~\\\n"
                + "                (===========) ______________\n"
                + "                |  ||  ||   ||~~~~~~~~~~~~~~|\n"
                + "                |  ||  ||   ||        (@)   |\n"
                + "                |  ||  ||   ||        //    |\n"
                + "                |  ||  ||   ||       //     |\n"
                + "                |  ||  ||   ||(@)===(o)     |\n"
                + "                |  ||  ||   ||        \\\\    |\n"
                + "                |           ||         \\\\   |\n"
                + "                |~~~~~~~~~~~||         (@)  |\n"
                + "                |___________||              |\n"
                + "                (___________)|              |\n"
                + "                 (_________) |    @--(o)    |\n"
                + "                   |     |   (              (\n"
                + "                   |     |    \\      (o)     \\\n"
                + "                   |     |     \\     /        \\\n"
                + "                   |     |      \\   @          \\\n"
                + "                   |_____|       \\              \\\n"
                + "                   |_____|        \\              \\\n"
                + "                   \\_____/         \\              \\\n"
                + "                     |/|            \\              \\\n"
                + "                  )  |/|             \\              \\\n"
                + "                 (  ,|/|  / '         \\              \\\n"
                + "                  \\  |/| ( '           \\              \\\n"
                + "            _____  ) |/|' )         _   \\              \\\n"
                + "      |    |     |___|/|___________| |   \\              \\\n"
                + "      |====|     |_________________| |    \\              \\\n"
                + "     =|   _|      |_______________|  |     \\              \\\n"
                + "         |                           |      \\              \\\n"
                + "    _____|___________________________|_______)______________)\n"
                + "   |                                                        |\n"
                + "   |                                                        |\n"
                + "   |________________________________________________________|");
        io.println("");
        io.println("Your work has been saved!");
        io.println("");
    }

}
