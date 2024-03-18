package io.hhplus.tdd.point.exception;

public class PointException extends RuntimeException {

    public static class IdNotExistException extends RuntimeException {
        public IdNotExistException(String message) {
            super(message);
        }
    }

    public static class AmountNotExistException extends RuntimeException {
        public AmountNotExistException(String message) {
            super(message);
        }
    }

    public static class UseAmountExceedChargedException extends RuntimeException {
        public UseAmountExceedChargedException(String message) {
            super(message);
        }
    }
}
