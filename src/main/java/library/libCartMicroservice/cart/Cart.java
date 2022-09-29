package library.libCartMicroservice.cart;

import library.libCartMicroservice.cartItem.CartItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name= "cart")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "CartSeq")
    @SequenceGenerator(name = "CartSeq", sequenceName = "cart_id_generator", allocationSize = 1)
    private Integer id;
    @Column(name = "client_id")
    private Integer client;
    @Column(name = "trans_date")
    private LocalDate tranDate;
    private Integer done;
    @OneToMany(mappedBy = "cart",fetch = FetchType.EAGER)
    private List<CartItem> cartItems;

}

