package library.libCartMicroservice.cart;

import library.libCartMicroservice.cart.v1.CartService;
import library.libCartMicroservice.cartItem.CartItemRequestDTO;
import lombok.AllArgsConstructor;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/cart")
@EnableFeignClients(basePackages = "library.libCartMicroservice")
@AllArgsConstructor
public class CartController {

    private CartService service;

    @GetMapping
    public Page<CartResponseDTO> findAllCarts(Pageable pageable){
        return service.findAllCarts(pageable);
    }

    @GetMapping("/{id}")
    public CartResponseDTO findCartById(@PathVariable("id") Integer id){
        return service.findCartById(id);
    }

    @GetMapping("/client/{id}")
    public Page<CartResponseDTO> findCartsByClientId(@PathVariable("id") Integer id, Pageable pageable){
        return service.findCartsByClientId(id, pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Integer save(@RequestBody CartRequestDTO cart){
        return service.save(cart).getId();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Integer id){
        service.delete(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable("id") Integer id, @RequestBody CartRequestDTO cart){
        service.update(id,cart);
    }

    @PutMapping("/items/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addItem(@PathVariable("id") Integer id, @RequestBody CartItemRequestDTO itemDTO){
        service.addItem(id, itemDTO);
    }

    @DeleteMapping("/items/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeItem(@PathVariable("id") Integer id, @RequestBody CartItemRequestDTO itemDTO){
        service.removeItem(id, itemDTO);
    }

}
