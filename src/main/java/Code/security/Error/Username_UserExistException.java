package Code.security.Error;

public class Username_UserExistException extends RuntimeException {
    public Username_UserExistException(String username) {
            super("User with username "+ username +" Already exist");
    }
}
