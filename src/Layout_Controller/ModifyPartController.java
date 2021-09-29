package Layout_Controller;

import Objects.Inventory;
import Objects.InHousePart;
import Objects.OutsourcedPart;
import Objects.Part;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Optional;

/**
 * The controller methods for modifying parts.
 **/
public class ModifyPartController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    Stage stage;
    Parent scene;
    @FXML
    private RadioButton modPartInHouse;
    @FXML
    private RadioButton modPartOutsourced;
    @FXML
    private ToggleGroup partSource;
    @FXML
    private Label modPartVariableName;
    @FXML
    private TextField partIdField;
    @FXML
    private TextField modPartVariableField;
    @FXML
    private TextField partPriceField;
    @FXML
    private TextField partStockField;
    @FXML
    private TextField partNameField;
    @FXML
    private TextField partMaxField;
    @FXML
    private TextField partMinField;

    /**
     * @param event section modifies the part through the user interface with InHousePart.
     **/
    @FXML
    public void onActionModPartIn(ActionEvent event) {
        modPartVariableName.setText("Machine ID:");
    }

    /**
     * @param event modifies the part through the user interface with OutsourcedPart.
     **/
    @FXML
    public void onActionModPartOut(ActionEvent event) {
        modPartVariableName.setText("Company Name:");
    }

    /**
     * @param event returns to the main screen from the modify user interface.
     * @throws java.io.IOException exception to saved changes
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
     * Validation: Min should be less than Max; and Inv should be between those two values.
     *
     * @param event save part modification from the user interface.
     * @throws java.io.IOException exception thrown from user input.
     **/
    @FXML
    public void onActionSave(ActionEvent event) throws IOException {

        if (Integer.parseInt(partStockField.getText()) >= Integer.parseInt(partMaxField.getText()) || Integer.parseInt(partStockField.getText()) <= Integer.parseInt(partMinField.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Min should be less than Max; and Inv should be between those two values.");
            alert.showAndWait();
        } else {

            int id = Integer.parseInt(partIdField.getText());
            String name = partNameField.getText();
            int stock = Integer.parseInt(partStockField.getText());
            double price = Double.parseDouble(partPriceField.getText());
            int min = Integer.parseInt(partMinField.getText());
            int max = Integer.parseInt(partMaxField.getText());

            for (Part part : Inventory.getAllParts()) {

                if (part.getId() != id) {
                    continue;
                }

                int partIndex = Inventory.getAllParts().indexOf(part);

                if (!modPartInHouse.isSelected()) {
                    if (!(part instanceof OutsourcedPart)) {
                        Part outSrcPart = new OutsourcedPart(id, name, price, stock, min, max, modPartVariableField.getText());
                        Inventory.updatePart(partIndex, outSrcPart);
                    } else {
                        part.setName(name);
                        part.setStock(stock);
                        part.setPrice(price);
                        part.setMax(max);
                        part.setMin(min);

                        ((OutsourcedPart) part).setCompanyName(modPartVariableField.getText());
                    }
                } else {
                    if (part instanceof InHousePart) {
                        part.setName(name);
                        part.setStock(stock);
                        part.setPrice(price);
                        part.setMax(max);
                        part.setMin(min);

                        ((InHousePart) part).setMachineId(Integer.parseInt(modPartVariableField.getText()));
                    } else {
                        Part inHousePart = new InHousePart(id, name, price, stock, min, max, Integer.parseInt(modPartVariableField.getText()));
                        Inventory.updatePart(partIndex, inHousePart);
                    }
                }
                break;
            }
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Layout_Controller/MainScreen.fxml")));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /**
     * @param part sends a through the user interface of "part" to other windows.
     **/
    public void sendPartInfo(Part part) {

        if (!(part instanceof InHousePart)) {
            modPartOutsourced.setSelected(true);
            modPartVariableName.setText("Company Name:");
            modPartVariableField.setText(((OutsourcedPart) part).getCompanyName());
        } else {
            modPartInHouse.setSelected(true);
            modPartVariableName.setText("Machine ID:");
            modPartVariableField.setText(String.valueOf(((InHousePart) part).getMachineId()));
        }

        partIdField.setText(String.valueOf(part.getId()));
        partNameField.setText(part.getName());
        partStockField.setText(String.valueOf(part.getStock()));
        partPriceField.setText(String.valueOf(part.getPrice()));
        partMaxField.setText(String.valueOf(part.getMax()));
        partMinField.setText(String.valueOf(part.getMin()));
    }
}
