import exceptions.InventoryDepletedException;
import models.InventoryItem;
import models.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Runner {

    public static void main(String[] args) {

        List<InventoryItem> inventoryItems = new ArrayList<>();
        inventoryItems.add(new InventoryItem(new Product(1000, "Adapter", 23.90), 1000));
        inventoryItems.add(new InventoryItem(new Product(1001, "Laptop", 999.90), 1000));
        inventoryItems.add(new InventoryItem(new Product(1002, "Mouse", 50.90), 1000));
        inventoryItems.add(new InventoryItem(new Product(1003, "Camera", 200.00), 1000));
        inventoryItems.add(new InventoryItem(new Product(1004, "Charger", 10.5), 1000));

        InventorySystem inventorySystem = new InventorySystem(inventoryItems);
        Scanner sc = new Scanner(System.in);
        int role, option;
        int code, quantity;
        String name;
        double price;
        char proceed;

        do {
            do {
                System.out.println("Welcome to XYZ Stock Inventory Management System!");
                System.out.println("Please select your role:");
                System.out.println("(1) Customer");
                System.out.println("(2) Staff");
                role = sc.nextInt();
            } while (!inventorySystem.isValidRole(role));

            if (inventorySystem.currentUser == 1) {
                do {
                    System.out.println("Select an option");
                    System.out.println("(1) View Inventory");
                    System.out.println("(2) Purchase InventoryItem");
                    System.out.println("(3) View Shopping Cart");
                    System.out.println("(4) Exit");

                    option = sc.nextInt();

                    switch (option) {
                        case 1:
                            inventorySystem.displayInventory();
                            break;
                        case 2:
                            do {
                                System.out.println("Enter Item Code:");
                                code = sc.nextInt();
                                System.out.println("Enter quantity:");
                                quantity = sc.nextInt();
                                try {
                                    inventorySystem.addToCart(code, quantity);
                                } catch (InventoryDepletedException e) {
                                    System.out.println(e.getMessage());
                                } finally {
                                    System.out.println("Do you want to continue shopping? (y/n)");
                                    proceed = sc.next().toUpperCase().charAt(0);
                                }
                            } while (inventorySystem.hasMoreItems(proceed));
                            break;
                        case 3:
                            inventorySystem.displayShoppingCart();
                            break;
                        case 4:
                            System.out.println("System Terminating...");
                            System.out.println("Done! Thank You and have a nice day!");
                            System.exit(0);
                            break;
                        default:
                            System.out.println("Invalid Option!");
                            break;
                    }
                } while (inventorySystem.isValidOption(option));

            } else if (role == 2) {
                do {
                    System.out.println("Select an option");
                    System.out.println("(1) View Inventory");
                    System.out.println("(2) Add InventoryItem");
                    System.out.println("(3) Modify InventoryItem");
                    System.out.println("(4) Remove InventoryItem");
                    System.out.println("(5) Exit");

                    option = sc.nextInt();

                    switch (option) {
                        case 1:
                            inventorySystem.displayInventory();
                            break;
                        case 2:
                            System.out.println("Enter item code of product:");
                            code = sc.nextInt();
                            sc.nextLine();
                            System.out.println("Enter name of product:");
                            name = sc.nextLine();
                            System.out.println("Enter price of product:");
                            price = sc.nextDouble();
                            System.out.println("Enter quantiy of product");
                            quantity = sc.nextInt();
                            inventorySystem.addInventoryItem(code, name, price, quantity);
                            break;
                        case 3:
                            inventorySystem.modifyInventoryItem();
                            break;
                        case 4:
                            System.out.println("Enter Item Code of Product to remove:");
                            code = sc.nextInt();
                            inventorySystem.removeInventoryItem(code);
                            break;
                        case 5:
                            System.out.println("System Terminating...");
                            System.out.println("Done! Thank You and have a nice day!");
                            System.exit(0);
                            break;
                        default:
                            System.out.println("Invalid Option!");
                            break;
                    }
                } while (inventorySystem.isValidOption(option));
            }
        } while (true);
    }
}
