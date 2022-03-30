import exceptions.DuplicateInventoryException;
import exceptions.InventoryDepletedException;
import exceptions.InventoryNotFoundException;
import models.InventoryItem;
import models.Product;

import java.util.*;

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
        String role, option;
        int code, quantity;
        String name, proceed;
        double price;
        boolean isLoggedIn;
        Map<String, String> changes = new HashMap<>();

        do {
            do {
                System.out.println("Welcome to XYZ Stock Inventory Management System!");
                System.out.println("Please select your role:");
                System.out.println("(1) Customer");
                System.out.println("(2) Staff");
                role = sc.nextLine();
            } while (!inventorySystem.isValidRole(role));

            if (inventorySystem.currentUser == 1) {
                isLoggedIn = true;
                do {
                    System.out.println("Select an option");
                    System.out.println("(1) View Inventory");
                    System.out.println("(2) Purchase InventoryItem");
                    System.out.println("(3) View Shopping Cart");
                    System.out.println("(4) Exit");

                    option = sc.nextLine();

                    switch (option) {
                        case "1":
                            inventorySystem.displayInventory();
                            break;
                        case "2":
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
                                    sc.nextLine();
                                    System.out.println("Do you want to continue shopping? (y/n)");
                                    proceed = sc.nextLine();
                                }
                            } while (inventorySystem.hasMoreItems(proceed));
                            break;
                        case "3":
                            inventorySystem.displayShoppingCart();
                            break;
                        case "4":
                            isLoggedIn = false;
                            System.out.println("Thank You and have a nice day!");
                            System.exit(0);
                            break;
                        default:
                            System.out.println("Invalid Option!");
                            break;
                    }
                } while (!inventorySystem.isValidOption(option) || isLoggedIn);

            } else if (role.equals("2")) {
                isLoggedIn = true;
                do {
                    System.out.println("Select an option");
                    System.out.println("(1) View Inventory");
                    System.out.println("(2) Add InventoryItem");
                    System.out.println("(3) Modify InventoryItem");
                    System.out.println("(4) Remove InventoryItem");
                    System.out.println("(5) Exit");

                    option = sc.nextLine();

                    switch (option) {
                        case "1":
                            inventorySystem.displayInventory();
                            break;
                        case "2":
                            System.out.println("Enter item code of product:");
                            code = sc.nextInt();
                            sc.nextLine();
                            System.out.println("Enter name of product:");
                            name = sc.nextLine();
                            System.out.println("Enter price of product:");
                            price = sc.nextDouble();
                            System.out.println("Enter quantiy of product");
                            quantity = sc.nextInt();
                            try {
                                inventorySystem.addInventoryItem(code, name, price, quantity);
                            } catch (DuplicateInventoryException e) {
                                System.out.println(e.getMessage());
                            } finally {
                                sc.nextLine();
                            }
                            break;
                        case "3":
                            System.out.println("Enter item code of product:");
                            code = sc.nextInt();
                            sc.nextLine();
                            System.out.println("What do you want to modify?");
                            System.out.println("(1) Product Name");
                            System.out.println("(2) Product Price");
                            System.out.println("(3) Quantity");
                            option = sc.nextLine();

                            try {
                                switch (option) {
                                    case "1":
                                        System.out.println("Enter new Product Name:");
                                        name = sc.nextLine();
                                        changes.put("name", name);
                                        inventorySystem.modifyInventoryItem(code, changes);
                                        break;
                                    case "2":
                                        System.out.println("Enter new Product Price:");
                                        price = sc.nextDouble();
                                        changes.put("price", Double.toString(price));
                                        inventorySystem.modifyInventoryItem(code, changes);
                                        sc.nextLine();
                                        break;
                                    case "3":
                                        System.out.println("Enter new quantity:");
                                        quantity = sc.nextInt();
                                        changes.put("quantity", Integer.toString(quantity));
                                        inventorySystem.modifyInventoryItem(code, changes);
                                        sc.nextLine();
                                        break;
                                    default:
                                        System.out.println("No such field to modify!");
                                        break;
                                }
                            } catch (InventoryNotFoundException e) {
                                System.out.println(e.getMessage());
                            } finally {
                                changes.clear();
                            }
                            break;
                        case "4":
                            System.out.println("Enter Item Code of Product to remove:");
                            code = sc.nextInt();
                            try {
                                inventorySystem.removeInventoryItem(code);
                            } catch (InventoryNotFoundException e) {
                                System.out.println(e.getMessage());
                            } finally {
                                sc.nextLine();
                            }
                            break;
                        case "5":
                            isLoggedIn = false;
                            System.out.println("Thank You and have a nice day!");
                            System.exit(0);
                            break;
                        default:
                            System.out.println("Invalid Option!");
                            break;
                    }
                } while (!inventorySystem.isValidOption(option) || isLoggedIn);
            }
        } while (true);
    }
}
