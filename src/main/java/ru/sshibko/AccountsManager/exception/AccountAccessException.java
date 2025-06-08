package ru.sshibko.AccountsManager.exception;

public class AccountAccessException extends RuntimeException {
    public AccountAccessException(String message) {
        super(message);
    }
}
