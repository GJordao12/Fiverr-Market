package Objects;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * * This section contains logic for the add,search,update, and delete parts along with products.
 **/

public class Inventory {
    private static final ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static final ObservableList<Product> allProducts = FXCollections.observableArrayList();

    public static void addPart(Part part) {
        allParts.add(part);
    }

    public static void addProduct(Product product) {
        allProducts.add(product);
    }

    public static Part lookupPart(int partId) {
        return allParts.stream().filter(part -> part.getId() == partId).findFirst().orElse(null);

    }

    public static Product lookupProduct(int productId) {
        return allProducts.stream().filter(product -> product.getId() == productId).findFirst().orElse(null);

    }

    public static ObservableList<Part> lookupPart(String partName) {
        ObservableList<Part> filteredPartsResult = FXCollections.observableArrayList();
        allParts.stream().filter(part -> part.getName().toLowerCase().contains(partName.toLowerCase())).forEach(filteredPartsResult::add);
        return filteredPartsResult.isEmpty() ? allParts : filteredPartsResult;

    }

    public static ObservableList<Product> lookupProduct(String productName) {
        ObservableList<Product> filteredProductsResult = FXCollections.observableArrayList();
        allProducts.stream().filter(product -> product.getName().toLowerCase().contains(productName.toLowerCase())).forEach(filteredProductsResult::add);
        return filteredProductsResult.isEmpty() ? allProducts : filteredProductsResult;

    }

    public static void updatePart(int index, Part part) {
        allParts.set(index, part);
    }

    public static void updateProduct(int index, Product product) {
        allProducts.set(index, product);
    }

    public static void deletePart(Part part) {
        allParts.remove(part);
    }

    public static void deleteProduct(Product product) {
        allProducts.remove(product);
    }

    /**
     * @param product gets parts == 0.
     * @return product
     * Deletion of product without a part.
     **/
    public static boolean canDeleteProduct(Product product) {
        return product.getAssociatedPartsCount() == 0;
    }

    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }


    public static ObservableList<Product> getProductsByName(String productInput) {

        ObservableList<Product> search = FXCollections.observableArrayList();

        for (Product p : allProducts) {
            if (p.getName().startsWith(productInput)) {
                search.add(p);
            }
        }
        return search;
    }

    public static ObservableList<Part> getPartsByName(String partInput) {

        ObservableList<Part> search = FXCollections.observableArrayList();

        for (Part p : allParts) {
            if (p.getName().startsWith(partInput)) {
                search.add(p);
            }
        }
        return search;
    }

    public static Product getOneProductById(int productIdField) {

        for (Product p : allProducts) {
            if (p.getId() == productIdField) {
                return p;
            }
        }
        return null;
    }
}