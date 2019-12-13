package com.msa.repository;

import com.msa.repository.dto.AuthTokenData;
import com.msa.repository.dto.ResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("baseservice")
public interface BaseServiceFeignClient {

    @GetMapping(value = "/auth", consumes = "application/json")
    ResponseDto<AuthTokenData> getAuthToken(@RequestParam(value = "token") String token);

}
