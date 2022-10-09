package library.libCartMicroservice.builders;

import library.libCartMicroservice.book.BookDTO;

public class BookBuilders {

    public static BookDTO createBook1(){
        return BookDTO.builder()
                .isbn("0000000000001")
                .stock(2)
                .price(20.0)
                .build();
    }

    public static BookDTO createBook2(){
        return BookDTO.builder()
                .isbn("0000000000002")
                .stock(5)
                .price(10.0)
                .build();
    }

}
