package cart;

import cartItem.CartItemDTO;
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
    private List<CartItemDTO> items;
}
