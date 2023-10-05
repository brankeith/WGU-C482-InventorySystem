package Model;

import javafx.beans.property.*;
import javafx.beans.property.StringProperty;
/**
 * Abstract class representing a generic Part.
 * This class provides the base functionality for both InHouse and Outsourced parts.
 */
public abstract class Part {


    private final IntegerProperty partID, inStock, min, max;
    private final StringProperty name;
    private final DoubleProperty price;


    /**
     * Default constructor to initialize all properties.
     */
    public Part() {
        partID = new SimpleIntegerProperty();
        name = new SimpleStringProperty();
        price = new SimpleDoubleProperty();
        inStock = new SimpleIntegerProperty();
        min = new SimpleIntegerProperty();
        max = new SimpleIntegerProperty();
    }
    /**
     * Default constructor to initialize all properties.
     */
    public IntegerProperty partIdProp() {
        return partID;
    }
    /**
     * Returns the StringProperty object for the part name.
     * @return StringProperty for part name.
     */
    public StringProperty partNameProp() {
        return name;
    }
    /**
     * Returns the DoubleProperty object for the part price.
     * @return DoubleProperty for part price.
     */
    public DoubleProperty partPriceProp() {
        return price;
    }
    /**
     * Returns the IntegerProperty object for the part's inventory level.
     * @return IntegerProperty for inventory level.
     */
    public IntegerProperty partInvProp() {
        return inStock;
    }


    //Getters & Setters
    public int getPartID() {
        return this.partID.get();
    }

    public String getPartName() {
        return this.name.get();
    }

    public double getPartPrice() {
        return this.price.get();
    }

    public int getPartInStock() {
        return this.inStock.get();
    }

    public int getPartMin() {
        return this.min.get();
    }

    public int getPartMax() {
        return this.max.get();
    }

    public void setPartID(int partID) {
        this.partID.set(partID);
    }

    public void setPartName(String name) {
        this.name.set(name);
    }

    public void setPartPrice(double price) {
        this.price.set(price);
    }

    public void setPartInStock(int inStock) {
        this.inStock.set(inStock);
    }

    public void setPartMin(int min) {
        this.min.set(min);
    }

    public void setPartMax(int max) {
        this.max.set(max);
    }

    /**
     * Validates the part details.
     *
     * @param name The name of the part.
     * @param min The minimum allowable quantity for the part.
     * @param max The maximum allowable quantity for the part.
     * @param inv The current inventory level.
     * @param price The price of the part.
     * @param errorMessage The existing error message, if any.
     * @return Updated error message based on validation.
     */
    public static String isPartValid(String name, int min, int max, int inv, double price,boolean isOutsourced, String partDyn, String errorMessage) {
        StringBuilder errorBuilder = new StringBuilder(errorMessage);

        if (name == null || name.trim().isEmpty()) {
            errorBuilder.append("The name field is blank. ");
        }
        if (inv < 1) {
            errorBuilder.append("The inventory count must be greater than 0. ");
        }
        if (price <= 0) {
            errorBuilder.append("The price must be greater than $0. ");
        }
        if (max < min) {
            errorBuilder.append("The Max must be greater than or equal to the Min. ");
        }
        if (inv < min || inv > max) {
            errorBuilder.append("The inventory must be between the Min and Max values. ");
        }
        if (isOutsourced) {
            if (partDyn == null || partDyn.trim().isEmpty()) {
                errorBuilder.append("Company Name is blank. ");
            }
        } else {
            try {
                int machineID = Integer.parseInt(partDyn);
                if (machineID <= 0) {
                    errorBuilder.append("Machine ID must be greater than 0. ");
                }
            } catch (NumberFormatException e) {
                errorBuilder.append("Machine ID is invalid. ");
            }
        }
        return errorBuilder.toString();
    }

    }
