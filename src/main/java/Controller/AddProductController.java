package Controller;

import Model.Inventory;
import Model.Part;
import Model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static Model.Inventory.getPartInv;
/**
 * The AddProductController class manages the UI and interactions for adding a new Product.
 * It enables the user to search for parts and add them to or delete them from the current product.
 */
public class AddProductController implements Initializable {

    @FXML private TextField txtAddProductName, txtAddProductInv, txtAddProductPrice, txtAddProductMin, txtAddProductMax, txtAddProdSearch;
    @FXML private TableView<Part> tvAddProdAdd, tvAddProdDelete;
    @FXML private TableColumn<Part, Integer> tvAddProductAddIDColumn;
    @FXML private TableColumn<Part, String> tvAddProductAddNameColumn;
    @FXML private TableColumn<Part, Integer> tvAddProductAddInvColumn;
    @FXML private TableColumn<Part, Double> tvAddProductAddPriceColumn;
    @FXML private TableColumn<Part, Integer> tvAddProductDeleteIDColumn;
    @FXML private TableColumn<Part, String> tvAddProductDeleteNameColumn;
    @FXML private TableColumn<Part, Integer> tvAddProductDeleteInvColumn;
    @FXML private TableColumn<Part, Double> tvAddProductDeletePriceColumn;
    @FXML private Label lblAddProductIDNumber;
    /**
     * A list to hold the current parts of a product.
     */
    private ObservableList<Part> currentParts = FXCollections.observableArrayList();
    /**
     * An integer to hold the ProductID.
     */
    private int productID;
    /**
     * A string to hold any exception messages.
     */
    private String exceptionMessage = "";


    /**
     * The event that occurs when the Search button is clicked.
     *
     * @param event the action event
     */
    @FXML
    private void addProdSearch(ActionEvent event) throws IOException {
        String searchPart = txtAddProdSearch.getText();
        int partIndex = -1;

        if (Inventory.lookupPart(searchPart) == -1) {
            displayAlert(1, "The search term entered does not match any known parts.");
        } else {
            partIndex = Inventory.lookupPart(searchPart);
            Part tempPart = getPartInv().get(partIndex);
            ObservableList<Part> tempPartList = FXCollections.observableArrayList();
            tempPartList.add(tempPart);
            tvAddProdAdd.setItems(tempPartList);
        }
    }

