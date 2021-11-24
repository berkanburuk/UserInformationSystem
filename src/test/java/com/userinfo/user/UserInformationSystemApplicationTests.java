package com.userinfo.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.userinfo.user.model.User;
import com.userinfo.user.repository.UserRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:data.sql")
class UserInformationSystemApplicationTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("When all users are requested then they are all returned")
    void allUsersRequested() throws Exception {
        mockMvc
                .perform(get("/user/getAllUsers"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$", hasSize((int) userRepository.count())));
    }

    @Test
    @DisplayName("When a user is requested, a user information will return.")
    void getAUserRequested() throws Exception {
        mockMvc
                .perform(get("/user/getUser/1"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("When a user delete is requested, deletion will be persisted")
    void deleteAUserRequested() throws Exception {
        mockMvc
                .perform(delete("/user/delete/1"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @Disabled
    @DisplayName("When a user update is requested then it is persisted")
    void updateAUser() throws Exception {
        String name = mapper.readValue(
                mockMvc.
                        perform(
                                put("/user/update")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(mapper.writeValueAsString(new User("B BURUK", "bb@gmail.com", 28, "55555555555"))))
                        .andExpect(status().is2xxSuccessful())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                User.class).getName();

        assertThat(name, equalTo("B Buruk"));
    }


    @Test
    @Disabled
    @DisplayName("When a user creation is requested then it is persisted")
    void userCreatedCorrectly() throws Exception {
        User newUser = new User("B BURUK", "bb@gmail.com", 28, "55555555555");
        Long userId =
                mapper
                        .readValue(
                                mockMvc
                                        .perform(
                                                post("/user/create")
                                                        .contentType(MediaType.APPLICATION_JSON)
                                                        .content(mapper.writeValueAsString(newUser)))
                                        .andExpect(status().isCreated())
                                        .andReturn()
                                        .getResponse()
                                        .getContentAsString(),
                                User.class)
                        .getId();
        newUser.setId(userId);
        assertThat(
                userRepository
                        .findById(userId)
                        .orElseThrow(
                                () -> new IllegalStateException("New user has not been saved in the repository")),
                equalTo(newUser));


    }


}
