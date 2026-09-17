package com.CodingBrajmohan.postService.client;

import com.CodingBrajmohan.postService.dto.PersonDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "connection-service", path = "/connection", url = "${CONNECTIONS_SERVICE_URI:}")
public interface ConnectionServiceClient {

    @GetMapping("/core/{userId}/first-degree")
    List<PersonDto> getFirstDegreeConnection(@PathVariable Long userId);
}
