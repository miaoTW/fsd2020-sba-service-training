package com.fsd.sba.client;

import com.fsd.sba.domain.Technologies;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "technology")
public interface TechnologiesClient {
    @RequestMapping(value = "/api/technologies/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Technologies> getTechnologies(@PathVariable("id") Long id);
}
