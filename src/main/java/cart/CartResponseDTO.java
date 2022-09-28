package cart;

import cartItem.CartItemDTO;
import client.ClientDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CartResponseDTO {
    private ClientDTO client;
    private boolean done;
    private LocalDate tranDate;
    private List<CartItemDTO> items;
    private double total;
}
