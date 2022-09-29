package library.libCartMicroservice.cart;

import library.libCartMicroservice.cartItem.CartItemResponseDTO;
import library.libCartMicroservice.client.ClientDTO;
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
    private List<CartItemResponseDTO> items;
    private double total;
}
