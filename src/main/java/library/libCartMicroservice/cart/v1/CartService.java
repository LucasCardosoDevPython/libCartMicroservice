package library.libCartMicroservice.cart.v1;

import library.libCartMicroservice.cart.Cart;
import library.libCartMicroservice.cart.CartRequestDTO;
import library.libCartMicroservice.cart.CartResponseDTO;
import library.libCartMicroservice.cartItem.CartItemRequestDTO;

import java.util.List;

public interface CartService {
    List<CartResponseDTO> findAllCarts();
    CartResponseDTO findCartById(Integer id);
    List<CartResponseDTO> findCartsByClientId(Integer id);
    Cart save(CartRequestDTO cartDTO);
    void delete(Integer id);
    void update(Integer id, CartRequestDTO cartDTO);
    void addItem(Integer cartId, CartItemRequestDTO itemDTO);
    void removeItem(Integer cartId, CartItemRequestDTO itemDTO);
}
