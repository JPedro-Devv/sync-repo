package exceptions;

public class NotGitRepositoryException extends RuntimeException {
    public NotGitRepositoryException(String message) {
        super(message);
    }
}
