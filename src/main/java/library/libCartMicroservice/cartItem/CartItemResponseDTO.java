package library.libCartMicroservice.cartItem;

import library.libCartMicroservice.book.BookDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CartItemResponseDTO {
    private BookDTO book;
    private Integer quantity;
}