    /**
     * Adds a selected part to the current product.
     *
     * @param event The action event.
     */
    @FXML
    void addProd(ActionEvent event) {
        Part part = tvAddProdAdd.getSelectionModel().getSelectedItem();
        currentParts.add(part);
        updateDeletePartTv();
    }
    /**
     * Deletes a selected part from the current product.
     *
     * @param event The action event.
     * @throws IOException If there is an IO error.
     */
    @FXML
    void handleDelete(ActionEvent event) {
        Part part = tvAddProdDelete.getSelectionModel().getSelectedItem();
        if(part != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Are you sure you want to delete " + part.getPartName() + " from parts?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                System.out.println("Part deleted.");
                currentParts.remove(part);
            } else {
                System.out.println("Cancelled.");
            }
        } else {
            displayAlert(5, "No part selected to delete.");
        }
    }

    /**
     * Saves the newly created product.
     *
     * @param event The action event.
     * @throws IOException If there is an IO error.
     */

    @FXML
    void handleAddProductSave(ActionEvent event) throws IOException {
        String productName = txtAddProductName.getText();
        String productInv = txtAddProductInv.getText();
        String productPrice = txtAddProductPrice.getText();
        String productMin = txtAddProductMin.getText();
        String productMax = txtAddProductMax.getText();

        try{
            if (productName.trim().isEmpty() || productInv.trim().isEmpty() || productPrice.trim().isEmpty() || productMin.trim().isEmpty() || productMax.trim().isEmpty()) {
                displayAlert(4, "Form contains blank fields.");
                return;
            }

            int inv = Integer.parseInt(productInv);
            double price = Double.parseDouble(productPrice);
            int min = Integer.parseInt(productMin);
            int max = Integer.parseInt(productMax);

            exceptionMessage = Product.isProductValid(productName, min, max, inv, price, currentParts, exceptionMessage);
            if (exceptionMessage.length() > 0) {
                displayAlert(3, exceptionMessage);
                exceptionMessage = "";
            }
            else {
                System.out.println("Product name: " + productName);
                Product newProduct = new Product();
                newProduct.setProductID(productID);
                newProduct.setProductName(productName);
                newProduct.setProductInStock(inv);
                newProduct.setProductPrice(price);
                newProduct.setProductMin(min);
                newProduct.setProductMax(max);
                newProduct.setProductParts(currentParts);
                Inventory.addProd(newProduct);

                Parent addProductSaveParent = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
                Scene scene = new Scene(addProductSaveParent);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            }
        }
        catch (NumberFormatException e) {
            displayAlert(4, "Form contains invalid field formats.");
        }
    }

    /**
     * Cancels the creation of a new product.
     *
     * @param event The action event.
     * @throws IOException If there is an IO error.
     */
    @FXML
    private void handleAddProductCancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Are you sure you want to cancel adding a new product?");
        Optional<ButtonType> result = alert.showAndWait();


        if (result.get() == ButtonType.OK) {
            Parent addProductCancel = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
            Scene scene = new Scene(addProductCancel);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } else {
            System.out.println("Cancelled.");
        }
    }
    /**
     * Displays an alert based on the given alert type and details.
     *
     * @param alertType The type of the alert.
     * @param details Additional information for the alert.
     * @throws IOException If there is an IO error.
     */
    private void displayAlert(int alertType, String details) {
        Alert alert;
        switch (alertType) {
            case 1:
                alert = new Alert(Alert.AlertType.ERROR, details, ButtonType.OK);
                alert.setTitle("Search Error");
                alert.setHeaderText("Part not found");
                break;
            case 2:
                alert = new Alert(Alert.AlertType.CONFIRMATION, details, ButtonType.YES, ButtonType.NO);
                break;
            case 3:
                alert = new Alert(Alert.AlertType.ERROR, details, ButtonType.OK);
                alert.setTitle("Error");
                alert.setContentText(exceptionMessage);
            case 4:
                alert = new Alert(Alert.AlertType.INFORMATION, details, ButtonType.OK);
                alert.setTitle("Error");
                alert.setHeaderText("Error Adding Product");
                break;
            case 5:
                alert = new Alert(Alert.AlertType.ERROR, details, ButtonType.OK);
                break;
            default:
                alert = new Alert(Alert.AlertType.NONE);
        }
        alert.showAndWait();
    }

    /**
     * Initializes the controller class. This method automatically gets called
     * after the fxml file has been loaded.
     *
     * @param url The location used to resolve relative paths for the root object.
     * @param rb The resources used to localize the root object.
     */

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tvAddProductAddIDColumn.setCellValueFactory(cellData -> cellData.getValue().partIdProp().asObject());
        tvAddProductAddNameColumn.setCellValueFactory(cellData -> cellData.getValue().partNameProp());
        tvAddProductAddInvColumn.setCellValueFactory(cellData -> cellData.getValue().partInvProp().asObject());
        tvAddProductAddPriceColumn.setCellValueFactory(cellData -> cellData.getValue().partPriceProp().asObject());
        tvAddProductDeleteIDColumn.setCellValueFactory(cellData -> cellData.getValue().partIdProp().asObject());
        tvAddProductDeleteNameColumn.setCellValueFactory(cellData -> cellData.getValue().partNameProp());
        tvAddProductDeleteInvColumn.setCellValueFactory(cellData -> cellData.getValue().partInvProp().asObject());
        tvAddProductDeletePriceColumn.setCellValueFactory(cellData -> cellData.getValue().partPriceProp().asObject());
        updateAddPartTv();
        updateDeletePartTv();
        productID = Inventory.getProdIDCount();
        lblAddProductIDNumber.setText("Auto-Gen: " + productID);
    }
    /**
     * Updates the Add Parts TableView to reflect the current inventory.
     */
    public void updateAddPartTv() {
        tvAddProdAdd.setItems(getPartInv());
    }
    /**
     * Updates the Delete Parts TableView based on the current parts in the product.
     */
    public void updateDeletePartTv() {
        tvAddProdDelete.setItems(currentParts);
    }
}