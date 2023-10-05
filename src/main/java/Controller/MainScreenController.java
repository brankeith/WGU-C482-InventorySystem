package Controller;

import Model.Inventory;
import Model.Part;
import Model.Product;
import static Model.Inventory.validatePartDelete;
import static Model.Inventory.validateProductDelete;
import static Model.Inventory.getPartInv;
import static Model.Inventory.removePart;
import static Model.Inventory.getProductInv;
import static Model.Inventory.removeProd;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.util.Optional;

/**
 * This class is responsible for controlling the main screen of the application.
 * It handles all interactions with both parts and products, including searching,
 * deleting, and navigating to add or modify screens.
 */
public class MainScreenController implements Initializable {

    @FXML private TableView<Part> tvParts;
    @FXML private TableView<Product> tvProducts;
    @FXML private TableColumn<Part, Integer> tvPartsIDColumn;
    @FXML private TableColumn<Part, String> tvPartsNameColumn;
    @FXML private TableColumn<Part, Integer> tvPartsInvColumn;
    @FXML private TableColumn<Part, Double> tvPartsPriceColumn;
    @FXML private TableColumn<Product, Integer> tvProductsIDColumn;
    @FXML private TableColumn<Product, String> tvProductsNameColumn;
    @FXML private TableColumn<Product, Integer> tvProductsInvColumn;
    @FXML private TableColumn<Product, Double> tvProductsPriceColumn;
    @FXML private TextField txtSearchParts, txtSearchProducts;
    private static Part modifyPart;
    private static int modifyPartIndex;
    private static Product modifyProduct;
    private static int modifyProductIndex;

    public static int partToModifyIndex() {
        return modifyPartIndex;
    }

    public static int productToModifyIndex() {
        return modifyProductIndex;
    }
    /**
     * Handles the search for parts.
     *
     * @param event The triggering event.
     * Logical error that could occur: -1 is used to denote "not found," but if it's misinterpreted,
     * it could result in an ArrayIndexOutOfBoundsException.
     */
    @FXML
    private void partsSearch(ActionEvent event) {
        String searchPart = txtSearchParts.getText();
        int partIndex = -1;
        if (Inventory.lookupPart(searchPart) == -1) {
            displayAlert(1);
        } else {
            partIndex = Inventory.lookupPart(searchPart);
            Part tempPart = Inventory.getPartInv().get(partIndex);
            ObservableList<Part> tempPartList = FXCollections.observableArrayList();
            tempPartList.add(tempPart);
            tvParts.setItems(tempPartList);
        }
    }
    /**
     * Handles deleting a selected part.
     *
     * @param event The triggering event.
     * Future Enhancement: Adding undo functionality for deleted parts.
     */
    @FXML
    private void partsDelete(ActionEvent event) {
        Part part = tvParts.getSelectionModel().getSelectedItem();
        if (validatePartDelete(part)) {
            displayAlert(2);
        } else {
            displayAlert(3);
        }
    }
    /**
     * Handles the search for products.
     *
     * @param event The triggering event.
     *
     */
    @FXML
    private void productsSearch(ActionEvent event) {
        String searchProduct = txtSearchProducts.getText();
        int prodIndex = -1;
        if (Inventory.lookupProd(searchProduct) == -1) {
            displayAlert(4);
        } else {
            prodIndex = Inventory.lookupProd(searchProduct);
            Product tempProduct = Inventory.getProductInv().get(prodIndex);
            ObservableList<Product> tempProductList = FXCollections.observableArrayList();
            tempProductList.add(tempProduct);
            tvProducts.setItems(tempProductList);
        }
    }
    /**
     * Handles deleting a selected product.
     *
     * @param event The triggering event.
     * Future Enhancement: Adding undo functionality for deleted parts.
     */
    @FXML
    private void productsDelete(ActionEvent event) {
        Product product = tvProducts.getSelectionModel().getSelectedItem();
        if (validateProductDelete(product)) {
            displayAlert(5);
        } else {
            displayAlert(6);
        }
    }


    @FXML
    private void openAddPartScreen(ActionEvent event) throws IOException {
        Parent addPartParent = FXMLLoader.load(getClass().getResource("/view/AddPart.fxml"));
        Scene addPartScene = new Scene(addPartParent);
        Stage addPartStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        addPartStage.setScene(addPartScene);
        addPartStage.show();
    }

