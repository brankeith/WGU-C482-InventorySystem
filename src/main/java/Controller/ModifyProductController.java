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
import static Controller.MainScreenController.productToModifyIndex;
import static Model.Inventory.getPartInv;
import static Model.Inventory.getProductInv;

/**
 * ModifyProductController class handles the modification of products in the inventory.
 * This class allows searching for parts, adding and deleting parts, and saving or canceling the modification.
 * <p>
 * Logical or Runtime Errors Corrected:
 * - Added NumberFormatException handling to ensure that empty fields don't crash the application.
 * </p>
 * <p>
 * Future Enhancements:
 * - Add more robust validation for form data.
 * - Implement batch modifications for products.
 * </p>
 */
public class ModifyProductController implements Initializable {

    @FXML private TextField txtModProdName, txtModProdInv, txtModProdPrice, txtModProdMin, txtModProdMax, txtModProdSearch;
    @FXML private TableView<Part> tvModProdAdd, tvModProdDelete;
    @FXML private TableColumn<Part, Integer> tcModProdAddID, tcModProdDeleteID;
    @FXML private TableColumn<Part, String> tcModProdAddName, tcModProdDeleteName;
    @FXML private TableColumn<Part, Integer> tcModProdAddInv, tcModProdDeleteInv;
    @FXML private TableColumn<Part, Double> tcModProdAddPrice, tcModProdDeletePrice;
    @FXML private Label lblModifyProductIDNumber;
    /**
     * Observable list to hold current parts associated with the product being modified.
     */
    private ObservableList<Part> currentParts = FXCollections.observableArrayList();
    private int productIndex = productToModifyIndex();
    private String exceptionMessage = "";
    private int productID;
    /**
     * Handles the search functionality in the Modify Product screen.
     * @param event ActionEvent associated with the function call.
     */
    @FXML
    void modSearch(ActionEvent event) {
        String searchPart = txtModProdSearch.getText();
        int partIndex = -1;
        if (Inventory.lookupPart(searchPart) == -1) {
            displayAlert(1);
        } else {
            partIndex = Inventory.lookupPart(searchPart);
            Part tempPart = Inventory.getPartInv().get(partIndex);
            ObservableList<Part> tempPartList = FXCollections.observableArrayList();
            tempPartList.add(tempPart);
            tvModProdAdd.setItems(tempPartList);
        }
    }

