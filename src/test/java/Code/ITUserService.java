package Code;


import Code.models.EnumRole;
import Code.models.Role;
import Code.models.User;
import Code.payload.request.SignupRequest;
import Code.security.services.IUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
// @WithMockUser(username="lakhdar", roles="any word")

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@WithMockUser  // by default take username = "" and roles = {"USER"}
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
public class ITUserService {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IUserService iUserService;

    @Autowired
    PasswordEncoder encoder;


    //      SignupRequest
    private SignupRequest signupRequest = SignupRequest.builder()
            .username("User P-User -2").email("User_2@gmail.com")
            .password("123456789 -2").build();

    private SignupRequest signupRequest2 = SignupRequest.builder()
            .username("User P-User -5").email("User_2@gmail.com")
            .password("123456789 -3").build();

    private SignupRequest signupRequest3 = SignupRequest.builder()
            .username("Lakhdar CHIKHI").email("lakhdar.chikhi5@gmail.com")
            .password("123456789lakhdarchikhi").build();



    //		Privilege
    private Role role = Role.builder()
            .name(EnumRole.ROLE_USER).build();


    @Test
    @Order(1)
    public void should_create_user_successfully() throws Exception {
        mockMvc.perform(post("/api/user")
                .content(asJsonString(signupRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message",is("User registered successfully!")))
                .andDo(print());
    }

    @Test
    @Order(2)
    public void should_throw_method_argument_not_valid_exception_user() throws Exception {
        signupRequest.setUsername(null);
        signupRequest.setEmail(null);
        mockMvc.perform(post("/api/user")
                .content(asJsonString(signupRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasItem("must not be blank")))
                .andDo(print());
    }

    @Test
    @Order(3)
    public void should_throw_username_user_exist_exception_when_duplicated_username() throws Exception {
//        iUserService.save(user);
        mockMvc.perform(post("/api/user")
                .content(asJsonString(signupRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.message", is("User with username "+signupRequest.getUsername()+" Already exist")))
                .andDo(print());
    }

    @Test
    @Order(4)
    public void should_throw_Email_user_exist_exception_when_duplicated_email() throws Exception {
//        iUserService.save(user);
        mockMvc.perform(post("/api/user")
                .content(asJsonString(signupRequest2))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.message", is("User with email "+signupRequest2.getEmail()+" Already exist")))
                .andDo(print());
    }



    @Test
    @Order(5)
    public void should_modify_user_successfully() throws Exception {
//        iUserService.save(user);
        mockMvc.perform(put("/api/user/{id}",1)
                .content(asJsonString(signupRequest3))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message",is("User Upadted successfully!")))
                .andDo(print());

    }

    @Test
    @Order(6)
    public void should_throw_method_argument_not_valid_exception_modifier_user() throws Exception {
//        iUserService.save(user);
        signupRequest.setEmail(null);
        signupRequest.setPassword(null);
        mockMvc.perform(put("/api/user/{id}",2)
                .content(asJsonString(signupRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasItem("must not be blank")))
                .andDo(print());
    }

    @Test
    @Order(7)
    public void should_throw_user_not_found_exception_when_there_is_not_user() throws Exception {
//        iUserService.save(user);
        mockMvc.perform(put("/api/user/{id}",100)
                .content(asJsonString(signupRequest3))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.message",is("User with username 100 not found")))
                .andDo(print());
    }



    @Test
    @Order(8)
    public void should_return_user_by_id_successfully() throws Exception {
//        iUserService.save(user);
        mockMvc.perform(get("/api/user/{id}",1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username",is(signupRequest3.getUsername())))
                .andExpect(jsonPath("$.email",is(signupRequest3.getEmail())))
                .andDo(print());
    }

    @Test
    @Order(9)
    public void should_throw_user_not_found_exception_when_there_is_not_user_get_user() throws Exception {
//        iUserService.save(user);
        mockMvc.perform(get("/api/user/{id}",100)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.message",is("User with username 100 not found")))
                .andDo(print());
    }

    @Test
    @Order(10)
    public void should_return_all_user_successfully() throws Exception {
//        iUserService.save(user);
        mockMvc.perform(get("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andDo(print());
    }

    @Test
    @Order(11)
    public void should_delete_user_by_id_successfully() throws Exception {
//        iUserService.save(user);
        mockMvc.perform(delete("/api/user/{id}",1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message",is("User Deleted successfully!")))
                .andDo(print());
    }

    @Test
    @Order(12)
    public void should_throw_user_not_found_exception_when_there_is_not_user_delete() throws Exception {
//        iUserService.save(user);
        mockMvc.perform(delete("/api/user/{id}",100)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.message",is("User with username 100 not found")))
                .andDo(print());
    }


    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