    @FXML
    private void openModifyPartScreen(ActionEvent event) throws IOException {
        modifyPart = tvParts.getSelectionModel().getSelectedItem();
        modifyPartIndex = getPartInv().indexOf(modifyPart);
        Parent modifyPartParent = FXMLLoader.load(getClass().getResource("/view/ModifyPart.fxml"));
        Scene modifyPartScene = new Scene(modifyPartParent);
        Stage modifyPartStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        modifyPartStage.setScene(modifyPartScene);
        modifyPartStage.show();
    }
    @FXML
    private void openAddProductScreen(ActionEvent event) throws IOException {
        Parent addProductParent = FXMLLoader.load(getClass().getResource("/view/AddProduct.fxml"));
        Scene addProductScene = new Scene(addProductParent);
        Stage addProductStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        addProductStage.setScene(addProductScene);
        addProductStage.show();
    }

    @FXML
    private void openModifyProductScreen(ActionEvent event) throws IOException {
        modifyProduct = tvProducts.getSelectionModel().getSelectedItem();
        modifyProductIndex = getProductInv().indexOf(modifyProduct);
        Parent modifyProductParent = FXMLLoader.load(getClass().getResource("/view/ModifyProduct.fxml"));
        Scene modifyProductScene = new Scene(modifyProductParent);
        Stage modifyProductStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        modifyProductStage.setScene(modifyProductScene);
        modifyProductStage.show();
    }
    /**
     * Updates the table view to reflect the current list of parts.
     *
     * Future Enhancement: Implement a refresh button to manually update the TableView.
     */
    public void updatePartsTv() {
        tvParts.setItems(getPartInv());
    }
    /**
     * Updates the table view to reflect the current list of products.
     *
     * Future Enhancement: Implement pagination for products if the list becomes too long.
     */
    public void updateProdTv() {
        tvProducts.setItems(getProductInv());
    }
    /**
     * Exit the application.
     *
     * @param event The triggering event.
     */
    @FXML
    private void exitButton(ActionEvent event) {
        displayAlert(7);
    }
    /**
     * Displays various types of alerts based on the given alert type.
     *
     * @param alertType The type of the alert.
     * Future Enhancement: Create an enum for alert types for better readability.
     */
    private void displayAlert(int alertType) {
        Alert alert;
        String title = "";
        String content = "";

        switch (alertType) {
            case 1:
                alert = new Alert(Alert.AlertType.INFORMATION);
                title = "Warning";
                content = "The search term entered does not match any known parts.";
                break;
            case 2:
                alert = new Alert(Alert.AlertType.WARNING);
                title = "Warning";
                content = "Part is being used by one or more products.";
                break;
            case 3:
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                title = "Confirmation";
                content = "Are you sure you want to delete this part?";
                break;
            case 4:
                alert = new Alert(Alert.AlertType.INFORMATION);
                title = "Information";
                content = "The search term entered does not match any known products.";
                break;
            case 5:
                alert = new Alert(Alert.AlertType.WARNING);
                title = "Warning";
                content = "Product contains one or more parts.";
                break;
            case 6:
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                title = "Confirmation";
                content = "Are you sure you want to delete this product?";
                break;
            case 7:
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                title = "Confirmation";
                content = "Are you sure you want to exit?";
                break;
            default:
                return;
        }

        alert.setTitle(title);
        alert.setContentText(content);
        Optional<ButtonType> result = alert.showAndWait();
        if (alertType == 3 && result.isPresent() && result.get() == ButtonType.OK) {
            Part part = tvParts.getSelectionModel().getSelectedItem();
            removePart(part);
            updatePartsTv();
        }
        if (alertType == 6 && result.isPresent() && result.get() == ButtonType.OK) {
            Product product = tvProducts.getSelectionModel().getSelectedItem();
            removeProd(product);
            updateProdTv();
        }
        if (alertType == 7 && result.isPresent() && result.get() == ButtonType.OK) {
            System.exit(0);
        }
    }

    /**
     * Initialize method, sets up the table columns and updates the table view.
     *
     * @param url The location used to resolve relative paths for the root object.
     * @param rb The resources used to localize the root object.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tvPartsIDColumn.setCellValueFactory(cellData -> cellData.getValue().partIdProp().asObject());
        tvPartsNameColumn.setCellValueFactory(cellData -> cellData.getValue().partNameProp());
        tvPartsInvColumn.setCellValueFactory(cellData -> cellData.getValue().partInvProp().asObject());
        tvPartsPriceColumn.setCellValueFactory(cellData -> cellData.getValue().partPriceProp().asObject());
        tvProductsIDColumn.setCellValueFactory(cellData -> cellData.getValue().productIDProperty().asObject());
        tvProductsNameColumn.setCellValueFactory(cellData -> cellData.getValue().productNameProperty());
        tvProductsInvColumn.setCellValueFactory(cellData -> cellData.getValue().productInvProperty().asObject());
        tvProductsPriceColumn.setCellValueFactory(cellData -> cellData.getValue().productPriceProperty().asObject());
        updatePartsTv();
        updateProdTv();
    }
}