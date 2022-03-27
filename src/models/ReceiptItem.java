package models;

public class ReceiptItem {
    public Product product;
    public int selectedQty;
    public double totalPrice;

    public ReceiptItem(Product product, int selectedQty, double totalPrice) {
        this.product = product;
        this.selectedQty = selectedQty;
        this.totalPrice = totalPrice;
    }
}
