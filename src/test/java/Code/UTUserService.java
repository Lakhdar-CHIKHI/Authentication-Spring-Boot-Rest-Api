package Code;


import Code.models.EnumRole;
import Code.models.Role;
import Code.models.User;
import Code.repository.RoleRepository;
import Code.repository.UserRepository;
import Code.security.Error.Email_UserExistException;
import Code.security.Error.Role_NotFoundException;
import Code.security.Error.UserNotFoundException;
import Code.security.Error.Username_UserExistException;
import Code.security.services.IUserService;
import Code.security.services.UserDetailsServiceImpl;
import Code.security.services.roleService.IRoleService;
import Code.security.services.roleService.ImplRoleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

//@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
public class UTUserService {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    @Spy
    private final IRoleService iRoleService = new ImplRoleService();

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    @Spy
    private final IUserService iUserService = new UserDetailsServiceImpl();

    //		User
    private User user = User.builder()
           .username("User P-User -1").email("User_1@gmail.com")
            .password("123456789").build();

    //		Role
    private Role role = Role.builder()
            .name(EnumRole.ROLE_USER).build();

    @Test
    public void should_return_Role_successfully(){

        //Given
        when (roleRepository.findByName(Mockito.any(EnumRole.class))).thenReturn(Optional.of(role));

        //When
        Role result = iRoleService.findByName("user");

        Assertions.assertEquals(role.getName(),result.getName());
    }

    @Test
    public void should_throw_Role_NotFoundException_when_there_is_not_role(){
        //When

        assertThatThrownBy(() -> iRoleService.findByName("userr"))
                .isInstanceOf(Role_NotFoundException.class)
                .hasMessage("Fail! -> Cause: User Role not find.");
    }

    @Test
    public void should_return_user_successfully_findByUsername(){

        //Given
        when (userRepository.findByUsername("user")).thenReturn(Optional.of(user));

        //When
        User result = iUserService.findByUsername("user");

        Assertions.assertEquals(user.getUsername(),result.getUsername());
    }

    @Test
    public void should_throw_user_NotFoundException_when_there_is_not_user_with_this_username_findByUsername(){
        //When

        assertThatThrownBy(() -> iUserService.findByUsername("userr"))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User with username userr not found");
    }

    @Test
    public void should_return_user_successfully_findById(){

        //Given
        when (userRepository.findById(2L)).thenReturn(Optional.of(user));

        //When
        User result = iUserService.findById(2L);

        Assertions.assertEquals(user.getUsername(),result.getUsername());
    }

    @Test
    public void should_throw_user_NotFoundException_when_there_is_not_user_with_this_id_findById(){
        //When

        assertThatThrownBy(() -> iUserService.findById(2L))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User with username 2 not found");
    }

    @Test
    public void should_delete_user_successfully(){
        //Given
        when (userRepository.findById(2L)).thenReturn(Optional.of(user));
        //When
        iUserService.deleteById(2L);
        verify(userRepository).deleteById(2L);
    }

    @Test
    public void should_throw_user_NotFoundException_when_there_is_not_user_with_this_id_deleteById(){
        //When

        assertThatThrownBy(() -> iUserService.deleteById(2L))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User with username 2 not found");
    }

    @Test
    public void check_user_by_username_is_not_exist_successfully(){

        //Given
        when (userRepository.existsByUsername("user")).thenReturn(false);

        //When
        boolean result = iUserService.existsByUsername("user");

        Assertions.assertFalse(result);
    }

    @Test
    public void should_throw_Username_UserExistException_when_there_is_user_with_same_username(){
        //Given
        when (userRepository.existsByUsername("user")).thenReturn(true);

        //When

        assertThatThrownBy(() -> iUserService.existsByUsername("user"))
                .isInstanceOf(Username_UserExistException.class)
                .hasMessage("User with username user Already exist");
    }

    @Test
    public void check_user_by_email_is_not_exist_successfully(){

        //Given
        when (userRepository.existsByEmail("user_1@gmail.com")).thenReturn(false);

        //When
        boolean result = iUserService.existsByEmail("user_1@gmail.com");

        Assertions.assertFalse(result);
    }

    @Test
    public void should_throw_Email_UserExistException_when_there_is_user_with_same_email(){
        //Given
        when (userRepository.existsByEmail("user_1@gmail.com")).thenReturn(true);

        //When

        assertThatThrownBy(() -> iUserService.existsByEmail("user_1@gmail.com"))
                .isInstanceOf(Email_UserExistException.class)
                .hasMessage("User with email user_1@gmail.com Already exist");
    }

    @Test
    public void should_return_user_successfully_create_user(){
        //Given
        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        //When
        User result = iUserService.save(user);

        Assertions.assertEquals(user,result);
    }
}
