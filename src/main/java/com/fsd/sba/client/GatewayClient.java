package com.fsd.sba.client;

import com.fsd.sba.service.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "gateway")
public interface GatewayClient {

    @RequestMapping(value = "/api/users/{login}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    UserDTO getUser(@PathVariable("login") String login);
}
