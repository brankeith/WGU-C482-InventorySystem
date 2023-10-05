package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
/**
 * Inventory class to manage the product and part inventory.
 */
public class Inventory {
    /**
     * Observable list to store the inventory of products.
     */
    private static ObservableList<Product> productInv = FXCollections.observableArrayList();
    /**
     * Observable list to store the inventory of parts.
     */
    private static ObservableList<Part> partInv = FXCollections.observableArrayList();
    /**
     * Counter to auto-generate part IDs.
     */
    private static int partIDCount = 0;
    /**
     * Counter to auto-generate product IDs.
     */
    private static int productIDCount = 0;
    /**
     * Returns the current part inventory.
     * @return The observable list of parts.
     */
    public static ObservableList<Part> getPartInv() {
        return partInv;
    }
    /**
     * Adds a part to the inventory.
     * @param part The part to be added.
     */
    public static void addPart(Part part) {
        partInv.add(part);
    }
    /**
     * Removes a part from the inventory.
     * @param part The part to be removed.
     */
    public static void removePart(Part part) {
        partInv.remove(part);
    }
    /**
     * Updates a part in the inventory at a specific index.
     * @param index The index where the part needs to be updated.
     * @param part The new part to replace the existing one.
     */
    public static void updatePart(int index, Part part) {
        partInv.set(index, part);
    }
    /**
     * Returns and increments the part ID count.
     * @return The new part ID count.
     */
    public static int getPartIDCount() {
        partIDCount++;
        return partIDCount;
    }
    /**
     * Validates if a part can be deleted based on its associations.
     * @param part The part to be validated.
     * @return True if part is found in any product, otherwise false.
     */
    public static boolean validatePartDelete(Part part) {
        boolean isFound = false;
        for (int i = 0; i < productInv.size(); i++) {
            if (productInv.get(i).getProductParts().contains(part)) {
                isFound = true;
            }
        }
        return isFound;
    }
    /**
     * Validates if a product can be deleted based on its associations.
     * @param product The product to be validated.
     * @return True if product has associated parts, otherwise false.
     */
    public static boolean validateProductDelete(Product product) {
        boolean isFound = false;
        int productID = product.getProductID();
        for (int i = 0; i < productInv.size(); i++) {
            if (productInv.get(i).getProductID() == productID) {
                if (!productInv.get(i).getProductParts().isEmpty()) {
                    isFound = true;
                }
            }
        }
        return isFound;
    }
    /**
     * Searches for a part by its ID or name.
     * @param searchTerm The term used for searching.
     * @return The index of the part if found, otherwise -1.
     */
    public static int lookupPart(String searchTerm) {
        boolean isFound = false;
        int index = 0;
        if (isInt(searchTerm)) {
            for (int i = 0; i < partInv.size(); i++) {
                if (Integer.parseInt(searchTerm) == partInv.get(i).getPartID()) {
                    index = i;
                    isFound = true;
                }
            }
        }
        else {
            for (int i = 0; i < partInv.size(); i++) {
                searchTerm = searchTerm.toLowerCase();
                if (searchTerm.equals(partInv.get(i).getPartName().toLowerCase())) {
                    index = i;
                    isFound = true;
                }
            }
        }

        if (isFound == true) {
            return index;
        }
        else {
            System.out.println("No parts found.");
            return -1;
        }
    }
    /**
     * Returns the current product inventory.
     * @return The observable list of products.
     */
    public static ObservableList<Product> getProductInv() {
        return productInv;
    }
    /**
     * Adds a product to the inventory.
     * @param product The product to be added.
     */
    public static void addProd(Product product) {
        productInv.add(product);
    }
    /**
     * Removes a product from the inventory.
     * @param product The product to be removed.
     */
    public static void removeProd(Product product) {
        productInv.remove(product);
    }
    /**
     * Returns and increments the product ID count.
     * @return The new product ID count.
     */
    public static int getProdIDCount() {
        productIDCount++;
        return productIDCount;
    }
    /**
     * Searches for a product by its ID or name.
     * @param searchTerm The term used for searching.
     * @return The index of the product if found, otherwise -1.
     */
    public static int lookupProd(String searchTerm) {
        boolean isFound = false;
        int index = 0;
        if (isInt(searchTerm)) {
            for (int i = 0; i < productInv.size(); i++) {
                if (Integer.parseInt(searchTerm) == productInv.get(i).getProductID()) {
                    index = i;
                    isFound = true;
                }
            }
        }
        else {
            for (int i = 0; i < productInv.size(); i++) {
                if (searchTerm.equals(productInv.get(i).getProductName())) {
                    index = i;
                    isFound = true;
                }
            }
        }

        if (isFound == true) {
            return index;
        }
        else {
            System.out.println("No products found.");
            return -1;
        }
    }
    /**
     * Updates a product in the inventory at a specific index.
     * @param index The index where the product needs to be updated.
     * @param product The new product to replace the existing one.
     */
    public static void updateProd(int index, Product product) {
        productInv.set(index, product);
    }
    /**
     * Checks if a given input string is an integer.
     * @param input The string to be checked.
     * @return True if the string is an integer, otherwise false.
     */
    public static boolean isInt(String input) {
        try {
            Integer.parseInt(input);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
}