package Model;


import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
/**
 * Represents a Product with its associated details and parts.
 */
public class Product {

    private static ObservableList<Model.Part> parts = FXCollections.observableArrayList();
    private final IntegerProperty productID, inStock, min, max;
    private final StringProperty name;
    private final DoubleProperty price;

    /**
     * Default constructor initializing properties for the Product class.
     */
    public Product() {
        productID = new SimpleIntegerProperty();
        name = new SimpleStringProperty();
        price = new SimpleDoubleProperty();
        inStock = new SimpleIntegerProperty();
        min = new SimpleIntegerProperty();
        max = new SimpleIntegerProperty();
    }
    /**
     * Gets the product ID property.
     * @return The IntegerProperty for product ID.
     */
    public IntegerProperty productIDProperty() {
        return productID;
    }
    /**
     * Gets the product name property.
     * @return The StringProperty for product name.
     */
    public StringProperty productNameProperty() {
        return name;
    }
    /**
     * Gets the product price property.
     * @return The DoubleProperty for product price.
     */
    public DoubleProperty productPriceProperty() {
        return price;
    }
    /**
     * Gets the product inventory property.
     * @return The IntegerProperty for product inventory.
     */
    public IntegerProperty productInvProperty() {
        return inStock;
    }
    //Getters & Setters
    public int getProductID() {
        return this.productID.get();
    }

    public String getProductName() {
        return this.name.get();
    }

    public double getProductPrice() {
        return this.price.get();
    }

    public int getProductInStock() {
        return this.inStock.get();
    }

    public int getProductMin() {
        return this.min.get();
    }

    public int getProductMax() {
        return this.max.get();
    }

    public ObservableList getProductParts() {
        return parts;
    }


    //// Setters
    public void setProductID(int productID) {
        this.productID.set(productID);
    }

    public void setProductName(String name) {
        this.name.set(name);
    }

    public void setProductPrice(double price) {
        this.price.set(price);
    }

    public void setProductInStock(int inStock) {
        this.inStock.set(inStock);
    }

    public void setProductMin(int min) {
        this.min.set(min);
    }

    public void setProductMax(int max) {
        this.max.set(max);
    }
    /**
     * Sets the list of parts for the product.
     * @param parts An ObservableList of parts.
     */
    public void setProductParts(ObservableList<Model.Part> parts) {
        Product.parts = parts;
    }


    /**
     * Validates the product based on various rules.
     * @param name Name of the product.
     * @param min Minimum inventory level for the product.
     * @param max Maximum inventory level for the product.
     * @param inv Current inventory level for the product.
     * @param price Price of the product.
     * @param parts ObservableList of parts associated with the product.
     * @param errorMessage The initial error message, usually empty.
     * @return An error message string. If empty, the product is valid.
     */
    public static String isProductValid(String name, int min, int max, int inv, double price, ObservableList<Part> parts, String errorMessage) {
        double sumOfParts = 0.00;
        for (Part part : parts) {
            sumOfParts += part.getPartPrice();
        }

        StringBuilder errorMessageBuilder = new StringBuilder(errorMessage);

        if (name == null || name.trim().isEmpty()) {
            errorMessageBuilder.append("The name field is blank. ");
        }
        if (inv < 1) {
            errorMessageBuilder.append("The inventory count must be greater than 0. ");
        }
        if (price <= 0) {
            errorMessageBuilder.append("The price must be greater than $0. ");
        }
        if (max < min) {
            errorMessageBuilder.append("The Max must be greater than or equal to the Min. ");
        }
        if (inv < min || inv > max) {
            errorMessageBuilder.append("The inventory must be between the Min and Max values. ");
        }
        if (parts.size() < 2) {
            errorMessageBuilder.append("The product must contain at least 2 parts. ");
        }
        if (sumOfParts > price) {
            errorMessageBuilder.append("Price must be greater than the sum of all part costs. ");
        }

        return errorMessageBuilder.toString();
    }

}