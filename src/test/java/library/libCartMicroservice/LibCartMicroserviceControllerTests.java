package library.libCartMicroservice;

import library.libCartMicroservice.cart.CartController;
import library.libCartMicroservice.cart.v1.CartServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("cotroller")
@DisplayName("Valida os endpoints relacionados a compras")
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = CartController.class)
public class LibCartMicroserviceControllerTests {

    @MockBean
    private CartServiceImpl cartService;
    @Autowired
    private MockMvc mockMvc;

    private static String readJson(String file) throws Exception {
        byte[] bytes = Files.readAllBytes(
                Paths.get("src/test/resources/requestJsons/" + file).toAbsolutePath());
        return new String(bytes);
    }
    @Test
    @DisplayName("Deve salvar uma compra e retornar o id dela")
    void shouldSaveCart() throws  Exception{
        mockMvc.perform(post("/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readJson("simple_cart_save.json")))
                .andExpect(status().isCreated());

    }

}
