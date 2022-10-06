package library.libCartMicroservice.cart.v1;


import library.libCartMicroservice.book.BookRepository;
import library.libCartMicroservice.cart.Cart;
import library.libCartMicroservice.cart.CartRepository;
import library.libCartMicroservice.cart.CartRequestDTO;
import library.libCartMicroservice.cart.CartResponseDTO;
import library.libCartMicroservice.cartItem.CartItem;
import library.libCartMicroservice.cartItem.CartItemRepository;
import library.libCartMicroservice.cartItem.CartItemRequestDTO;
import library.libCartMicroservice.cartItem.CartItemResponseDTO;
import library.libCartMicroservice.client.ClientRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@Service
@AllArgsConstructor
public class CartServiceImplementation implements CartService{

    private CartRepository carts;
    private CartItemRepository items;
    private BookRepository books;
    private ClientRepository clients;

    private double loadCartTotal(Cart cart){
        double total = 0;
        for(CartItem item: cart.getCartItems()){
            total+= this.loadItemTotal(item);
        }
        return total;
    }

    private double loadItemTotal(CartItem item){
        return books.findBookPrice(item.getBookId())*item.getQuantity();
    }

    public CartResponseDTO toCartResponseDTO(Cart cart){
        LinkedList<CartItemResponseDTO> items = new LinkedList<CartItemResponseDTO>();
        boolean done;
        done = cart.getDone() != 0;
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
        if(!clients.isPresent(inDTO.getClientId())){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Não foi encontrado nenhum cliente com o id "+inDTO.getClientId()+" na base de dados."
            );
        }
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
                .items(new HashMap<String, CartItem>())
                .build();
        for(CartItemRequestDTO i: inDTO.getItems()){
            cart.addCartItem(this.fromItemDTO(i, cart));
        };
        return cart;
    }

    private Cart fromCartUpdateRequestDTO(CartRequestDTO inDTO, Integer cartId){
        if(!clients.isPresent(inDTO.getClientId())){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Não foi encontrado nenhum cliente com o id "+inDTO.getClientId()+" na base de dados."
            );
        }
        int done;
        if(inDTO.isDone()){
            done = 1;
        }else{
            done = 0;
        }
        Cart cart = Cart.builder()
                .id(cartId)
                .client(inDTO.getClientId())
                .done(done)
                .tranDate(LocalDate.now())
                .items(new HashMap<String, CartItem>())
                .build();
        for(CartItemRequestDTO i: inDTO.getItems()){
            cart.addCartItem(this.fromItemDTO(i, cart));
        };
        return cart;
    }

    public CartItemResponseDTO toItemDTO(CartItem item){
        return new CartItemResponseDTO(
                books.findBookById(item.getBookId()),
                item.getQuantity()
        );
    }

    private CartItem fromItemDTO(CartItemRequestDTO itemDTO, Cart cart){
        if(!books.isPresent(itemDTO.getBookId())){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Não foi encontrado nenhum cliente com o id "+itemDTO.getBookId()+" na base de dados."
            );
        }
        if(itemDTO.getQuantity()<0){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A quantidade de um item não pode ser menor que 0. "+
                            "Livro: "+itemDTO.getBookId()+", "+
                            "Quantidade: "+itemDTO.getQuantity()
            );
        }
        return CartItem.builder()
                .bookId(itemDTO.getBookId())
                .quantity(itemDTO.getQuantity())
                .cart(cart)
                .build();
    }

    private Cart getCartFromId(Integer cartId){
        return carts
                .findById(cartId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Não foi encontrada nenhuma compra com o id " + cartId + " na base de dados."
                ));
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
        Cart cart = this.getCartFromId(id);

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

        Cart cart = this.getCartFromId(id);
        for(CartItem i: cart.getCartItems()){
            items.delete(i);
        }
        carts.delete(cart);
    }

    @Override
    @Transactional
    public void update(Integer cartId, CartRequestDTO cartDTO) {
        Cart oldCart = this.getCartFromId(cartId);
        Cart newCart = this.fromCartUpdateRequestDTO(cartDTO, cartId);
        newCart.setId(oldCart.getId());
        CartItem oldItem;
        for(CartItem newItem: newCart.getCartItems()){
            if(oldCart.containsBook(newItem.getBookId())){
                oldItem = oldCart.getItem(newItem.getBookId());
                newItem.setId(oldItem.getId());
                newItem.setQuantity(
                        oldItem.getQuantity() + newItem.getQuantity()
                );
            }
            items.save(newItem);
        }
        carts.save(newCart);
    }

    @Override
    @Transactional
    public void addItem(Integer cartId, CartItemRequestDTO itemDTO) {
        Cart oldCart = this.getCartFromId(cartId);
        CartItem newItem = this.fromItemDTO(itemDTO, oldCart);
        if(oldCart.containsBook(newItem.getBookId())){
            CartItem oldItem = oldCart.getItem(newItem.getBookId());
            newItem.setId(oldItem.getId());
            newItem.setQuantity(
                    oldItem.getQuantity() + newItem.getQuantity()
            );
        }
        items.save(newItem);
    }

    @Override
    @Transactional
    public void removeItem(Integer cartId, CartItemRequestDTO itemDTO) {
        Cart oldCart = this.getCartFromId(cartId);
        if (oldCart.containsBook(itemDTO.getBookId())) {
            if(itemDTO.getQuantity()==0){
                items.delete(oldCart.getItem(itemDTO.getBookId()));
            }else{
                CartItem newItem = this.fromItemDTO(itemDTO, oldCart);
                CartItem oldItem = oldCart.getItem(newItem.getBookId());
                newItem.setId(oldItem.getId());
                newItem.setQuantity(
                        oldItem.getQuantity() - newItem.getQuantity()
                );
                if (newItem.getQuantity() <= 0) {
                    items.delete(newItem);
                } else {
                    items.save(newItem);
                }
            }
        }else{
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A compra " + oldCart.getId() + " já não possui o libro de ID " + itemDTO.getBookId()
            );
        }
    }
}
