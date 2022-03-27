import exceptions.InventoryDepletedException;
import models.InventoryItem;
import models.Product;
import models.ReceiptItem;

import java.util.ArrayList;
import java.util.List;

public class InventorySystem {

    List<InventoryItem> inventoryItems;
    List<ReceiptItem> receiptItems = new ArrayList<>();
    int currentUser;

    InventorySystem(List<InventoryItem> inventoryItems) {
        this.inventoryItems = inventoryItems;
    }

    public void displayInventory() {
        System.out.println("=================================================================");
        System.out.println("Item Code\t\t\tItem Name\t\t\tItem Price\t\t\tQty");
        System.out.println("=================================================================");
        for (InventoryItem x : inventoryItems) {
            System.out.println(x.product.code + "\t\t\t\t" + x.product.name + "\t\t\t\t" + x.product.price + "\t\t\t\t" + x.quantity);
        }
    }

    public boolean isValidRole(int input) {
        if (input == 1) {
            currentUser = 1;
            return true;
        } else if (input == 2) {
            currentUser = 2;
            return true;
        } else {
            System.out.println("Invalid input, please try again!");
            return false;
        }
    }

    public boolean isValidOption(int input) {
        if (currentUser == 1 && (input > 0 && input < 4)) {
            return true;
        } else if (currentUser == 2 && (input > 0 && input < 5)) {
            return true;
        }
        return false;
    }

    public void addInventoryItem(int code, String name, double price, int quantity) {
        inventoryItems.add(new InventoryItem(new Product(code, name, price), quantity));
        System.out.println("Item added successfully!");
    }

    public void modifyInventoryItem() {
        //TODO: Logic for modifying Inventory Item (WIP)
    }

    public void removeInventoryItem(int code) {
        inventoryItems.removeIf(item -> item.product.code == code);
        System.out.println("Item removed successfully!");
    }

    public boolean hasMoreItems(char proceed) {
        return proceed == 'Y' || proceed == 'y';
    }

    public void addToCart(int code, int quantity) throws InventoryDepletedException {
        InventoryItem temp = new InventoryItem(null, 0);
        for (InventoryItem x : inventoryItems) {
            if (x.product.code == code) {
                temp = x;
            }
        }
        if (temp.quantity - quantity < 0) {
            throw new InventoryDepletedException();
        } else {
            //TODO: Update main inventoryItems to minus off the balance qty
            double totalprice = temp.product.price * quantity;
            ReceiptItem receiptItem = new ReceiptItem(temp.product, quantity, totalprice);
            receiptItems.add(receiptItem);
            displayReceiptItem(receiptItem);
            System.out.println("Added to Cart!");
        }
    }

    public void displayReceiptItem(ReceiptItem receiptItem) {
        System.out.println(receiptItem.product.code + "\t\t\t" + receiptItem.product.name + "\t\t\t" + receiptItem.selectedQty + "\t\t\t\t" + receiptItem.totalPrice);

    }

    public void displayShoppingCart() {
        double total = 0.0;
        System.out.println("=====================================================");
        System.out.println("Item Code\t\tItem Name\t\tQuantity\t\tTotal");
        System.out.println("=====================================================");
        for (ReceiptItem x : receiptItems) {
            displayReceiptItem(x);
            total += x.totalPrice;
        }
        System.out.println("=====================================================");
        System.out.println("TOTAL: \t\t\t\t\t\t\t\t\t\t\t" + total);
        System.out.println("=====================================================");
    }
}
