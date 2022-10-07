package library.libCartMicroservice.cart.v1;

import library.libCartMicroservice.cart.Cart;
import library.libCartMicroservice.cart.CartRequestDTO;
import library.libCartMicroservice.cart.CartResponseDTO;
import library.libCartMicroservice.cartItem.CartItemRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CartService {
    Page<CartResponseDTO> findAllCarts(Pageable pageable);
    CartResponseDTO findCartById(Integer id);
    Page<CartResponseDTO> findCartsByClientId(Integer id, Pageable pageable);
    Cart save(CartRequestDTO cartDTO);
    void delete(Integer id);
    void update(Integer id, CartRequestDTO cartDTO);
    void addItem(Integer cartId, CartItemRequestDTO itemDTO);
    void removeItem(Integer cartId, CartItemRequestDTO itemDTO);
}
