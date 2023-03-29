package org.Baloot.Exception;
public class ExceptionHandler extends Exception {

    private static String errorMessage;
    public static void setErrorMessage(String msg) {
        errorMessage = msg;
    }
    public static String getErrorMessage() { return errorMessage; }
    public ExceptionHandler(String message){
        super(message);
    }
}

