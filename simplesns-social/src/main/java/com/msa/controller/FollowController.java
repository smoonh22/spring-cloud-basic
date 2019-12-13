package com.msa.controller;

import java.util.ArrayList;
import java.util.List;

import com.msa.repository.BaseServiceFeignClient;
import com.msa.repository.dto.AuthTokenData;
import com.msa.repository.dto.ResponseDto;
import com.netflix.discovery.converters.Auto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.msa.controller.dto.FollowDto;
import com.msa.controller.dto.ResultDto;
import com.msa.domain.AuthToken;
import com.msa.domain.Follow;
import com.msa.repository.AuthTokenRestRepository;
import com.msa.service.FollowService;

@CrossOrigin
@RestController
public class FollowController {
	private static Logger log = LoggerFactory.getLogger(FollowController.class);
	
	private final FollowService followService;
	private final BaseServiceFeignClient baseServiceFeignClient;

	public FollowController(FollowService followService, AuthTokenRestRepository authTokenRestRepository, BaseServiceFeignClient baseServiceFeignClient) {
		this.followService = followService;
		this.baseServiceFeignClient = baseServiceFeignClient;
	}

	@PostMapping("/follow")
	public ResultDto addFollow(@RequestBody FollowDto dto, @RequestHeader(value="accesstoken") String accesstoken) {
		// AuthToken authToken = authService.getAuthToken(accesstoken);
		ResponseDto<AuthTokenData> authTokenData = baseServiceFeignClient.getAuthToken(accesstoken);
		AuthTokenData authToken = authTokenData.getData();

		if(authToken == null) {
			return new ResultDto(4002, "OK", "Authentication Failed");
		}
		
		followService.addFollow(dto.getFolloweeId(), authToken.getUserId());
		
		return new ResultDto(200, "OK", "Success");
	}
	
	@DeleteMapping("/follow")
	public ResultDto deleteFollow(@RequestBody FollowDto dto, @RequestHeader(value="accesstoken") String accesstoken) {
		// AuthToken authToken = authService.getAuthToken(accesstoken);
		ResponseDto<AuthTokenData> authTokenData = baseServiceFeignClient.getAuthToken(accesstoken);
		AuthTokenData authToken = authTokenData.getData();
				
		if(authToken == null) {
			return new ResultDto(4002, "OK", "Authentication Failed");
		}
		
		followService.deleteFollow(dto.getFolloweeId(), authToken.getUserId());
		
		return new ResultDto(200, "OK", "Success");
	}
	
	@GetMapping("/followee")
	public ResultDto getFolloweeList(@RequestParam Long userId, @RequestParam String userIds) {
		log.info("GET /followee called");
		String[] idArray = userIds.split(",");
		List<Long> userIdList = new ArrayList<>();
		for(String id : idArray) {
			userIdList.add(Long.valueOf(id));
		}
		
		List<Follow> followList = followService.getFolloweeList(userId, userIdList);
		
		return new ResultDto(200, "OK", followList);
	}
}
