package com.msa.service;

import java.util.List;

import javax.transaction.Transactional;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.msa.domain.Follow;
import com.msa.repository.FollowRepository;

@Service
public class FollowServiceImpl implements FollowService {

	private final FollowRepository followRepository;

	public FollowServiceImpl(FollowRepository followRepository) {
		this.followRepository = followRepository;
	}

	@Override
	@HystrixCommand
	public Follow addFollow(Long followeeId, Long followerId) {
		
		Follow follow = new Follow(followeeId, followerId);
		
		Follow result = followRepository.saveAndFlush(follow);
		
		return result;
	}

	@Override
	public List<Follow> getFollowerList(Long followeeId) {

		List<Follow> followList = followRepository.findByFolloweeId(followeeId);
		
		return followList;
	}

	@Override
	@Transactional
	public void deleteFollow(Long followeeId, Long followerId) {
		followRepository.deleteByFolloweeIdAndFollowerId(followeeId, followerId);

	}

	@Override
	public List<Follow> getFolloweeList(Long followerId, List<Long> userIdList) {
		List<Follow> followList = followRepository.findByFollowerIdAndFolloweeIdIn(followerId, userIdList);
		
		return followList;
	}

}
