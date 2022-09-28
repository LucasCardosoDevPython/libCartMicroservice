package book;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@FeignClient(url = "localhost:8082/book")
public interface BookRepository {
    @GetMapping("/{isbn}")
    BookDTO findBookById(@RequestParam("isbn") String isbn);

    @GetMapping("/price_finder/{isbn}")
    double findBookPrice(@RequestParam("isbn") String isbn);
}
