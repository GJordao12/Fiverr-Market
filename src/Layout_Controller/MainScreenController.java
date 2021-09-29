package Layout_Controller;

import Objects.Inventory;
import Objects.Product;
import Objects.Part;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

import static Objects.Inventory.canDeleteProduct;

/**
 * The main user interface for Part and Product.
 **/
public class MainScreenController implements Initializable {

    Stage stage;
    Parent scene;

    @FXML
    private TextField partSearchField;
    @FXML
    private TextField productSearchField;
    @FXML
    private TableView<Part> partTableView;
    @FXML
    private TableColumn<Part, Integer> partIdCol;
    @FXML
    private TableColumn<Part, Integer> partInventoryCol;
    @FXML
    private TableColumn<Part, String> partNameCol;
    @FXML
    private TableColumn<Part, Double> partPriceCol;
    @FXML
    private TableView<Product> productTableView;
    @FXML
    private TableColumn<Product, Integer> productIdCol;
    @FXML
    private TableColumn<Product, String> productNameCol;
    @FXML
    private TableColumn<Product, Integer> productInventoryCol;
    @FXML
    private TableColumn<Product, Double> productPriceCol;

    /**
     * This section populates the columns and tables with values.
     **/
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // This line creates the table view with parts.
        partTableView.setItems(Inventory.getAllParts());

        // This line creates the table view through products.
        productTableView.setItems(Inventory.getAllProducts());

        // These lines add parts to columns through values
        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        // These lines add products to columns through values
        productIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * @param event adds a part through the User Interface.
     * @throws java.io.IOException new scene
     **/
    @FXML
    public void onActionAddPart(ActionEvent event) throws IOException {

        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Layout_Controller/AddPartView.fxml")));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * @param event adds a product through the User Interface.
     * @throws java.io.IOException get
     **/
    @FXML
    public void onActionAddProduct(ActionEvent event) throws IOException {

        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Layout_Controller/AddProductView.fxml")));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Deletes a part through the user interface.
     * The application confirms the Delete and Remove actions.
     **/
    @FXML
    void onActionDeletePart(ActionEvent event) {
        if (partTableView.getSelectionModel().getSelectedItem() != null) {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will delete the part, do you want to continue?");
            alert.setTitle("CONFIRMATION");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                Inventory.deletePart(partTableView.getSelectionModel().getSelectedItem());
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "No part is selected");
            alert.setTitle("INFORMATION");
            alert.showAndWait();
        }
    }

    /**
     * @param event deletes a product through the user interface.
     *              The application confirms the Delete and Remove actions.
     *              The user should not delete a product that has a part associated with it.
     *              Validation: NULLPointerException that part is not associated or that product has been selected
     *              The application will not crash when inappropriate user data is entered in the forms;
     *              instead, error messages should be generated.
     **/
    @FXML
    public void onActionDeleteProduct(ActionEvent event) {
        Product product = productTableView.getSelectionModel().getSelectedItem();
        try {
            if (!canDeleteProduct(product)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("ERROR!");
                alert.setHeaderText("This product cannot be removed");
                alert.setContentText("This product has parts associated with it. Please remove parts associated with the product and try again.");
                alert.showAndWait();
            } else {
                if (productTableView.getSelectionModel().getSelectedItem() == null)
                    System.out.println("No product is selected.");

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will delete the product, do you want to continue?");
                alert.setTitle("CONFIRMATION");

                Optional<ButtonType> result = alert.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.OK) {
                    Inventory.deleteProduct(productTableView.getSelectionModel().getSelectedItem());
                }
            }
        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("No product is selected.");
            alert.showAndWait();
        }
    }

    /**
     * @param event exits the program and asks the user to confirm exit.
     */
    @FXML
    public void onActionExit(ActionEvent event) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirm Exit");
        alert.setHeaderText("Confirm Exit");
        alert.setContentText("Are you sure you want to exit?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            System.exit(0);
        } else {
            System.out.println("You clicked cancel.");
        }
    }

    /**
     * @param event modifies the part through the user interface.
     * @throws java.io.IOException exception for new scene on part selection.
     */
    @FXML
    public void onActionModifyPart(ActionEvent event) throws IOException {

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/Layout_Controller/ModifyPartView.fxml"));
            loader.load();

            ModifyPartController modPartController = loader.getController();
            modPartController.sendPartInfo(partTableView.getSelectionModel().getSelectedItem());

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "No part selected to modify.");
            alert.setTitle("ERROR");
            alert.showAndWait();
        }
    }

    /**
     * @param event modifies the product through the user interface.
     * @throws java.io.IOException exception for no selection of product.
     **/
    @FXML
    public void onActionModifyProduct(ActionEvent event) throws IOException {

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/Layout_Controller/ModifyProductView.fxml"));
            loader.load();

            ModifyProductController modProdController = loader.getController();
            modProdController.sendProductInfo(productTableView.getSelectionModel().getSelectedItem());

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "No product selected to modify.");
            alert.setTitle("ERROR");
            alert.showAndWait();
        }
    }

    /**
     * @param event search a part through the user interface.
     * Validation: If a part exists or was entered in incorrectly.
     * Logical/runtime error correction: Unable to gettext from table for the PartsSearch,
     * I just add to correct the name to parts instead of using Products.
     **/
    @FXML
    public void onActionPartsSearch(ActionEvent event) {

        int partId;
        String partInput = partSearchField.getText().trim();
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

    /**
     * @param event search a product through the user interface.
     * NumberFormatException if part is not valid.
     * Validation: IF a product exists or was entered in incorrectly.
     */
    @FXML
    public void onActionProductsSearch(ActionEvent event) {

        int productId;
        String productInput = productSearchField.getText().trim();
        ObservableList<Product> searchResult = FXCollections.observableArrayList();
        ObservableList<Product> search;


        if (productInput.isEmpty()) {
            productTableView.setItems(Inventory.getAllProducts());
        } else {
            try {
                productId = Integer.parseInt(productInput);
                searchResult.add(Inventory.lookupProduct(productId));
            } catch (NumberFormatException e) {
                search = Inventory.getProductsByName(productInput);
                for (int i = 0; i < search.toArray().length; i++) {
                    searchResult.add(Inventory.lookupProduct(search.get(i).getId()));
                }

            }

            if (searchResult.toArray().length == 0 || searchResult.get(0) == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setContentText("Product not found in search, please enter product");
                alert.showAndWait();
                productTableView.setItems(Inventory.getAllProducts());
            } else {
                productTableView.setItems(searchResult);
            }
        }
    }
}
