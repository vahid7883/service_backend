package com.numjew.service_backend.shopping_basket.controller;

import com.numjew.service_backend.auth.AuthService;
import com.numjew.service_backend.product.exception.ProductVariantNotFoundException;
import com.numjew.service_backend.shopping_basket.domain.UpdateBasketItemRequest;
import com.numjew.service_backend.shopping_basket.dto.ShoppingBasketItemDto;
import com.numjew.service_backend.shopping_basket.exception.ShoppingBasketNotFoundException;
import com.numjew.service_backend.shopping_basket.service.ShoppingBasketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.numjew.service_backend.shopping_basket.dto.ShoppingBasketDto;
import com.numjew.service_backend.user.Role;
import com.numjew.service_backend.user.User;
import org.junit.jupiter.api.Test;
import com.numjew.service_backend.shopping_basket.domain.AddBasketItemRequest;
import org.springframework.http.MediaType;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ShoppingBasketControllerTest {

    @Mock
    private ShoppingBasketService shoppingBasketService;

    @Mock
    private AuthService authService;

    @InjectMocks
    private ShoppingBasketController shoppingBasketController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(shoppingBasketController)
                .build();
    }

    @Test
    void shouldCreateBasket() throws Exception {
        // Arrange
        var user = User.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .role(Role.USER)
                .build();

        var basketDto = new ShoppingBasketDto();
        basketDto.setId(UUID.randomUUID());

        when(authService.getCurrentUser())
                .thenReturn(user);

        when(shoppingBasketService.createBasket(user.getId()))
                .thenReturn(basketDto);

        // Act & Assert
        mockMvc.perform(post("/shopping-baskets"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(basketDto.getId().toString()));

        verify(authService).getCurrentUser();
        verify(shoppingBasketService).createBasket(user.getId());
    }

    @Test
    void shouldAddItemToBasket() throws Exception {
        // Arrange
        var user = User.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .role(Role.USER)
                .build();

        var basketId = UUID.randomUUID();

        var request = new AddBasketItemRequest();
        request.setProductVariantId(10L);

        var basketDto = new ShoppingBasketDto();
        basketDto.setId(basketId);

        when(authService.getCurrentUser())
                .thenReturn(user);

        when(shoppingBasketService.addToBasket(
                basketId,
                10L,
                user.getId()
        )).thenReturn(basketDto);

        // Act & Assert
        mockMvc.perform(
                        post("/shopping-baskets/{shoppingBasketId}/items", basketId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                            {
                                "productVariantId": 10
                            }
                            """)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(basketId.toString()));

        verify(authService).getCurrentUser();

        verify(shoppingBasketService).addToBasket(
                basketId,
                10L,
                user.getId()
        );
    }

    @Test
    void shouldGetBasket() throws Exception {
        // Arrange
        var user = User.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .role(Role.USER)
                .build();

        var basketId = UUID.randomUUID();

        var request = new AddBasketItemRequest();
        request.setProductVariantId(10L);

        var basketDto = new ShoppingBasketDto();
        basketDto.setId(basketId);

        when(authService.getCurrentUser())
                .thenReturn(user);

        when(shoppingBasketService.getBasket(
                basketId,
                user.getId()
        )).thenReturn(basketDto);

        // Act & Assert
        mockMvc.perform(
                        get("/shopping-baskets/{basketId}", basketId)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(basketId.toString()));

        verify(authService).getCurrentUser();

        verify(shoppingBasketService).getBasket(
                basketId,
                user.getId()
        );
    }

    @Test
    void shouldUpdateBasket() throws Exception{
        var user = User.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .role(Role.USER)
                .build();

        var basketId = UUID.randomUUID();
        int newQuantity = 3;
        var productVariantId = 10L;

        UpdateBasketItemRequest request = new UpdateBasketItemRequest();

        request.setQuantity(newQuantity);


        ShoppingBasketItemDto expectedResponse = new ShoppingBasketItemDto();
        expectedResponse.setProductVariantId(productVariantId);
        expectedResponse.setQuantity(newQuantity);

        when(authService.getCurrentUser())
                .thenReturn(user);
        when(shoppingBasketService.updateItem(basketId,productVariantId,newQuantity,user.getId())).thenReturn(expectedResponse);

        mockMvc.perform(put("/shopping-baskets/{basketId}/items/{productVariantId}", basketId, productVariantId) // Adjust base path if needed
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                           
                                "quantity": 3
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(newQuantity));

        verify(authService).getCurrentUser();

        verify(shoppingBasketService).updateItem(basketId,productVariantId,newQuantity,user.getId());

    }

    @Test
    void shouldRemoveItem() throws Exception {
        var user = User.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .role(Role.USER)
                .build();

        var basketId = UUID.randomUUID();
        var productVariantId = 10L;

        when(authService.getCurrentUser())
                .thenReturn(user);

        mockMvc.perform(
                        delete(
                                "/shopping-baskets/{basketId}/items/{productVariantId}",
                                basketId,
                                productVariantId
                        )
                )
                .andExpect(status().isNoContent());

        verify(authService).getCurrentUser();

        verify(shoppingBasketService).removeItem(
                basketId,
                productVariantId,
                user.getId()
        );
    }

    @Test
    void shouldClearBasket() throws Exception{
        var user = User.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .role(Role.USER)
                .build();

        var basketId = UUID.randomUUID();

        when(authService.getCurrentUser())
                .thenReturn(user);
        mockMvc.perform(
                delete("/shopping-baskets/{basketId}/items", basketId))
                .andExpect(status().isNoContent());

        verify(authService).getCurrentUser();
        verify(shoppingBasketService).clearBasket(basketId,user.getId());

    }

    @Test
    void shouldReturn404WhenBasketNotFound() throws Exception {
        var user = User.builder()
                .id(1L)
                .role(Role.USER)
                .build();

        var basketId = UUID.randomUUID();

        when(authService.getCurrentUser())
                .thenReturn(user);

        when(shoppingBasketService.getBasket(
                basketId,
                user.getId()
        )).thenThrow(new ShoppingBasketNotFoundException());

        mockMvc.perform(
                        get("/shopping-baskets/{basketId}", basketId)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error")
                        .value("Basket not found."));
    }

    @Test
    void shouldReturnNotFoundWhenBasketDoesNotExist() throws Exception {
        var user = User.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .role(Role.USER)
                .build();

        var basketId = UUID.randomUUID();

        when(authService.getCurrentUser())
                .thenReturn(user);

        when(shoppingBasketService.getBasket(basketId, user.getId()))
                .thenThrow(new ShoppingBasketNotFoundException());

        mockMvc.perform(
                        get("/shopping-baskets/{basketId}", basketId)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error")
                        .value("Basket not found."));

        verify(authService).getCurrentUser();

        verify(shoppingBasketService)
                .getBasket(basketId, user.getId());
    }

    @Test
    void shouldReturnBadRequestWhenProductVariantDoesNotExist() throws Exception {
        var user = User.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .role(Role.USER)
                .build();

        var basketId = UUID.randomUUID();
        var productVariantId = 10L;

        when(authService.getCurrentUser())
                .thenReturn(user);

        when(shoppingBasketService.addToBasket(
                basketId,
                productVariantId,
                user.getId()
        )).thenThrow(new ProductVariantNotFoundException());

        mockMvc.perform(
                        post(
                                "/shopping-baskets/{shoppingBasketId}/items",
                                basketId
                        )
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                            {
                                "productVariantId": 10
                            }
                            """)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error")
                        .value("Product not found."));

        verify(authService).getCurrentUser();

        verify(shoppingBasketService).addToBasket(
                basketId,
                productVariantId,
                user.getId()
        );
    }


}