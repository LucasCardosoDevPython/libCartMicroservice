package library.libCartMicroservice.cart;

import library.libCartMicroservice.cartItem.CartItemRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CartRequestDTO {
    private Integer clientId;
    private boolean done;
    private List<CartItemRequestDTO> items;
}
