package library.libCartMicroservice.cartItem;

import library.libCartMicroservice.book.BookDTO;
import library.libCartMicroservice.book.BookRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CartItemResponseDTO {
    private Integer id;
    private BookDTO book;
    private Integer quantity;

    public static CartItemResponseDTO toDTO(CartItem item, BookRepository books){
        return CartItemResponseDTO.builder()
                .id(item.getId())
                .book(books.findBookById(item.getBookId()))
                .quantity(item.getQuantity())
                .build();
    }
}
