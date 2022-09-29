package library.libCartMicroservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(url = "localhost:8081/client", name = "clientMicroservice")
public interface ClientRepository {
    @GetMapping(value = "/{id}")
    ClientDTO findById(@RequestParam("id") Integer clientId);
}
