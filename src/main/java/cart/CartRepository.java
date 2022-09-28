package cart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface CartRepository  extends JpaRepository<Cart, Integer> {

    @Query("select k from Cart k left join fetch k.cartItems where k.id = :id")
    Cart findCartFetchBooks(@Param("id") Integer id);

    @Query(value = "select * from cart k where k.client_id = :id", nativeQuery = true)
    Set<Cart> findCartsByClientId(@Param("id") Integer id);

}
