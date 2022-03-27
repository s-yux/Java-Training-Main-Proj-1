package models;

public class InventoryItem {

    public Product product;
    public int quantity;

    public InventoryItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }
}
