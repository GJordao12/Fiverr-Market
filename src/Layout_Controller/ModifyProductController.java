package Layout_Controller;

import Objects.Inventory;
import Objects.Product;
import Objects.Part;

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

import static java.lang.Integer.parseInt;

/**
 * Section contains the controller methods for the modification of products.
 **/
public class ModifyProductController implements Initializable {

    Stage stage;
    Parent scene;
    public ObservableList<Part> modifiedAssociatedParts = FXCollections.observableArrayList();

    @FXML
    private TextField productIdField;
    @FXML
    private TextField productNameField;
    @FXML
    private TextField productStockField;
    @FXML
    private TextField productPriceField;
    @FXML
    private TextField productMaxField;
    @FXML
    private TextField productMinField;
    @FXML
    private TextField partSearchField;
    @FXML
    private TableView<Part> partTableView;
    @FXML
    private TableView<Part> associatedPartsTableView;
    @FXML
    private TableColumn<Product, Integer> inventoryPartId;
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

    public Product p;
    public int idToSave;

    public void sendProductInfo(Product product) {

        // Sets the product info fields.
        productIdField.setText(Integer.toString(product.getId()));
        idToSave = product.getId();
        productNameField.setText(product.getName());
        productStockField.setText(Integer.toString(product.getStock()));
        productPriceField.setText(Double.toString(product.getPrice()));
        productMaxField.setText(Integer.toString(product.getMax()));
        productMinField.setText(Integer.toString(product.getMin()));

        // Sets the associated parts table view.
        associatedPartsTableView.setItems(modifiedAssociatedParts);

        // Fills the associated parts column.
        associatedPartId.setCellValueFactory(new PropertyValueFactory<>("id"));
        associatedPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        associatedStockLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
        associatedPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        partTableView.setItems(Inventory.getAllParts());
        p = Inventory.getOneProductById(idToSave);
        if (p != null && (!(p.getAllAssociatedParts().toArray().length == 0))) {
            for (int i = 0; i < p.getAllAssociatedParts().toArray().length; i++) {
                modifiedAssociatedParts.add(p.getAllAssociatedParts().get(i));
            }
        }
    }

    /**
     * Populates tables and columns with values.
     **/
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Fills the Part column with values
        inventoryPartId.setCellValueFactory(new PropertyValueFactory<>("id"));
        inventoryPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        inventoryStockLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
        inventoryPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * @param event add a part through the modify product user interface.
     **/
    @FXML
    public void onActionAddPart(ActionEvent event) {
        modifiedAssociatedParts.add(partTableView.getSelectionModel().getSelectedItem());
    }

    /**
     * @param event deletes a part through the modify product user interface.
     * The application confirms the Delete and Remove actions.
     **/
    @FXML
    public void onActionDeletePart(ActionEvent event) {

        if (associatedPartsTableView.getSelectionModel().getSelectedItem() != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will remove the part from the product, do you want to continue?");
            alert.setTitle("CONFIRMATION");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                modifiedAssociatedParts.removeAll(associatedPartsTableView.getSelectionModel().getSelectedItem());
            }
        }
    }

    /**
     * @param event returns to the main screen through the product uder interface.
     * @throws IOException NullPointerException: Changes will not saved, click through for continuation.
     **/
    @FXML
    public void onActionReturnToMainScreen(ActionEvent event) throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Changes wont be saved, do you want to continue?");
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
     * @param event save part from the user interface of the ModifyPart.
     * Validation: The application confirms the Delete and Remove actions.
     * @throws java.io.IOException exception thrown for error min/max values
     **/
    @FXML
    public void onActionSave(ActionEvent event) throws IOException {

        if (parseInt(productStockField.getText()) >= parseInt(productMaxField.getText()) || parseInt(productStockField.getText()) <= parseInt(productMinField.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Min should be less than Max; and Inv should be between those two values.");
            alert.showAndWait();
        } else {

            int id = parseInt(productIdField.getText());
            String name = productNameField.getText();
            int stock = parseInt(productStockField.getText());
            double price = Double.parseDouble(productPriceField.getText());
            int min = parseInt(productMinField.getText());
            int max = parseInt(productMaxField.getText());

            if (p != null) {
                p.setId(id);
                p.setName(name);
                p.setStock(stock);
                p.setPrice(price);
                p.setMin(min);
                p.setMax(max);
            }

            assert p != null;
            p.clearParts();
            for (int i = 0; i < modifiedAssociatedParts.size(); i++) {
                p.addAssociatedPart(modifiedAssociatedParts.get(i));
            }
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Layout_Controller/MainScreen.fxml")));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /**
     * Search through the modify product user interface. Checks for exception if the field is empty or part is missing.
     * The application will not crash when inappropriate user data is entered in the forms; instead, error messages should be generated.
     * @param event if part input is not valid.
     **/
    @FXML
    public void onActionSearchPart(ActionEvent event) {

        int partId;
        String partInput = partSearchField.getText().trim();
        ObservableList<Part> searchResult = FXCollections.observableArrayList();
        ObservableList<Part> search;

        if (partInput.isEmpty()) {
            partTableView.setItems(Inventory.getAllParts());
        } else {

            try {
                partId = parseInt(partInput);
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
