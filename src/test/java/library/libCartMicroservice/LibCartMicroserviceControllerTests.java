package library.libCartMicroservice;

import library.libCartMicroservice.cart.CartController;
import library.libCartMicroservice.cart.CartRequestDTO;
import library.libCartMicroservice.cart.v1.CartService;
import library.libCartMicroservice.cart.v1.CartServiceImplementation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static library.libCartMicroservice.builders.CartBuilders.createBasicSaveCart;
import static library.libCartMicroservice.builders.CartBuilders.createBasicSaveCartRequestDTO;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Tag("cotroller")
@DisplayName("Valida os endpoints relacionados a compras")
public class LibCartMicroserviceControllerTests {

    @Mock
    private CartService cartService;

    @InjectMocks
    private CartController cartController;

    @Test
    @DisplayName("Deve salvar uma compra e retornar o id dela")
    void shouldSaveCart(){
        when(cartService.save(any(CartRequestDTO.class)))
                .thenReturn(createBasicSaveCart());
        //
        int savedCartId = cartController.save(createBasicSaveCartRequestDTO());
        //
        assertThat(savedCartId, is(900));
    }

}
