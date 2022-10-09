package library.libCartMicroservice.builders;

import library.libCartMicroservice.client.ClientDTO;

import java.time.LocalDate;

public class ClientBuilders {

    public static ClientDTO createClient1(){
        return ClientDTO.builder()
                .email("lucas@phoebus.br")
                .name("lucas")
                .birthDate(LocalDate.parse("1999-08-15"))
                .phone("99889999899")
                .sex("M")
                .build();
    }

}
