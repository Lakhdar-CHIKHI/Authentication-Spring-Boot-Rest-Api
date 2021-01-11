package Code.security.Error;

public class Role_NotFoundException extends RuntimeException {
    public Role_NotFoundException() {
        super("Fail! -> Cause: User Role not find.");
    }
}
