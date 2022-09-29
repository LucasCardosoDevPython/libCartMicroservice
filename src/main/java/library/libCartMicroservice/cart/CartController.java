package library.libCartMicroservice.cart;

import library.libCartMicroservice.cart.v1.CartService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.openfeign.EnableFeignClients;
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

import java.util.List;

@RestController
@RequestMapping(value = "/cart")
@EnableFeignClients(basePackages = "library.libCartMicroservice")
@AllArgsConstructor
public class CartController {

    private CartService service;

    @GetMapping
    public List<CartResponseDTO> findAllCarts(){
        return service.findAllCarts();
    }

    @GetMapping("/{id}")
    public CartResponseDTO findCartById(@PathVariable("id") Integer id){
        return service.findCartById(id);
    }

    @GetMapping("/client/{id}")
    public List<CartResponseDTO> findCartsByClientId(@PathVariable("id") Integer id){
        return service.findCartsByClientId(id);
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

}
