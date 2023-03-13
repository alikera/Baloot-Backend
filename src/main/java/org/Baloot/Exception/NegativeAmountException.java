package org.Baloot.Exception;

public class NegativeAmountException extends ExceptionHandler {
    public NegativeAmountException() {
        super("Amount cannot be negative!");
    }
}
