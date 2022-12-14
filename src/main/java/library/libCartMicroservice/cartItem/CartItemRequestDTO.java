package library.libCartMicroservice.cartItem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CartItemRequestDTO {
    private Integer id;
    private String bookId;
    private Integer quantity;
}
