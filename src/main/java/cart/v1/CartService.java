package cart.v1;

import cart.Cart;
import cart.CartRequestDTO;
import cart.CartResponseDTO;

import java.util.List;

public interface CartService {
    List<CartResponseDTO> findAllCarts();
    CartResponseDTO findCartById(Integer id);
    List<CartResponseDTO> findCartsByClientId(Integer id);
    Cart save(CartRequestDTO cartDTO);
    void delete(Integer id);
    void update(Integer id, CartRequestDTO cartDTO);
}
