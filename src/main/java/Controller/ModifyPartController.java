package Controller;

import Model.InHousePart;
import Model.Inventory;
import Model.OutsourcedPart;
import Model.Part;
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
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import static Model.Inventory.getPartInv;
import static Controller.MainScreenController.partToModifyIndex;
/**
 * Controller for modifying existing parts in the application.
 */
public class ModifyPartController implements Initializable {

    @FXML
    private Label lblModPartIDNumber, lblModPart;
    @FXML
    private TextField txtModPartName, txtModPartInv, txtModPartPrice, txtModPartMin, txtModPartMax, txtModPart;
    @FXML
    private RadioButton radioModPartInH, radioModPartOut;
    private ToggleGroup toggleGroup;
    private boolean isOutsourced;
    private final int partIndex = partToModifyIndex();
    private int partID;
    private String exceptionMessage = "";

    /**
     * Handles radio button selection and sets up the UI accordingly.
     *
     * @param event The ActionEvent object representing the radio button selection event.
     */
    @FXML
    void handleRadioSelection(ActionEvent event) {
        RadioButton selectedRadioButton = (RadioButton) toggleGroup.getSelectedToggle();
        if (selectedRadioButton == radioModPartInH) {
            handleInHouseSelection();
        } else if (selectedRadioButton == radioModPartOut) {
            handleOutsourcedSelection();
        }
    }

    /**
     * Configures the UI components when the InHouse radio button is selected.
     */
    private void handleInHouseSelection() {
        System.out.println("InHouse radio button selected");
        isOutsourced = false;
        lblModPart.setText("Machine ID");
        txtModPart.setText("");
        txtModPart.setPromptText("Machine ID");
    }
    /**
     * Configures the UI components when the Outsourced radio button is selected.
     */
    private void handleOutsourcedSelection() {
        System.out.println("Outsourced radio button selected");
        isOutsourced = true;
        lblModPart.setText("Company Name");
        txtModPart.setText("");
        txtModPart.setPromptText("Company Name");
    }
    /**
     * Creates and returns a Part object with the provided values.
     *
     * @param partName  The name of the part.
     * @param partInv   The inventory level of the part.
     * @param partPrice The price of the part.
     * @param partMin   The minimum inventory level of the part.
     * @param partMax   The maximum inventory level of the part.
     * @param partDyn   Dynamic field value representing either Machine ID or Company Name based on part type.
     * @return A Part object with the provided values.
     * @throws NumberFormatException If parsing of numeric values fails.
     */
    @FXML
    private Part createPart(String partName, String partInv, String partPrice, String partMin, String partMax, String partDyn) throws NumberFormatException {
        Part part;
        if (isOutsourced) {
            part = new OutsourcedPart();
            ((OutsourcedPart) part).setCompanyName(partDyn);
            System.out.println("OutsourcedPart");
        } else {
            part = new InHousePart();
            ((InHousePart) part).setMachineID(Integer.parseInt(partDyn));
            System.out.println("InHousePart");
        }
        part.setPartID(partID);
        part.setPartName(partName);
        part.setPartInStock(Integer.parseInt(partInv));
        part.setPartPrice(Double.parseDouble(partPrice));
        part.setPartMin(Integer.parseInt(partMin));
        part.setPartMax(Integer.parseInt(partMax));

        return part;
    }


    /**
     * Handles the saving of modifications to a part.
     *
     * @param event Triggered when the Save button is clicked.
     * @throws IOException If an I/O error occurs.
     */
    @FXML
    void modPartSave(ActionEvent event) throws IOException {
        String partName = txtModPartName.getText();
        String partInv = txtModPartInv.getText();
        String partPrice = txtModPartPrice.getText();
        String partMin = txtModPartMin.getText();
        String partMax = txtModPartMax.getText();
        String partDyn = txtModPart.getText();

        try {
            exceptionMessage = Part.isPartValid(partName, Integer.parseInt(partMin), Integer.parseInt(partMax), Integer.parseInt(partInv), Double.parseDouble(partPrice), isOutsourced, partDyn, exceptionMessage);
            if (exceptionMessage.length() > 0) {
                displayAlert(1, exceptionMessage);
                exceptionMessage = "";
            } else {
                Part part = createPart(partName, partInv, partPrice, partMin, partMax, partDyn);
                Inventory.updatePart(partIndex, part);

                Parent modifyProductSave = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/MainScreen.fxml")));
                Scene scene = new Scene(modifyProductSave);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            }
        } catch (NumberFormatException e) {
            System.err.println("Caught NumberFormatException: " + e.getMessage());
            displayAlert(2, "Invalid input: Please ensure all fields are correctly filled out.");
        }
    }

    /**
     * Handles the cancellation of part modification.
     *
     * @param event Triggered when the Cancel button is clicked.
     * @throws IOException If an I/O error occurs.
     */
    @FXML
    private void modifyPartCancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        displayAlert(3, "");
        Optional<ButtonType> result = alert.showAndWait();


        if (result.get() == ButtonType.OK) {
            Parent modifyPartCancel = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
            Scene scene = new Scene(modifyPartCancel);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } else {
            System.out.println("Cancelled.");
        }
    }

    /**
     * Helper method to display alerts.
     *
     * @param alertType        The type of alert to display.
     * @param exceptionMessage The exception message to display.
     */
    private void displayAlert(int alertType, String exceptionMessage) {
        Alert alert = (alertType <= 2) ? new Alert(Alert.AlertType.INFORMATION) : new Alert(Alert.AlertType.CONFIRMATION);
        switch (alertType) {
            case 1:
                alert.setHeaderText("Error Modifying Part");
                alert.setContentText(exceptionMessage);
                break;
            case 2:
                alert.setContentText("Form contains blank fields.");
                break;
            case 3:
                alert.setContentText("Are you sure you want to cancel modifying the part?");
                break;
        }
        alert.showAndWait();
    }

    /**
     * Initializes the controller after its root element has been completely processed.
     *
     * @param url The location to resolve relative paths for the root object.
     * @param rb  The resources used to localize the root object.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        toggleGroup = new ToggleGroup();
        radioModPartInH.setToggleGroup(toggleGroup);
        radioModPartOut.setToggleGroup(toggleGroup);
        Part part = getPartInv().get(partIndex);
        partID = getPartInv().get(partIndex).getPartID();
        lblModPartIDNumber.setText("Auto-Gen: " + partID);
        txtModPartName.setText(part.getPartName());
        txtModPartInv.setText(Integer.toString(part.getPartInStock()));
        txtModPartPrice.setText(Double.toString(part.getPartPrice()));
        txtModPartMin.setText(Integer.toString(part.getPartMin()));
        txtModPartMax.setText(Integer.toString(part.getPartMax()));
        if (part instanceof InHousePart) {
            lblModPart.setText("Machine ID");
            txtModPart.setText(Integer.toString(((InHousePart) getPartInv().get(partIndex)).getMachineID()));
            radioModPartInH.setSelected(true);
            isOutsourced = false;
        } else {
            lblModPart.setText("Company Name");
            txtModPart.setText(((OutsourcedPart) getPartInv().get(partIndex)).getCompanyName());
            radioModPartOut.setSelected(true);
            isOutsourced = true;
        }
    }
}