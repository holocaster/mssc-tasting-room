package br.com.prcompany.mssctastingroom.exceptions;

public class OrderNotInsertedException extends RuntimeException {

    public OrderNotInsertedException(String message) {
        super(message);
    }

    public OrderNotInsertedException(String message, Throwable cause) {
        super(message, cause);
    }
}
