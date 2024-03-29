package com.msa.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import com.msa.domain.Follow;
import com.msa.repository.dto.FollowData;
import com.msa.repository.dto.FollowResponseDto;


@Repository
public class FollowRestRepository {

	private final SocialFeignClient socialFeignClient;

	public FollowRestRepository(SocialFeignClient socialFeignClient) {
		this.socialFeignClient = socialFeignClient;
	}


	public List<Follow> getFolloweeList(Long userId, List<Long> userIdList) {
//		StringBuilder idStr = new StringBuilder();
//		for(Long id : userIdList) {
//			idStr = idStr.length() > 0 ? idStr.append(",").append(id) : idStr.append(id);
//		}

		FollowResponseDto response = socialFeignClient.getFollowee(userId, userIdList);
		
		List<FollowData> followDataList = response.getData();
		List<Follow> followList = new ArrayList<>();
		for(FollowData data : followDataList) {
			followList.add(new Follow(data.getFolloweeId(), data.getFollowerId()));
		}
		
		return followList;
	}

}