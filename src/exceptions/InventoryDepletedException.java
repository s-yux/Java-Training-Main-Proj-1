package exceptions;

public class InventoryDepletedException extends Exception{
    @Override
    public String getMessage() {
        return "There is not enough stocks to fulfill your needs!";
    }
}
