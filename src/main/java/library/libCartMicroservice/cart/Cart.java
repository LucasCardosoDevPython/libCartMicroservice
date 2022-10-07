package library.libCartMicroservice.cart;

import library.libCartMicroservice.book.BookRepository;
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
import java.util.Map;

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
    private Map<String, CartItem> items;

    public double getTotal(BookRepository books){
        double total = 0;
        for(CartItem item: this.getCartItems()){
            total+= item.getTotal(books);
        }
        return total;
    }

    public void addCartItem(CartItem item){
        items.put(item.getBookId(), item);
    }

    public void removeCartItem(CartItem item){
        items.remove(item.getBookId());
    }

    public List<CartItem> getCartItems(){
        return (List<CartItem>) this.items.values();
    }

    public boolean containsBook(String bookId){
        return this.items.containsKey(bookId);
    }

    public CartItem getItem(String bookId){
        return this.items.get(bookId);
    }

}

