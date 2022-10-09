package library.libCartMicroservice.cartItem;

import library.libCartMicroservice.book.BookRepository;
import library.libCartMicroservice.cart.Cart;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name="cart_item")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "KItemSeq")
    @SequenceGenerator(name = "KItemSeq", sequenceName = "cart_item_id_generator", allocationSize = 1)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;
    @Column(name = "book_id")
    private String bookId;
    private Integer quantity;

}

