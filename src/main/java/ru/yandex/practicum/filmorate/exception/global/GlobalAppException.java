package ru.yandex.practicum.filmorate.exception.global;

public class GlobalAppException extends RuntimeException {
    private String logMessage;

    public GlobalAppException() {
        super();
    }

    public GlobalAppException(String message) {
        super(message);
        this.logMessage = "Application error";
    }

    public GlobalAppException(String message, String logMessage) {
        super(message);
        this.logMessage = logMessage;
    }

    public String getLogMessage() {
        return logMessage;
    }
}
