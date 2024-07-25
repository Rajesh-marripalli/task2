package com.marketsim.task.controller;
import com.marketsim.task.model.request.AuthRequest;
import com.marketsim.task.service.MyAdminDetails;
import com.marketsim.task.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Annotate the class to indicate that it is a Spring Boot test and should load the application context
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AuthControllerTest {

    // Mock the necessary components that the AuthController depends on
    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private MyAdminDetails myAdminDetails;

    @Mock
    private JwtUtil jwtUtil;

    // Inject the mocks into the AuthController instance to be tested
    @InjectMocks
    private AuthController authController;

    // MockMvc object to simulate HTTP requests to the controller
    private MockMvc mockMvc;

    // Method to set up the test environment before each test case runs
    @BeforeEach
    public void setUp() {
        // Initialize mocks and inject them into the AuthController
        MockitoAnnotations.initMocks(this);
        // Set up MockMvc to use the AuthController
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    // Test method to verify the behavior of the authentication endpoint
    @Test
    public void testCreateAuthenticationToken() throws Exception {
        // Create an AuthRequest object with the test username and password
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUserName("user");
        authRequest.setPassword("password");

        // Create a UserDetails object representing a valid user
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername("user")
                .password("password")
                .authorities("ROLE_USER")
                .build();

        // Mock the behavior of the AuthenticationManager to return null when authenticate is called
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        // Mock the behavior of the MyAdminDetails service to return the UserDetails object for the test user
        when(myAdminDetails.loadUserByUsername("user")).thenReturn(userDetails);
        // Mock the behavior of the JwtUtil to return a fake JWT token for the test user
        when(jwtUtil.generateToken(userDetails)).thenReturn("mocked-jwt-token");

        // Perform a POST request to the /auth endpoint with the test username and password
        mockMvc.perform(post("/auth")
                        .contentType("application/json")
                        .content("{\"userName\": \"user\", \"password\": \"password\"}"))
                // Expect the status of the response to be OK (200)
                .andExpect(status().isOk());
    }
}
