package com.msa.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import com.msa.domain.AuthToken;
import com.msa.repository.dto.AuthTokenData;
import com.msa.repository.dto.ResponseDto;

@Repository
public class AuthTokenRestRepository {

	private final RestTemplate restTemplate;

	public AuthTokenRestRepository(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public AuthToken getAuthToken(String token) {
		ResponseDto<AuthTokenData> response = restTemplate.
				exchange("http://baseservice/auth?token={token}",
				HttpMethod.GET, null, 
				new ParameterizedTypeReference<ResponseDto<AuthTokenData>>() {}, token).getBody();
		
		AuthTokenData authTokenData = response.getData();
		AuthToken authToken = new AuthToken(authTokenData.getToken(), authTokenData.getUserId());
		
		return authToken;
	}

}
