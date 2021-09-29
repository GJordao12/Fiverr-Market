package Objects;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

/**
 * * This creates setters and getters for product.
 **/
public class Product {

    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    private int id;
    private int stock;
    private int min;
    private int max;
    private double price;
    private String name;

    public Product(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public void addAssociatedPart(Part part) {
        this.associatedParts.add(part);
    }

    public void clearParts() {
        this.associatedParts = FXCollections.observableArrayList();
    }

    public void deleteAllAssociatedParts() {
        associatedParts = FXCollections.observableArrayList();
    }

    public int getAssociatedPartsCount() {
        return associatedParts.size();
    }

    public boolean deleteAssociatedParts(int partID) {
        for (Part i : associatedParts) {
            if (i.getId() == partID) {
                associatedParts.remove(i);
                return true;
            }
        }
        return false;
    }

    public ObservableList<Part> getAllAssociatedParts() {
        return this.associatedParts;
    }
}