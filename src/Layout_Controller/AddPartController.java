package Layout_Controller;

import Objects.Inventory;
import Objects.OutsourcedPart;
import Objects.InHousePart;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Optional;

/**
 * This section contains methods to add parts.
 **/
public class AddPartController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    Stage stage;
    Parent scene;

    @FXML
    private RadioButton addPartInHouse;
    @FXML
    private RadioButton addPartOutsourced;
    @FXML
    private ToggleGroup partSource;
    @FXML
    private Label addPartVariableLabel;
    @FXML
    private TextField addPartID;
    @FXML
    private TextField addPartName;
    @FXML
    private TextField addPartInventory;
    @FXML
    private TextField addPartPrice;
    @FXML
    private TextField addPartMin;
    @FXML
    private TextField addPartMax;
    @FXML
    private TextField addPartVariableField;

    /**
     * @param event Machine ID set
     *              Adds Machine ID through the Part user interface with "InHousePart".
     *              Compatible feature: A feature that would extend functionality to the next version if I were to update
     *              this application would be a new tableview/button that flows from the addPartView/controller to
     *              a 3rd parties company inventory tableview/menu. This would enable this system to verify another companies stock for
     *              verification purposes.
     **/
    @FXML
    public void onActionAddPartIn(ActionEvent event) {
        addPartVariableLabel.setText("Machine ID:");

    }

    /**
     * * Adds Company Name through the part user interface with "OutsourcedPart".
     **/
    @FXML
    void onActionAddPartOut(ActionEvent event) {
        addPartVariableLabel.setText("Company Name:");

    }

    /**
     * * @param event return to main screen
     * * @throws java.io.IOException exception for user confirmation.
     * * This section returns the user to the main screen user interface.
     **/
    @FXML
    public void onActionReturnToMainScreen(ActionEvent event) throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Changes will not be saved, do you want to continue?");
        alert.setTitle("CONFIRMATION");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isEmpty() || result.get() != ButtonType.OK) {
            return;
        }

        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Layout_Controller/MainScreen.fxml")));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * * Below code section saves a part through the part user interface.
     * * @param event data save from user input.
     * * @throws java.io.IOException Validation;
     * * Error issued explaining Inventory value should be between Max and Min values.
     * * The application will not crash when inappropriate user data is entered in the forms;
     * * instead, error messages should be generated.
     **/
    @FXML
    public void onActionSave(ActionEvent event) throws IOException {
        try {
            int id = Inventory.getAllParts().size() + 1;
            String name = addPartName.getText();
            double price = Double.parseDouble(addPartPrice.getText());
            int stock = Integer.parseInt(addPartInventory.getText());
            int min = Integer.parseInt(addPartMin.getText());
            int max = Integer.parseInt(addPartMax.getText());

            if (stock >= max || stock <= min) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setContentText("Min should be less than Max; and Inv should be between those two values.");
                alert.showAndWait();
            } else {
                if (addPartInHouse.isSelected()) {
                    int machineId = Integer.parseInt(addPartVariableField.getText());
                    Inventory.addPart(new InHousePart(id, name, price, stock, min, max, machineId));
                } else {

                    String companyName = addPartVariableField.getText();
                    Inventory.addPart(new OutsourcedPart(id, name, price, stock, min, max, companyName));
                }

                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Layout_Controller/MainScreen.fxml")));
                stage.setScene(new Scene(scene));
                stage.show();
            }

        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Fields must have values in order to save.");
            alert.showAndWait();
        }
    }
}