package library.libCartMicroservice;

import library.libCartMicroservice.book.BookRepository;
import library.libCartMicroservice.builders.BookBuilders;
import library.libCartMicroservice.builders.CartBuilders;
import library.libCartMicroservice.builders.ClientBuilders;
import library.libCartMicroservice.cart.Cart;
import library.libCartMicroservice.cart.CartRepository;
import library.libCartMicroservice.cart.CartResponseDTO;
import library.libCartMicroservice.cart.v1.CartServiceImplementation;
import library.libCartMicroservice.cartItem.CartItem;
import library.libCartMicroservice.cartItem.CartItemRepository;
import library.libCartMicroservice.client.ClientRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import static library.libCartMicroservice.builders.BookBuilders.createBook1;
import static library.libCartMicroservice.builders.BookBuilders.createBook2;
import static library.libCartMicroservice.builders.CartBuilders.createBasicSaveCart;
import static library.libCartMicroservice.builders.CartBuilders.createBasicSaveCartRequestDTO;
import static library.libCartMicroservice.builders.CartBuilders.createCartWithItemRequestDTOWithInvalidBook;
import static library.libCartMicroservice.builders.CartBuilders.createCartWithMultipleItems;
import static library.libCartMicroservice.builders.CartBuilders.createCartWithoutItems;
import static library.libCartMicroservice.builders.CartBuilders.createEmptyCartOptional;
import static library.libCartMicroservice.builders.CartItemBuilders.createBasicSaveCartItem;
import static library.libCartMicroservice.builders.CartItemBuilders.createBasicSaveCartItemRequestDTO;
import static library.libCartMicroservice.builders.ClientBuilders.createClient1;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Tag("service")
@DisplayName("Valida as funcionalidades relacionadas a compras")
class LibCartMicroserviceServiceTests {

	@Mock
	private CartRepository cartRepository;
	@Mock
	private CartItemRepository itemRepository;
	@Mock
	private BookRepository bookRepository;
	@Mock
	private ClientRepository clientRepository;
	@InjectMocks
	private CartServiceImplementation cartService;

	@Test
	@DisplayName("Deve salvar uma compra")
	void shouldSaveCart() {
		when(clientRepository.isPresent(anyInt()))
				.thenReturn(true);
		when(bookRepository.isPresent(anyString()))
				.thenReturn(true);
		when(cartRepository.save(any(Cart.class)))
				.thenReturn(createBasicSaveCart());
		ArgumentCaptor<CartItem> cartItemCapture = ArgumentCaptor.forClass(CartItem.class);
		//
		Cart savedCart = cartService.save(createBasicSaveCartRequestDTO());
		//
		verify(itemRepository).save(cartItemCapture.capture());
		CartItem savedItem = cartItemCapture.getValue();
		assertAll("Cart",
				() -> assertThat(savedCart.getId(), is(900)),
				() -> assertThat(savedCart.getClient(), is(1)),
				() -> assertThat(savedCart.getDone(), is(0)),
				() -> assertNotNull(savedCart.getTranDate())
		);
		assertAll("Item",
				() -> assertThat(savedItem.getId(), is(901)),
				() -> assertThat(savedItem.getBookId(), is("0000000000001")),
				() -> assertThat(savedItem.getQuantity(), is(2))
		);
	}

	@Test
	@DisplayName("Deve tentar salvar uma compra mas lançar uma excessão")
	void shouldThrowClientNotFoundException() {
		when(clientRepository.isPresent(anyInt()))
				.thenReturn(false);
		//
		assertThrows(
				ResponseStatusException.class,
				() -> cartService.save(createBasicSaveCartRequestDTO())
		);
		//
		verify(clientRepository).isPresent(eq(1));
		verify(bookRepository, times(0)).isPresent(anyString());
	}

	@Test
	@DisplayName("Deve tentar excluir uma compra que não existe")
	void shouldThrowCartNotFoundException(){
		when(cartRepository.findById(eq(900)))
				.thenReturn(createEmptyCartOptional());
		//
		assertThrows(
				ResponseStatusException.class,
				() -> cartService.delete(900)
		);
		//
		verify(itemRepository, times(0)).delete(any(CartItem.class));
		verify(cartRepository, times(0)).delete(any(Cart.class));
	}

	@Test
	@DisplayName("Deve tentar remover um item de uma compra que não o possui")
	void shouldNotDeleteInexistentItemFromCart(){
		when(cartRepository.findById(eq(900)))
				.thenReturn(createCartWithoutItems());
		//
		assertThrows(
				ResponseStatusException.class,
				() -> cartService.removeItem(900, createBasicSaveCartItemRequestDTO())
		);
		//
		verify(itemRepository, times(0)).delete(any(CartItem.class));
		verify(itemRepository, times(0)).save(any(CartItem.class));
		verify(cartRepository, times(0)).delete(any(Cart.class));
	}

	@Test
	@DisplayName("Deve tentar remover um item de uma compra que não existe")
	void shouldNotDeleteItemFromInexistentCart(){
		when(cartRepository.findById(eq(900)))
				.thenReturn(createEmptyCartOptional());
		//
		assertThrows(
				ResponseStatusException.class,
				() -> cartService.removeItem(900, createBasicSaveCartItemRequestDTO()));
		//
		verify(itemRepository, times(0)).delete(any(CartItem.class));
		verify(itemRepository, times(0)).save(any(CartItem.class));
		verify(cartRepository, times(0)).delete(any(Cart.class));
	}

	@Test
	@DisplayName("Deve tentar salvar uma compra com um item que possui um livro inexistente")
	void shouldNotSaveCartWithInexistentBook(){
		when(clientRepository.isPresent(anyInt()))
				.thenReturn(true);
		when(bookRepository.isPresent(eq("0000000000001")))
				.thenReturn(true);
		when(bookRepository.isPresent(eq("0000000000000")))
				.thenReturn(false);
		//
		assertThrows(
				ResponseStatusException.class,
				() -> cartService.save(createCartWithItemRequestDTOWithInvalidBook())
		);
		//
		verify(clientRepository, times(1)).isPresent(eq(1));
		verify(bookRepository, times(2)).isPresent(anyString());
		verify(itemRepository, times(0)).save(any(CartItem.class));
		verify(cartRepository, times(0)).save(any(Cart.class));
	}

	@Test
	@DisplayName("Deve retornar o valor correto da soma dos itens de uma compra")
	void shouldReturnCartTotal(){
		when(cartRepository.findById(eq(900)))
				.thenReturn(createCartWithMultipleItems());
		when(bookRepository.findBookById("0000000000001"))
				.thenReturn(createBook1());
		when(bookRepository.findBookPrice("0000000000001"))
				.thenReturn(20.0);
		when(bookRepository.findBookById("0000000000002"))
				.thenReturn(createBook2());
		when(bookRepository.findBookPrice("0000000000002"))
				.thenReturn(10.0);
		when(clientRepository.findById(eq(1)))
				.thenReturn(createClient1());
		//
		CartResponseDTO cartDTO = cartService.findCartById(900);
		//
		assertThat(cartDTO.getTotal(), is(90.0));
	}

}
