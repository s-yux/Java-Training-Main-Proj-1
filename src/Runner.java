import exceptions.DuplicateInventoryException;
import exceptions.InventoryDepletedException;
import exceptions.InventoryNotFoundException;
import models.InventoryItem;
import models.Product;

import java.io.File;
import java.util.*;

public class Runner {

    public static void main(String[] args) {

        List<InventoryItem> inventoryItems = new ArrayList<>();

        Scanner sc;
        String name, role, option, proceed;
        int code, quantity;
        double price;

        InventorySystem inventorySystem;
        Map<String, String> changes = new HashMap<>();

        try {
            System.out.println("Loading Inventory System...");
            File file = new File("files/input.txt");
            sc = new Scanner(file);

            while (sc.hasNextLine()) {
                String[] data = sc.nextLine().split(",");
                code = Integer.parseInt(data[0]);
                name = data[1].trim();
                price = Double.parseDouble(data[2].trim());
                quantity = Integer.parseInt(data[3].trim());
                inventoryItems.add(new InventoryItem(new Product(code, name, price), quantity));
            }

            sc.close();
            System.out.println("Inventory System Loaded Successfully!");

            inventorySystem = new InventorySystem(inventoryItems);
            sc = new Scanner(System.in);

            do {
                do {
                    System.out.println("Welcome to XYZ Stock Inventory Management System!");
                    System.out.println("Please select your role:");
                    System.out.println("(1) Customer");
                    System.out.println("(2) Staff");
                    role = sc.nextLine();
                } while (inventorySystem.isInvalidRole(role));

                if (inventorySystem.currentUser == 1) {
                    do {
                        System.out.println("Select an option");
                        System.out.println("(1) View Inventory");
                        System.out.println("(2) Purchase Inventory Item");
                        System.out.println("(3) View Shopping Cart");
                        System.out.println("(4) Exit");

                        option = sc.next();

                        switch (option) {
                            case "1":
                                inventorySystem.displayInventory();
                                break;
                            case "2":
                                do {
                                    try {
                                        System.out.println("Enter Item Code:");
                                        code = sc.nextInt();
                                        inventorySystem.checkValidItemCode(code);
                                        System.out.println("Enter quantity:");
                                        quantity = sc.nextInt();
                                        inventorySystem.addToCart(code, quantity);
                                    } catch (InventoryNotFoundException e) {
                                        System.out.println(e.getMessage());
                                    } catch (InventoryDepletedException e) {
                                        System.out.println(e.getMessage());
                                    } catch (InputMismatchException e) {
                                        System.out.println("Error! Enter only numerical values!");
                                    } finally {
                                        System.out.println("Do you want to continue shopping? (y/n)");
                                        proceed = sc.next();
                                    }
                                } while (inventorySystem.hasMoreItems(proceed));
                                break;
                            case "3":
                                inventorySystem.displayShoppingCart();
                                break;
                            case "4":
                                System.out.println("Thank You and have a nice day!");
                                System.exit(0);
                                break;
                            default:
                                System.out.println("Invalid Option!");
                                break;
                        }
                    } while (true);

                } else if (role.equals("2")) {
                    do {
                        System.out.println("Select an option");
                        System.out.println("(1) View Inventory");
                        System.out.println("(2) Add InventoryItem");
                        System.out.println("(3) Modify InventoryItem");
                        System.out.println("(4) Remove InventoryItem");
                        System.out.println("(5) Exit");

                        option = sc.next();

                        switch (option) {
                            case "1" -> inventorySystem.displayInventory();
                            case "2" -> {
                                try {
                                    System.out.println("Enter item code of product:");
                                    code = sc.nextInt();
                                    inventorySystem.checkDuplicateItemCode(code);
                                    System.out.println("Enter name of product:");
                                    name = sc.next();
                                    System.out.println("Enter price of product:");
                                    price = sc.nextDouble();
                                    System.out.println("Enter quantity of product");
                                    quantity = sc.nextInt();
                                    inventorySystem.addInventoryItem(code, name, price, quantity);
                                } catch (DuplicateInventoryException e) {
                                    System.out.println("Unable to proceed. " + e.getMessage());
                                } catch (InputMismatchException e) {
                                    System.out.println("Error! Enter only numerical values!");
                                }
                            }
                            case "3" -> {
                                try {
                                    System.out.println("Enter item code of product:");
                                    code = sc.nextInt();
                                    inventorySystem.checkValidItemCode(code);
                                    System.out.println("What do you want to modify?");
                                    System.out.println("(1) Product Name");
                                    System.out.println("(2) Product Price");
                                    System.out.println("(3) Quantity");
                                    option = sc.next();
                                    switch (option) {
                                        case "1" -> {
                                            System.out.println("Enter new Product Name:");
                                            name = sc.next();
                                            changes.put("name", name);
                                        }
                                        case "2" -> {
                                            System.out.println("Enter new Product Price:");
                                            price = sc.nextDouble();
                                            changes.put("price", Double.toString(price));
                                        }
                                        case "3" -> {
                                            System.out.println("Enter new quantity:");
                                            quantity = sc.nextInt();
                                            changes.put("quantity", Integer.toString(quantity));
                                        }
                                        default -> System.out.println("No such field to modify!");
                                    }
                                    inventorySystem.modifyInventoryItem(code, changes);
                                } catch (InventoryNotFoundException e) {
                                    System.out.println("Unable to proceed. " + e.getMessage());
                                } catch (InputMismatchException e) {
                                    System.out.println("Error! Enter only numerical values!");
                                } finally {
                                    changes.clear();
                                }
                            }
                            case "4" -> {
                                try {
                                    System.out.println("Enter Item Code of Product to remove:");
                                    code = sc.nextInt();
                                    inventorySystem.checkValidItemCode(code);
                                    inventorySystem.removeInventoryItem(code);
                                } catch (InventoryNotFoundException e) {
                                    System.out.println("Unable to proceed. " + e.getMessage());
                                } catch (InputMismatchException e) {
                                    System.out.println("Error! Enter only numerical values!");
                                }
                            }
                            case "5" -> {
                                System.out.println("Thank You and have a nice day!");
                                System.exit(0);
                            }
                            default -> System.out.println("Invalid Option!");
                        }
                    } while (true);
                }
            } while (true);

        } catch (Exception e) {
            System.out.println("Something went wrong while loading file!");
        }
    }
}
