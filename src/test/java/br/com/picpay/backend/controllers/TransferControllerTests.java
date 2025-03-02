package br.com.picpay.backend.controllers;

import static org.mockito.Mockito.*;

import br.com.picpay.backend.config.JacksonConfiguration;
import br.com.picpay.backend.config.TestContainersConfiguration;
import br.com.picpay.backend.config.TomcatTestConfiguration;
import br.com.picpay.backend.data.dtos.TransferInformation;
import br.com.picpay.backend.data.dtos.TransferResult;
import br.com.picpay.backend.data.entities.User;
import br.com.picpay.backend.data.enums.TransferKnownStates;
import br.com.picpay.backend.data.enums.UserKnownTypes;
import br.com.picpay.backend.data.repositories.UserRepository;
import br.com.picpay.backend.exceptions.ApiErrorHandler;
import br.com.picpay.backend.services.AuthorizationService;
import br.com.picpay.backend.services.TransferService;
import br.com.picpay.backend.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@WebMvcTest(controllers = TransferController.class)
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class TransferControllerTests {

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @Mock
    private AuthorizationService authService;

    @Mock
    private TransferService transferService;


    @Configuration
    @Import({
            JacksonConfiguration.class,
            TestContainersConfiguration.class,
            TomcatTestConfiguration.class,
    })
    static class TransferControllerTestConfiguration {
    }

    @BeforeEach
    public void setup() {

        MockitoAnnotations.openMocks(this);

        this.userRepository = Mockito.mock(UserRepository.class);
        this.userService = new UserService(userRepository);
        this.transferService = new TransferService(userService, authService);

        this.mockMvc = MockMvcBuilders.standaloneSetup(new TransferController(this.transferService))
                .setControllerAdvice(new ApiErrorHandler())
                .build();

        var testUser1 = new User(1L,
                "test_user1@gmail.com",
                "test-user-1",
                UserKnownTypes.Common,
                1000.0);

        var testUser2 = new User(2L,
                "test_user2@gmail.com",
                "test-user-2",
                UserKnownTypes.Common,
                1000.0);

        var testStoreUser1 = new User(3L,
                "store_user1@gmail.com",
                "store-user-1",
                UserKnownTypes.StoreOwner,
                1000.0);

        var testStoreUser2 = new User(4L,
                "store_user2@gmail.com",
                "store-user-2",
                UserKnownTypes.StoreOwner,
                1000.0);

        lenient().when(userRepository.findByUserId(1L)).thenReturn(testUser1);
        lenient().when(userRepository.findByUserId(2L)).thenReturn(testUser2);
        lenient().when(userRepository.findByUserId(3L)).thenReturn(testStoreUser1);
        lenient().when(userRepository.findByUserId(4L)).thenReturn(testStoreUser2);

        lenient().when(userRepository.findByUserName("test-user1")).thenReturn(testUser1);
        lenient().when(userRepository.findByUserName("test-user-2")).thenReturn(testUser2);
        lenient().when(userRepository.findByUserName("store-user-1")).thenReturn(testStoreUser1);
        lenient().when(userRepository.findByUserName("store-user-2")).thenReturn(testStoreUser2);
    }

    // Test Case 1: Valid transfer attempt (Customer -> Customer)
    @Test
    void testCustomerToCustomerTransferShouldReturn200() throws Exception {

        var transferInfo = new TransferInformation(
                1L,
                2L,
                100.0);

        var expectedResponse = new TransferResult(
                transferInfo,
                TransferKnownStates.Completed);

        mockMvc.perform(MockMvcRequestBuilders.post("/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transferInfo)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(expectedResponse)));
    }
    // Test Case 2: Valid transfer attempt (Customer -> Store)
    @Test
    void testCustomerToStoreTransferShouldReturn200() throws Exception {
        var transferInfo = new TransferInformation(
                1L,
                3L,
                100.0);

        var expectedResponse = new TransferResult(
                transferInfo,
                TransferKnownStates.Completed);

        mockMvc.perform(MockMvcRequestBuilders.post("/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transferInfo)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(expectedResponse)));
    }
    // Test Case 3: Invalid transfer attempt (Store -> Customer)
    @Test
    void testStoreToCustomerTransferShouldFail() throws Exception {
        var transferInfo = new TransferInformation(
                3L,
                1L,
                100.0);

        var expectedResponse = new TransferResult(
                transferInfo,
                TransferKnownStates.InvalidUserKnownTypes);

        mockMvc.perform(MockMvcRequestBuilders.post("/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transferInfo)))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(expectedResponse)));
    }
    // Test Case 4: Invalid transfer attempt (Store -> Store)
    @Test
    void testStoreToStoreTransferShouldReturnFail() throws Exception {
        var transferInfo = new TransferInformation(
                3L,
                4L,
                100.0);

        var expectedResponse = new TransferResult(
                transferInfo,
                TransferKnownStates.InvalidUserKnownTypes);

        mockMvc.perform(MockMvcRequestBuilders.post("/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transferInfo)))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(expectedResponse)));
    }
    // Test Case 5: Invalid transfer attempt (Insufficient Funds)
    @Test
    void testAnyUserTypeTransferWithInvalidAmountShouldFail() throws Exception {
        var transferInfo = new TransferInformation(
                1L,
                2L,
                9999.0);

        var expectedResponse = new TransferResult(
                transferInfo,
                TransferKnownStates.InsufficientFunds);

        mockMvc.perform(MockMvcRequestBuilders.post("/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transferInfo)))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(expectedResponse)));
    }
    // Test Case 6: Invalid transfer attempt (Unknown user)
    @Test
    void testUnknownUserTransferShouldFail() throws Exception {
        var transferInfo = new TransferInformation(
                1L,
                99L, /* Non-existing user sample id */
                1.0);

        var expectedResponse = new TransferResult(
                transferInfo,
                TransferKnownStates.InvalidUserInformation);

        mockMvc.perform(MockMvcRequestBuilders.post("/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transferInfo)))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(expectedResponse)));
    }

    @Test
    void testUnauthorizedUserTransferShouldFail() throws Exception {

        when(authService.isAuthorized(any(User.class))).thenReturn(false);

        var transferInfo = new TransferInformation(
                1L,
                3L,
                100.0);

        var expectedResponse = new TransferResult(
                transferInfo,
                TransferKnownStates.UnauthorizedUser);

        mockMvc.perform(MockMvcRequestBuilders.post("/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transferInfo)))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(expectedResponse)));
    }

    @Test
    void testUnauthorizedUserTransferShouldReturn200() throws Exception {

        when(authService.isAuthorized(any(User.class))).thenReturn(true);

        var transferInfo = new TransferInformation(
                1L,
                3L,
                100.0);

        var expectedResponse = new TransferResult(
                transferInfo,
                TransferKnownStates.Completed);

        mockMvc.perform(MockMvcRequestBuilders.post("/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transferInfo)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(expectedResponse)));
    }
}
