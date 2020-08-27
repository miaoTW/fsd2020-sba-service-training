package com.fsd.sba.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "user")
public interface UserClient {
    @RequestMapping(value = "/api/mentors/name/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    String findMentorNameById(@PathVariable("id") Long id);
}