    /**
     * Adds a selected part to the current parts list.
     * @param event ActionEvent associated with the function call.
     */
    @FXML
    void modAdd(ActionEvent event) {
        Part part = tvModProdAdd.getSelectionModel().getSelectedItem();
        currentParts.add(part);
        updateDeletePartsTv();
    }
    /**
     * Deletes a selected part from the current parts list.
     * @param event ActionEvent associated with the function call.
     */
    @FXML
    void modDelete(ActionEvent event) {
        Part part = tvModProdDelete.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        displayAlert(4);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            currentParts.remove(part);
        } else {
            System.out.println("Cancelled.");
        }
    }

    /**
     * Saves the modified product.
     * @param event ActionEvent associated with the function call.
     * @throws IOException if any IO exception occurs.
     */
    @FXML
    private void modProdSave(ActionEvent event) throws IOException {
        String productName = txtModProdName.getText();
        String productInv = txtModProdInv.getText();
        String productPrice = txtModProdPrice.getText();
        String productMin = txtModProdMin.getText();
        String productMax = txtModProdMax.getText();

        try {
            exceptionMessage = Product.isProductValid(productName, Integer.parseInt(productMin), Integer.parseInt(productMax), Integer.parseInt(productInv), Double.parseDouble(productPrice), currentParts, exceptionMessage);
            if (exceptionMessage.length() > 0) {
                displayAlert(2);
                exceptionMessage = "";
            }
            else {
                System.out.println("Product name: " + productName);
                Product newProduct = new Product();
                newProduct.setProductID(productID);
                newProduct.setProductName(productName);
                newProduct.setProductInStock(Integer.parseInt(productInv));
                newProduct.setProductPrice(Double.parseDouble(productPrice));
                newProduct.setProductMin(Integer.parseInt(productMin));
                newProduct.setProductMax(Integer.parseInt(productMax));
                newProduct.setProductParts(currentParts);
                Inventory.updateProd(productIndex, newProduct);

                Parent modifyProductSaveParent = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
                Scene scene = new Scene(modifyProductSaveParent);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            }
        }
        catch (NumberFormatException e) {
            displayAlert(3);
        }
    }
    /**
     * Cancels the modification and returns to the main screen.
     * @param event ActionEvent associated with the function call.
     * @throws IOException if any IO exception occurs.
     */
    @FXML
    private void modProdCancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        displayAlert(5);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            Parent modifyProductCancelParent = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
            Scene scene = new Scene(modifyProductCancelParent);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        }
        else {
            System.out.println("Cancelled.");
        }
    }
    /**
     * Displays different types of alerts based on the alertType parameter.
     * @param alertType An integer representing the type of alert to be displayed.
     */
    private void displayAlert(int alertType) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        Alert alertConf = new Alert(Alert.AlertType.CONFIRMATION);

        switch (alertType) {
            case 1:
                alert.setTitle("Error");
                alert.setContentText("The search term entered does not match any known parts.");
                alert.showAndWait();
                break;
            case 2:
                alert.setTitle("Error");
                alert.setHeaderText("Error Modifying Product");
                alert.showAndWait();
                break;
            case 3:
                alert.setTitle("Error");
                alert.setContentText("Form contains blank fields.");
                alert.showAndWait();
                break;
            case 4:
                alertConf.setTitle("Confirmation");
                alertConf.setContentText("Are you sure you want to delete part from parts?");
                alertConf.showAndWait();
                break;
            case 5:
                alertConf.setTitle("Confirmation");
                alertConf.setContentText("Are you sure you want to cancel modifying the product?");
                alertConf.showAndWait();
                break;
        }
    }
    /**
     * Initializes the ModifyProductController. This method sets initial data and updates tables.
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param rb The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Product product = getProductInv().get(productIndex);
        productID = getProductInv().get(productIndex).getProductID();
        lblModifyProductIDNumber.setText("Auto-Gen: " + productID);
        txtModProdName.setText(product.getProductName());
        txtModProdInv.setText(Integer.toString(product.getProductInStock()));
        txtModProdPrice.setText(Double.toString(product.getProductPrice()));
        txtModProdMin.setText(Integer.toString(product.getProductMin()));
        txtModProdMax.setText(Integer.toString(product.getProductMax()));
        currentParts = product.getProductParts();
        tcModProdAddID.setCellValueFactory(cellData -> cellData.getValue().partIdProp().asObject());
        tcModProdAddName.setCellValueFactory(cellData -> cellData.getValue().partNameProp());
        tcModProdAddInv.setCellValueFactory(cellData -> cellData.getValue().partInvProp().asObject());
        tcModProdAddPrice.setCellValueFactory(cellData -> cellData.getValue().partPriceProp().asObject());
        tcModProdDeleteID.setCellValueFactory(cellData -> cellData.getValue().partIdProp().asObject());
        tcModProdDeleteName.setCellValueFactory(cellData -> cellData.getValue().partNameProp());
        tcModProdDeleteInv.setCellValueFactory(cellData -> cellData.getValue().partInvProp().asObject());
        tcModProdDeletePrice.setCellValueFactory(cellData -> cellData.getValue().partPriceProp().asObject());
        updateAddPartsTv();
        updateDeletePartsTv();
    }

    public void updateAddPartsTv() {
        tvModProdAdd.setItems(getPartInv());
    }

    public void updateDeletePartsTv() {
        tvModProdDelete.setItems(currentParts);
    }
}