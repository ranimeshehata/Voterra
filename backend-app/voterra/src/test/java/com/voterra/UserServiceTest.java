package com.voterra;

import com.voterra.entities.User;
import com.voterra.repos.UserRepository;
import com.voterra.services.UserService;
import com.voterra.tokenization.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtils jwtUtils;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSignup_UserAlreadyExists() {
        // Arrange
        User user = new User();
        user.setEmail("test@example.com");
        when(userRepository.findById(user.getEmail())).thenReturn(Optional.of(user));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.signup(user));
        assertEquals("Email already exists", exception.getMessage());
    }

    @Test
    void testSignup_UsernameAlreadyExists() {
        // Arrange
        User user = new User();
        user.setEmail("test@example.com");
        user.setUsername("testuser");
        when(userRepository.findById(user.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.signup(user));
        assertEquals("Username already exists", exception.getMessage());
    }

    @Test
    void testSignup_Success() {
        // Arrange
        User user = new User();
        user.setEmail("test@example.com");
        String password = "password123";
        user.setUsername("testuser");
        user.setPassword(new BCryptPasswordEncoder().encode(password)); // Storing hashed password

        when(userRepository.findById(user.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());
        when(userRepository.save(user)).thenReturn(user);
        when(jwtUtils.generateToken(user.getEmail())).thenReturn("jwtToken");

        // Act
        Object[] result = userService.signup(user);

        // Assert
        assertEquals(user, result[0]);
        assertNotNull(result[1]);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testSignupOrLoginWithGoogleOrFacebook_UserAlreadyExists() {
        // Arrange
        User user = new User();
        user.setEmail("test@example.com");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
        when(jwtUtils.generateToken(user.getEmail())).thenReturn("jwtToken");

        // Act
        Object[] result = userService.signupOrLoginWithGoogleOrFacebook(user);

        // Assert
        assertEquals(user, result[0]);
        assertNotNull(result[1]);
    }

    @Test
    void testSignupOrLoginWithGoogleOrFacebook_NewUser() {
        // Arrange
        User user = new User();
        user.setEmail("test@example.com");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(null);
        when(userRepository.save(user)).thenReturn(user);
        when(jwtUtils.generateToken(user.getEmail())).thenReturn("jwtToken");

        // Act
        Object[] result = userService.signupOrLoginWithGoogleOrFacebook(user);

        // Assert
        assertEquals(user, result[0]);
        assertNotNull(result[1]);
    }

    @Test
    void testLogin_UserNotFound() {
        // Arrange
        String email = "test@example.com";
        String password = "password123";

        when(userRepository.findById(email)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.login(email, password));
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void testLogin_InvalidPassword() {
        // Arrange
        String email = "test@example.com";
        String password = "wrongPassword";

        User user = new User();
        user.setEmail(email);
        user.setPassword(new BCryptPasswordEncoder().encode("password123")); // Storing hashed password

        when(userRepository.findById(email)).thenReturn(Optional.of(user));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.login(email, password);
        });
        assertEquals("Invalid password", exception.getMessage());
    }

    @Test
    void testLogin_Success() {
        // Arrange
        String email = "test@example.com";
        String password = "password123";

        User user = new User();
        user.setEmail(email);
        user.setPassword(new BCryptPasswordEncoder().encode(password)); // Storing hashed password

        when(userRepository.findById(email)).thenReturn(Optional.of(user));
        when(jwtUtils.generateToken(email)).thenReturn("jwtToken");

        // Act
        Object[] result = userService.login(email, password);

        // Assert
        assertEquals(user, result[0]);
        assertNotNull(result[1]);
    }
    @Test
    void resetpassword(){

    }

    @Test
    void testSignOut() {
        // Act
        // Arrange
        String email = "test@example.com";
        String password = "password123";

        User user = new User();
        user.setEmail(email);
        user.setPassword(new BCryptPasswordEncoder().encode(password)); // Storing hashed password

        when(userRepository.findById(email)).thenReturn(Optional.of(user));
        when(jwtUtils.generateToken(email)).thenReturn("jwtToken");

        // Act
        Object[] result = userService.login(email, password);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(((User)result[0]).getEmail(), null, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        userService.signOut();

        // Assert
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void forgetPassword() {
        // Arrange
        // Arrange
        String email = "test@example.com";
        String password = "password123";

        User user = new User();
        user.setEmail(email);
        user.setPassword(new BCryptPasswordEncoder().encode(password)); // Storing hashed password

        when(userRepository.findById(email)).thenReturn(Optional.of(user));

        // Act
        boolean result = userService.forgetPassword(user.getEmail(), "newPassword");

        // Assert
        assertTrue(result);
    }
}
