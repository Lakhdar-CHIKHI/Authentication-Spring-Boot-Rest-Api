package Code.security.services;

import Code.models.User;

import java.util.List;

public interface IUserService {
    User findByUsername(String username);
    User findById(long id);
    List<User> findAll();
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    User save(User user);
    void deleteById(long id);
}
