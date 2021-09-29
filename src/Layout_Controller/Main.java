package Layout_Controller;

import Objects.*;

import javafx.fxml.FXMLLoader;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.util.Objects;

/**
 * The main class that opens the application.
 **/
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Layout_Controller/MainScreen.fxml")));
        primaryStage.setTitle("Inventory Management System");
        primaryStage.setScene(new Scene(root, 1150, 450));
        primaryStage.show();
    }

    /**
     * @param args section adds sample data to Part and Product.
     **/
    public static void main(String[] args) {
        Part part1 = new InHousePart(1, "Wrench", 8.99, 5, 2, 6, 1);
        Part part2 = new OutsourcedPart(2, "Screw Driver", 6.99, 8, 3, 22, "Home Depot");
        Part part3 = new InHousePart(3, "Saw", 14.99, 12, 9, 18, 2);
        Part part4 = new OutsourcedPart(4, "Tape", 2.99, 16, 4, 29, "Lowes");

        Inventory.addPart(part1);
        Inventory.addPart(part2);
        Inventory.addPart(part3);
        Inventory.addPart(part4);

        Product product1 = new Product(1, "Tool Belt", 25.99, 4, 1, 8);
        Product product2 = new Product(2, "Hammer", 9.99, 13, 8, 17);
        Product product3 = new Product(3, "Tool Box", 45.99, 12, 7, 25);
        Product product4 = new Product(4, "Paint", 19.99, 10, 8, 31);

        Inventory.addProduct(product1);
        Inventory.addProduct(product2);
        Inventory.addProduct(product3);
        Inventory.addProduct(product4);

        launch(args);
    }
}
