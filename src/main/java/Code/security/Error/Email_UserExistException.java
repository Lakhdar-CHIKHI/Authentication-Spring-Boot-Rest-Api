package Code.security.Error;

public class Email_UserExistException extends RuntimeException {
    public Email_UserExistException(String email) {
        super("User with email "+ email +" Already exist");
    }
}
