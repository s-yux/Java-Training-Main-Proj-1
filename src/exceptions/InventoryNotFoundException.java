package exceptions;

public class InventoryNotFoundException extends Exception{
    @Override
    public String getMessage() {
        return "There is no such item in the system.";
    }
}
