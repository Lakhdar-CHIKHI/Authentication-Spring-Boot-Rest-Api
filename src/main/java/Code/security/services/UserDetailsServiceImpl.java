package Code.security.services;

import Code.security.Error.Email_UserExistException;
import Code.security.Error.UserNotFoundException;
import Code.security.Error.Username_UserExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import Code.models.User;
import Code.repository.UserRepository;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService, IUserService {
	@Autowired
	UserRepository userRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UserNotFoundException(username));

		return UserDetailsImpl.build(user);
	}

	@Override
	public User findByUsername(String username) throws UserNotFoundException {
		return userRepository.findByUsername(username)
				.orElseThrow(() -> new UserNotFoundException(username));
	}

	@Override
	public User findById(long id) {
		return userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException(""+id));
	}

	@Override
	public List<User> findAll() {
		return userRepository.findAll();
	}

	@Override
	public void deleteById(long id) {
		this.findById(id);
		userRepository.deleteById(id);
	}

	@Override
	public boolean existsByUsername(String username) throws Username_UserExistException {
		if (userRepository.existsByUsername(username))
			throw new Username_UserExistException(username);
		return false;
	}

	@Override
	public boolean existsByEmail(String email) throws Email_UserExistException {
		if (userRepository.existsByEmail(email))
			throw new Email_UserExistException(email);
		return false;
	}

	@Override
	public User save(User user) {
		return userRepository.save(user);
	}
}
