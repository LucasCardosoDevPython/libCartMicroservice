package library.libCartMicroservice.builders;

import library.libCartMicroservice.cart.Cart;
import library.libCartMicroservice.cart.CartRequestDTO;
import library.libCartMicroservice.cart.CartResponseDTO;
import library.libCartMicroservice.cartItem.CartItem;
import library.libCartMicroservice.cartItem.CartItemRequestDTO;
import library.libCartMicroservice.cartItem.CartItemResponseDTO;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.Optional;

import static library.libCartMicroservice.builders.CartItemBuilders.createBasicSaveCartItem;
import static library.libCartMicroservice.builders.CartItemBuilders.createBasicSaveCartItemRequestDTO;
import static library.libCartMicroservice.builders.CartItemBuilders.createCartItemRequestDTOWithInvalidBook;
import static library.libCartMicroservice.builders.CartItemBuilders.createMultipleCartItems;
import static library.libCartMicroservice.builders.ClientBuilders.createClient1;

public class CartBuilders {
    public static Cart createBasicSaveCart(){
        LinkedList<CartItem> itemsList = new LinkedList<>();
        itemsList.add(createBasicSaveCartItem());
        return Cart.builder()
                .id(900)
                .client(1)
                .done(0)
                .tranDate(LocalDate.now())
                .items(itemsList)
                .build();
    }

    public static CartRequestDTO createBasicSaveCartRequestDTO(){
        LinkedList<CartItemRequestDTO> itemsDTOList = new LinkedList<>();
        itemsDTOList.add(createBasicSaveCartItemRequestDTO());
        return CartRequestDTO.builder()
                .clientId(1)
                .done(false)
                .items(itemsDTOList)
                .build();
    }

    public static CartResponseDTO createBasicSaveCartResponseDTO(){
        LinkedList<CartItemResponseDTO> itemsDTOList = new LinkedList<>();
        return CartResponseDTO.builder()
                .client(createClient1())
                .done(false)
                .items(itemsDTOList)
                .build();
    }

    public static Optional<Cart> createEmptyCartOptional(){
        return Optional.empty();
    }

    public static Optional<Cart> createCartWithoutItems(){
        return Optional.of(
                Cart.builder()
                    .id(900)
                    .client(1)
                    .done(0)
                    .tranDate(LocalDate.now())
                    .items(new LinkedList<>())
                    .build()
        );
    }

    public static CartRequestDTO createCartWithItemRequestDTOWithInvalidBook(){
        LinkedList<CartItemRequestDTO> itemsDTOList = new LinkedList<>();
        itemsDTOList.add(createBasicSaveCartItemRequestDTO());
        itemsDTOList.add(createCartItemRequestDTOWithInvalidBook());
        return CartRequestDTO.builder()
                .clientId(1)
                .done(false)
                .items(itemsDTOList)
                .build();
    }

    public static Optional<Cart> createCartWithMultipleItems(){
        return Optional.of(Cart.builder()
                .id(900)
                .client(1)
                .done(0)
                .tranDate(LocalDate.now())
                .items(createMultipleCartItems())
                .build()
        );
    }
}
