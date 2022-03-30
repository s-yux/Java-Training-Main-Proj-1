package exceptions;

public class DuplicateInventoryException extends Exception{
    @Override
    public String getMessage() {
        return "Item Code already exists in system, please input another Item Code.";
    }
}
