package com.alten.technical.test.booking.hotel.exception;

public class RoomAlreadyBookedException extends RuntimeException {
    public RoomAlreadyBookedException() {
    }

    public RoomAlreadyBookedException(String message) {
        super(message);
    }

    public RoomAlreadyBookedException(String message, Throwable cause) {
        super(message, cause);
    }

    public RoomAlreadyBookedException(Throwable cause) {
        super(cause);
    }

    public RoomAlreadyBookedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
