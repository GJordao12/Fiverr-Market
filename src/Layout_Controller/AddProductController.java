package Layout_Controller;

import Objects.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Optional;

/**
 * Section contains methods to add products.
 **/
public class AddProductController implements Initializable {

    Stage stage;
    Parent scene;
    ObservableList<Part> tempAssociatedPartsList = FXCollections.observableArrayList();

    @FXML
    private TextField addProductId;
    @FXML
    private TextField addProductName;
    @FXML
    private TextField addProductInventory;
    @FXML
    private TextField addProductPrice;
    @FXML
    private TextField addProductMax;
    @FXML
    private TextField addProductMin;
    @FXML
    private TextField addProductPartSearchField;
    @FXML
    private TableView<Part> partTableView;
    @FXML
    private TableView<Part> associatedPartsTableView;
    @FXML
    private TableColumn<Product, Integer> inventoryPartID;
    @FXML
    private TableColumn<Product, String> inventoryPartName;
    @FXML
    private TableColumn<Product, Integer> inventoryStockLevel;
    @FXML
    private TableColumn<Product, Double> inventoryPrice;
    @FXML
    private TableColumn<Product, Integer> associatedPartId;
    @FXML
    private TableColumn<Product, String> associatedPartName;
    @FXML
    private TableColumn<Product, Integer> associatedStockLevel;
    @FXML
    private TableColumn<Product, Double> associatedPrice;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // Sets the Parts table view
        partTableView.setItems(Inventory.getAllParts());

        // Sets the associated parts table view
        associatedPartsTableView.setItems(tempAssociatedPartsList);

        // Fills the Parts column with values
        inventoryPartID.setCellValueFactory(new PropertyValueFactory<>("id"));
        inventoryPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        inventoryStockLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
        inventoryPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        //Fills the associated parts column with values
        associatedPartId.setCellValueFactory(new PropertyValueFactory<>("id"));
        associatedPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        associatedStockLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
        associatedPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * @param event adds a part through the user interface
     **/
    @FXML
    public void onActionAddPart(ActionEvent event) {
        tempAssociatedPartsList.add(partTableView.getSelectionModel().getSelectedItem());
    }

    /**
     * @param event deletes a part through the product user interface.
     *              The application confirms the Delete and Remove actions.
     **/
    @FXML
    public void onActionDeletePart(ActionEvent event) {
        if (associatedPartsTableView.getSelectionModel().getSelectedItem() != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Product will be deleted, do you want to continue?");
            alert.setTitle("CONFIRMATION");
            Optional<ButtonType> result = alert.showAndWait();

            tempAssociatedPartsList.remove(associatedPartsTableView.getSelectionModel().getSelectedItem());

        } else {
            if (associatedPartsTableView.getSelectionModel().getSelectedItem() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "No Product is selected.");
                alert.setTitle("ERROR");

                Optional<ButtonType> result = alert.showAndWait();

                if (result.isEmpty() || result.get() != ButtonType.OK) {
                    return;
                }
            }
        }
    }

    /**
     * @param event returns to the main Screen through the product user interface
     * @throws java.io.IOException exception for confirmation from user.
     */
    @FXML
    public void onActionReturnToMainScreen(ActionEvent event) throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Changes will not be saved, do you want to continue?");
        alert.setTitle("CONFIRMATION");

        Optional<ButtonType> result = alert.showAndWait();

        if (!result.isPresent() || result.get() != ButtonType.OK) {
            return;
        }
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Layout_Controller/MainScreen.fxml")));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * @param event Saves through product user interface
     *              Validation: acceptable Inventory Quantity and fields
     *              Min should be less than Max along with inventory being in between both values.
     *              The application will not crash when inappropriate user data is entered in the forms;
     *              instead, error messages should be generated.
     * @throws java.io.IOException unable to save if no fields are filled.
     */
    @FXML
    public void onActionSave(ActionEvent event) throws IOException {
        try {
            int id = Inventory.getAllProducts().size() + 1;
            String name = addProductName.getText();
            double price = Double.parseDouble(addProductPrice.getText());
            int stock = Integer.parseInt(addProductInventory.getText());
            int min = Integer.parseInt(addProductMin.getText());
            int max = Integer.parseInt(addProductMax.getText());

            if (stock >= max || stock <= min) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setContentText("Min should be less than Max; and Inv should be between those two values.");
                alert.showAndWait();
            } else {
                Product p = new Product(id, name, price, stock, min, max);
                Inventory.addProduct(p);
                for (int i = 0; i < tempAssociatedPartsList.size(); i++) {
                    p.addAssociatedPart(tempAssociatedPartsList.get(i));
                }

                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Layout_Controller/MainScreen.fxml")));
                stage.setScene(new Scene(scene));
                stage.show();
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Fill out fields to save");
            alert.showAndWait();
        }
    }

    /**
     * @param event search for a part through the user interface of product.
     *              NumberFormatException if part is not found.
     **/
    @FXML
    public void onActionSearchProductPart(ActionEvent event) {

        int partId;
        String partInput = addProductPartSearchField.getText().trim();
        ObservableList<Part> searchResult = FXCollections.observableArrayList();
        ObservableList<Part> search;

        if (partInput.isEmpty()) {
            partTableView.setItems(Inventory.getAllParts());
        } else {

            try {
                partId = Integer.parseInt(partInput);
                searchResult.add(Inventory.lookupPart(partId));
            } catch (NumberFormatException e) {
                search = Inventory.getPartsByName(partInput);
                for (int i = 0; i < search.toArray().length; i++) {
                    searchResult.add(Inventory.lookupPart(search.get(i).getId()));
                }

            }

            if (searchResult.toArray().length == 0 || searchResult.get(0) == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setContentText("Part not found in search, please enter part");
                alert.showAndWait();
                partTableView.setItems(Inventory.getAllParts());
            } else {
                partTableView.setItems(searchResult);
            }
        }
    }
}
