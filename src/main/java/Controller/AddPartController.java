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
import java.util.Optional;
import java.util.ResourceBundle;
/**
 * Controller for adding new parts in the application.
 */
public class AddPartController implements Initializable {

    @FXML private Label lblAddPartIDNumber, lblAddPartDyn;
    @FXML private TextField txtAddPartName, txtAddPartInv, txtAddPartPrice, txtAddPartMin, txtAddPartMax, txtAddPartDyn;
    @FXML private RadioButton radioAddPartInHouse, radioAddPartOutsourced;
    /**
     * Boolean flag to indicate if the part is outsourced.
     */
    private boolean isOutsourced;
    /**
     * Auto-generated part ID.
     */
    private int partID;
    /**
     * To store exception messages during validation.
     */
    private String exceptionMessage = "";
    /**
     * Changes the UI to accept Machine ID when InHouse radio button is selected.
     * @param event Triggered on selecting the radio button.
     */
    @FXML
    void addPartInHouseRadio(ActionEvent event) {
        isOutsourced = false;
        lblAddPartDyn.setText("Machine ID");
        txtAddPartDyn.setPromptText("Machine ID");
        radioAddPartOutsourced.setSelected(false);
    }
    /**
     * Changes the UI to accept Company Name when Outsourced radio button is selected.
     * @param event Triggered on selecting the radio button.
     */
    @FXML
    void addPartOutsourcedRadio(ActionEvent event) {
        isOutsourced = true;
        lblAddPartDyn.setText("Company Name");
        txtAddPartDyn.setPromptText("Company Name");
        radioAddPartInHouse.setSelected(false);
    }
    /**
     * Creates a Part instance based on user input.
     * @param partName Name of the part.
     * @param partInv Inventory value of the part.
     * @param partPrice Price of the part.
     * @param partMin Minimum limit of the part.
     * @param partMax Maximum limit of the part.
     * @param partDyn Dynamic attribute of the part (Machine ID or Company Name).
     * @return A new Part instance.
     */
    private Part createPart(String partName, String partInv, String partPrice, String partMin, String partMax, String partDyn) {
        Part part;
        if (isOutsourced) {
            part = new OutsourcedPart();
            ((OutsourcedPart) part).setCompanyName(partDyn);
        } else {
            part = new InHousePart();
            ((InHousePart) part).setMachineID(Integer.parseInt(partDyn));
        }
        part.setPartID(partID);
        part.setPartName(partName);
        part.setPartPrice(Double.parseDouble(partPrice));
        part.setPartInStock(Integer.parseInt(partInv));
        part.setPartMin(Integer.parseInt(partMin));
        part.setPartMax(Integer.parseInt(partMax));

        return part;
    }
    /**
     * Handles the event when the Save button is clicked.
     * @param event Triggered on clicking the Save button.
     * @throws IOException If the main screen's FXML file isn't found.
     */
    @FXML
    void addPartSave(ActionEvent event) throws IOException {
        String partName = txtAddPartName.getText();
        String partInv = txtAddPartInv.getText();
        String partPrice = txtAddPartPrice.getText();
        String partMin = txtAddPartMin.getText();
        String partMax = txtAddPartMax.getText();
        String partDyn = txtAddPartDyn.getText();

        try {
            exceptionMessage = Part.isPartValid(partName, Integer.parseInt(partMin), Integer.parseInt(partMax), Integer.parseInt(partInv), Double.parseDouble(partPrice), isOutsourced, partDyn, exceptionMessage);
            if (exceptionMessage.length() > 0) {
                displayAlert("Error Adding Part", exceptionMessage);
                exceptionMessage = "";
            } else {
                Part part = createPart(partName, partInv, partPrice, partMin, partMax, partDyn);
                Inventory.addPart(part);

                loadMainScreen(event);
            }
        } catch (NumberFormatException e) {
            displayAlert("Form Error", "Form contains blank fields.");
        }
    }

    /**
     * Handles the event when the Cancel button is clicked.
     * Future Enhancement: This function could be enhanced by logging the user action for auditing purposes.
     *
     * @param event Triggered on clicking the Cancel button.
     * @throws IOException If the main screen's FXML file isn't found.
     */
    @FXML
    private void addPartCancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Are you sure you want to cancel adding a new part?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            loadMainScreen(event);
        } else {
            System.out.println("Cancelled.");
        }
    }
    /**
     * Loads the main screen.
     * Future Enhancement: This function can be enhanced to support dynamic loading of different screens,
     * making the navigation between different screens more flexible and maintainable.
     * @param event The action event triggering the load of the main screen.
     * @throws IOException If there is an error loading the FXML file for the main screen.
     */
    private void loadMainScreen(ActionEvent event) throws IOException {
        Parent mainScreen = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
        Scene scene = new Scene(mainScreen);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
    /**
     * Displays an alert dialog with the specified title and content.
     * Future Enhancement: This method can be enhanced by adding more customization options like alert type and buttons.
     * @param title The title of the alert dialog.
     * @param content The content text of the alert dialog.
     */
    private void displayAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Initializes the controller class and sets up default values.
     * @param url The location to resolve relative paths for the root object.
     * @param rb  The resources used to localize the root object.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        partID = Inventory.getPartIDCount();
        lblAddPartIDNumber.setText("Auto-Gen: " + partID);
    }
}