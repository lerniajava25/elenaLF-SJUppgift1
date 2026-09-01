package elprices.application;

// Thrown when prices cannot be retrieved(instead of returning null) (http 404, API doesn't response, missing data, JSON problem)
public class PriceUnavailableException extends RuntimeException {

    public PriceUnavailableException(String message) {

        super(message);
    }

    public PriceUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}

