package library.libCartMicroservice.builders;

import library.libCartMicroservice.cartItem.CartItem;
import library.libCartMicroservice.cartItem.CartItemRequestDTO;

import java.util.LinkedList;
import java.util.List;

public class CartItemBuilders {
     public static CartItem createBasicSaveCartItem(){
         return CartItem.builder()
                 .id(901)
                 .bookId("0000000000001")
                 .quantity(2)
                 .build();
     }

    public static CartItemRequestDTO createBasicSaveCartItemRequestDTO(){
         return CartItemRequestDTO.builder()
                 .bookId("0000000000001")
                 .quantity(2)
                 .build();
    }

    public static CartItemRequestDTO createCartItemRequestDTOWithInvalidBook(){
         return CartItemRequestDTO.builder()
                 .bookId("0000000000000")
                 .quantity(5)
                 .build();
    }

    public static List<CartItem> createMultipleCartItems(){
         LinkedList<CartItem> items = new LinkedList<>();
        items.add(
                CartItem.builder()
                        .bookId("0000000000001")
                        .quantity(2)
                        .build()
        );
        items.add(
                CartItem.builder()
                        .bookId("0000000000002")
                        .quantity(5)
                        .build()
        );
         return items;
    }

}
