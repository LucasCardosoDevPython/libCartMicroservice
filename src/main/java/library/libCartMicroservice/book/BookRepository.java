package library.libCartMicroservice.book;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(url = "localhost:8082/book", name = "bookMicroservice")
public interface BookRepository {
    @GetMapping("/{isbn}")
    BookDTO findBookById(@RequestParam("isbn") String isbn);

    @GetMapping("/price_finder/{isbn}")
    double findBookPrice(@RequestParam("isbn") String isbn);
}
