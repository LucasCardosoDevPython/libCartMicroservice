package library.libCartMicroservice.cart;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartRepository  extends JpaRepository<Cart, Integer> {

    @Query(value = "select * from cart k where k.client_id = :id", nativeQuery = true)
    Page<Cart> findCartsByClientId(@Param("id") Integer id, Pageable pageable);

}
