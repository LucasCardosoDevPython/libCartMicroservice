package library.libCartMicroservice.cart;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository  extends JpaRepository<Cart, Integer> {

    Page<Cart> findByClient(Integer client, Pageable pageable);

}
