package ru.sshibko.AccountsManager.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {

    private String message;

    @Override
    public String toString() {
        return "ErrorResponse [message=" + message + "]";
    }
}
