import exceptions.DuplicateInventoryException;
import exceptions.InventoryDepletedException;
import exceptions.InventoryNotFoundException;
import models.InventoryItem;
import models.Product;
import models.ReceiptItem;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InventorySystem {

    private static final DecimalFormat df = new DecimalFormat("0.00");

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
        System.out.println("=================================================================");
    }

    public boolean isInvalidRole(String input) {
        if (input.equals("1")) {
            currentUser = 1;
            return false;
        } else if (input.equals("2")) {
            currentUser = 2;
            return false;
        } else {
            System.out.println("Invalid role, please try again!");
            return true;
        }
    }

    public void addInventoryItem(int code, String name, double price, int quantity) throws DuplicateInventoryException {
        InventoryItem temp = getInventoryItemByCode(code);
        if (temp != null) {
            throw new DuplicateInventoryException();
        } else {
            inventoryItems.add(new InventoryItem(new Product(code, name, price), quantity));
            System.out.println("Item added successfully!");
        }
    }

    public InventoryItem getInventoryItemByCode(int code) {
        for (InventoryItem x : inventoryItems) {
            if (x.product.code == code) {
                return x;
            }
        }
        return null;
    }

    public void modifyInventoryItem(int code, Map<String, String> changes) throws InventoryNotFoundException {
        if (changes.isEmpty()) {
            System.out.println("No changes to be made!");
        } else {
            InventoryItem temp = getInventoryItemByCode(code);
            if (temp == null) {
                throw new InventoryNotFoundException();
            } else {
                int index = inventoryItems.indexOf(temp);
                if (changes.containsKey("name")) {
                    temp.product.name = changes.get("name");
                } else if (changes.containsKey("price")) {
                    temp.product.price = Double.parseDouble(changes.get("price"));
                } else if (changes.containsKey("quantity")) {
                    temp.quantity = Integer.parseInt(changes.get("quantity"));
                }
                inventoryItems.set(index, temp);
                System.out.println("Item modified successfully!");
            }
        }
    }

    public void removeInventoryItem(int code) throws InventoryNotFoundException {
        InventoryItem temp = getInventoryItemByCode(code);
        if (temp == null) {
            throw new InventoryNotFoundException();
        } else {
            inventoryItems.removeIf(item -> item.product.code == code);
            System.out.println("Item removed successfully!");
        }
    }

    public boolean hasMoreItems(String proceed) {
        return proceed.charAt(0) == 'Y' || proceed.charAt(0) == 'y';
    }

    public void addToCart(int code, int quantity) throws InventoryDepletedException, InventoryNotFoundException {
        InventoryItem temp = getInventoryItemByCode(code);
        if (temp == null) {
            throw new InventoryNotFoundException();
        } else {
            if (temp.quantity - quantity < 0) {
                throw new InventoryDepletedException();
            } else {
                temp.quantity = temp.quantity - quantity;
                double totalPrice = Double.parseDouble(df.format(temp.product.price * quantity));
                ReceiptItem receiptItem = new ReceiptItem(temp.product, quantity, totalPrice);
                receiptItems.add(receiptItem);
                displayReceiptItem(receiptItem);
                System.out.println("Added to Cart!");
            }
        }
    }

    public void displayReceiptItem(ReceiptItem receiptItem) {
        System.out.println(receiptItem.product.code + "\t\t\t" + receiptItem.product.name + "\t\t\t" + receiptItem.selectedQty + "\t\t\t\t" + receiptItem.totalPrice);
    }

    public void displayShoppingCart() {
        if (receiptItems.size() == 0) {
            System.out.println("Your shopping cart is empty!");
        } else {
            double total = 0.0;
            System.out.println("=======================================================");
            System.out.println("Item Code\t\tItem Name\t\tQuantity\t\tTotal");
            System.out.println("=======================================================");
            for (ReceiptItem x : receiptItems) {
                displayReceiptItem(x);
                total += x.totalPrice;
            }
            System.out.println("=======================================================");
            System.out.println("TOTAL: \t\t\t\t\t\t\t\t\t\t\t" + total);
            System.out.println("=======================================================");
        }
    }
}
