package etu1748.framework;

public class NotAuthorizedException extends Exception {
    public NotAuthorizedException(String error) {
        super(error);
    }
}
 