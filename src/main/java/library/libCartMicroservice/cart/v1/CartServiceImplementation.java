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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;

@Service
@RequiredArgsConstructor
public class CartServiceImplementation implements CartService{

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;
    private final ClientRepository clientRepository;

    private double loadCartTotal(Cart cart){
        double total = 0;
        for(CartItem item: cart.getItems()){
            total+= loadItemTotal(item);
        }
        return total;
    }

    private double loadItemTotal(CartItem item){
        return bookRepository.findBookPrice(item.getBookId())*item.getQuantity();
    }

    public CartResponseDTO toCartResponseDTO(Cart cart){
        LinkedList<CartItemResponseDTO> items = new LinkedList<CartItemResponseDTO>();
        boolean done = cart.getDone() != 0;
        for(CartItem i: cart.getItems()){
            items.add(toItemDTO(i));
        }
        return CartResponseDTO.builder()
                .client(clientRepository.findById(cart.getClient()))
                .done(done)
                .tranDate(cart.getTranDate())
                .items(items)
                .total(loadCartTotal(cart))
                .build();
    }

    private void verifyClientAndBooks(CartRequestDTO inDTO){
        if(!clientRepository.isPresent(inDTO.getClientId())){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Não foi encontrado nenhum cliente com o id "+inDTO.getClientId()+" na base de dados."
            );
        }
        for(CartItemRequestDTO i: inDTO.getItems()){
            this.verifyBookExistence(i.getBookId());
        }
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
                .items(new LinkedList<>())
                .build();
        for(CartItemRequestDTO i: inDTO.getItems()){
            cart.addCartItem(this.fromItemDTO(i, cart));
        };
        return cart;
    }

    private Cart fromCartUpdateRequestDTO(CartRequestDTO inDTO, Integer cartId){
        if(!clientRepository.isPresent(inDTO.getClientId())){
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
                .build();
        for(CartItemRequestDTO i: inDTO.getItems()){
            cart.addCartItem(this.fromItemDTO(i, cart));
        };
        return cart;
    }

    public CartItemResponseDTO toItemDTO(CartItem item){
        return CartItemResponseDTO.builder()
                .book(bookRepository.findBookById(item.getBookId()))
                .quantity(item.getQuantity())
                .build();
    }

    private void verifyBookExistence(String bookId){
        if(!bookRepository.isPresent(bookId)){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Não foi encontrado nenhum cliente com o id "+bookId+" na base de dados."
            );
        }
    }

    private CartItem fromItemDTO(CartItemRequestDTO itemDTO, Cart cart){
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

    private Page<CartResponseDTO> fromPage(Page<Cart> page){
        return page.map(cart -> this.toCartResponseDTO(cart));
    }//CartServiceImplementation::toCartResponseDTO

    private Cart getCartFromId(Integer cartId){
        return cartRepository
                .findById(cartId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Não foi encontrada nenhuma compra com o id " + cartId + " na base de dados."
                ));
    }

    @Override
    @Transactional
    public Page<CartResponseDTO> findAllCarts(Pageable pageable) {
        return this.fromPage(cartRepository.findAll(pageable));
    }

    @Override
    @Transactional
    public CartResponseDTO findCartById(Integer id) {
        Cart cart = this.getCartFromId(id);
        return this.toCartResponseDTO(cart);
    }

    @Override
    @Transactional
    public Page<CartResponseDTO> findCartsByClientId(Integer id, Pageable pageable) {
        return this.fromPage(cartRepository.findCartsByClientId(id, pageable));
    }

    @Override
    @Transactional
    public Cart save(CartRequestDTO cartDTO) {
        this.verifyClientAndBooks(cartDTO);
        Cart cart = this.fromCartRequestDTO(cartDTO);
        cart = cartRepository.save(cart);
        for(CartItem i: cart.getItems()){
            cartItemRepository.save(i);
        }
        return cart;
    }

    @Override
    @Transactional
    public void delete(Integer id) {

        Cart cart = this.getCartFromId(id);
        for(CartItem i: cart.getItems()){
            cartItemRepository.delete(i);
        }
        cartRepository.delete(cart);
    }

    @Override
    @Transactional
    public void update(Integer cartId, CartRequestDTO cartDTO) {
        Cart oldCart = this.getCartFromId(cartId);
        Cart newCart = this.fromCartUpdateRequestDTO(cartDTO, cartId);
        newCart.setId(oldCart.getId());
        CartItem oldItem;
        for(CartItem newItem: newCart.getItems()){
            if(oldCart.containsBook(newItem.getBookId())){
                oldItem = oldCart.getItem(newItem.getBookId());
                newItem.setId(oldItem.getId());
                newItem.setQuantity(
                        oldItem.getQuantity() + newItem.getQuantity()
                );
            }
            cartItemRepository.save(newItem);
        }
        cartRepository.save(newCart);
    }

    @Override
    @Transactional
    public void addItem(Integer cartId, CartItemRequestDTO itemDTO) {
        Cart oldCart = this.getCartFromId(cartId);
        this.verifyBookExistence(itemDTO.getBookId());
        CartItem newItem = this.fromItemDTO(itemDTO, oldCart);
        if(oldCart.containsBook(newItem.getBookId())){
            CartItem oldItem = oldCart.getItem(newItem.getBookId());
            newItem.setId(oldItem.getId());
            newItem.setQuantity(
                    oldItem.getQuantity() + newItem.getQuantity()
            );
        }
        cartItemRepository.save(newItem);
    }

    @Override
    @Transactional
    public void removeItem(Integer cartId, CartItemRequestDTO itemDTO) {
        Cart oldCart = this.getCartFromId(cartId);
        if (oldCart.containsBook(itemDTO.getBookId())) {
            if(itemDTO.getQuantity()==0){
                cartItemRepository.delete(oldCart.getItem(itemDTO.getBookId()));
            }else{
                this.verifyBookExistence(itemDTO.getBookId());
                CartItem newItem = this.fromItemDTO(itemDTO, oldCart);
                CartItem oldItem = oldCart.getItem(newItem.getBookId());
                newItem.setId(oldItem.getId());
                newItem.setQuantity(
                        oldItem.getQuantity() - newItem.getQuantity()
                );
                if (newItem.getQuantity() <= 0) {
                    cartItemRepository.delete(newItem);
                } else {
                    cartItemRepository.save(newItem);
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
