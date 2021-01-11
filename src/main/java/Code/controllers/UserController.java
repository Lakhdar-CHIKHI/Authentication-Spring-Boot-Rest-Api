package Code.controllers;

import Code.models.Role;
import Code.models.User;
import Code.payload.request.SignupRequest;
import Code.security.services.IUserService;
import Code.security.services.roleService.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import Code.payload.response.MessageResponse;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge=3600)
@RestController
@RequestMapping("/api/")
public class UserController {

    @Autowired
    IUserService iUserService;

    @Autowired
    IRoleService iRoleService;


    @Autowired
    PasswordEncoder encoder;

    @PostMapping("/user")
    public ResponseEntity registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        boolean response = iUserService.existsByUsername(signUpRequest.getUsername());
        response = iUserService.existsByEmail(signUpRequest.getEmail());

        User user = User.builder().username(signUpRequest.getUsername())
                .email(signUpRequest.getEmail())
                .password(encoder.encode(signUpRequest.getPassword())).build();

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles != null)
            strRoles.forEach(role -> {
                roles.add(iRoleService.findByName(role));
            });

        user.setRoles(roles);

        iUserService.save(user);

        return new ResponseEntity<>(new MessageResponse("User registered successfully!"), HttpStatus.CREATED);
    }


    @PutMapping(value = "user/{id}")
    public ResponseEntity editUser(@PathVariable("id") long id,@Valid @RequestBody SignupRequest signUpRequest) throws IOException {

            User user_ = iUserService.findById(id);
            Set<String> strRoles = signUpRequest.getRole();
            Set<Role> roles = new HashSet<>();

            if (strRoles != null)
                strRoles.forEach(role -> {
                    roles.add(iRoleService.findByName(role));
                });

            user_.setUsername(signUpRequest.getUsername());
            user_.setEmail(signUpRequest.getEmail());
            user_.setRoles(roles);

        if (signUpRequest.getPassword() != null)
            user_.setPassword(encoder.encode(signUpRequest.getPassword()));



            iUserService.save(user_);

        return new ResponseEntity<>(new MessageResponse("User Upadted successfully!"), HttpStatus.OK);
    }


    @GetMapping("user/{id}")
    public ResponseEntity getUserById(@PathVariable("id") long id) {
        User user_ = iUserService.findById(id);

        return new ResponseEntity<>(user_, HttpStatus.OK);
    }

    @GetMapping("/users")
    public ResponseEntity getAllUsers(){

        return new ResponseEntity<>(iUserService.findAll(), HttpStatus.OK);
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity deleteCustomer(@PathVariable("id") long id) {
        iUserService.deleteById(id);
        return new ResponseEntity<>(new MessageResponse("User Deleted successfully!"),HttpStatus.OK);
    }


}
