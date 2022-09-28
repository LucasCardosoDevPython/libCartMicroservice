package cart.v1;


import book.BookRepository;
import client.ClientRepository;
import cart.Cart;
import cart.CartRepository;
import cart.CartRequestDTO;
import cart.CartResponseDTO;
import cartItem.CartItem;
import cartItem.CartItemDTO;
import cartItem.CartItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@Service
@AllArgsConstructor
public class CartServiceImplementation implements CartService{

    private CartRepository carts;
    private CartItemRepository items;
    private ClientRepository clients;
    private BookRepository books;

    private double loadCartTotal(Cart cart){
        double total = 0;
        for(CartItem item: cart.getCartItems()){
            total+= this.loadItemTotal(item);
        }
        return total;
    }

    private double loadItemTotal(CartItem item){
        return books.findBookPrice(item.getBook())*item.getQuantity();
    }

    public CartResponseDTO toCartResponseDTO(Cart cart){
        LinkedList<CartItemDTO> items;
        boolean done;
        done = cart.getDone() != 0;
        items = new LinkedList<CartItemDTO>();
        for(CartItem i: cart.getCartItems()){
            items.add(toItemDTO(i));
        }
        return new CartResponseDTO(
                clients.findById(cart.getClient()),
                done,
                cart.getTranDate(),
                items,
                this.loadCartTotal(cart)
        );
    }

    private Cart fromCartRequestDTO(CartRequestDTO inDTO){
        int done;
        if(inDTO.isDone()){
            done = 1;
        }else{
            done = 0;
        }
        Cart cart = Cart.builder()
                .client(inDTO.getClientId())
                .done(done)
                .tranDate(LocalDate.now())
                .cartItems(new LinkedList<CartItem>())
                .build();
        for(CartItemDTO i: inDTO.getItems()){
            cart.getCartItems().add(this.fromItemDTO(i, cart));
        };
        return cart;
    }

    public CartItemDTO toItemDTO(CartItem item){
        return new CartItemDTO(
                books.findBookById(item.getBook()),
                item.getQuantity()
        );
    }

    private CartItem fromItemDTO(CartItemDTO itemDTO, Cart cart){
        return CartItem.builder()
                .book(itemDTO.getBook().getIsbn())
                .quantity(itemDTO.getQuantity())
                .cart(cart)
                .build();
    }

    @Override
    @Transactional
    public List<CartResponseDTO> findAllCarts() {

        List<Cart> all = carts.findAll();

        LinkedList<CartResponseDTO> allOut = new LinkedList<CartResponseDTO>();

        for(Cart k: all){
            allOut.add(this.toCartResponseDTO(k));
        }

        return allOut;
    }

    @Override
    @Transactional
    public CartResponseDTO findCartById(Integer id) {
        Cart cart = carts
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Não foi encontrada nenhuma compra com o id " + id + " na base de dados."
                ));

        return this.toCartResponseDTO(cart);
    }

    @Override
    @Transactional
    public List<CartResponseDTO> findCartsByClientId(Integer id) {

        LinkedList<CartResponseDTO> allOut = new LinkedList<CartResponseDTO>();

        for(Cart k: carts.findCartsByClientId(id)){
            allOut.add(this.toCartResponseDTO(k));
        }

        return allOut;
    }

    @Override
    @Transactional
    public Cart save(CartRequestDTO cartDTO) {
        Cart cart = this.fromCartRequestDTO(cartDTO);
        cart = carts.save(cart);
        for(CartItem i: cart.getCartItems()){
            items.save(i);
        }
        return cart;
    }

    @Override
    @Transactional
    public void delete(Integer id) {

        Cart cart = carts
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Não foi encontrada nenhuma compra com o id " + id + " na base de dados."
                ));
        for(CartItem i: cart.getCartItems()){
            items.delete(i);
        }
        carts.delete(cart);
    }

    @Override
    @Transactional
    public void update(Integer id, CartRequestDTO cartDTO) {
        Cart cart = this.fromCartRequestDTO(cartDTO);
        carts
                .findById(id)
                .map(existentCart -> {
                    cart.setId(existentCart.getId());
                    carts.save(cart);
                    return existentCart;
                }).orElseThrow(()-> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Não foi encontrada nenhuma compra com o id "+id+" na base de dados."
                ));
    }
}
