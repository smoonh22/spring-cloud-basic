package com.msa.repository;

import com.msa.domain.Feed;
import com.msa.repository.dto.FeedData;
import com.msa.repository.dto.FeedRequestDto;
import com.msa.repository.dto.FeedResponseDto;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.jboss.logging.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
public class FeedRestRepository {
    Logger log = Logger.getLogger(FeedRestRepository.class);

    private final SocialFeignClient socialFeignClient;

    public FeedRestRepository(SocialFeignClient socialFeignClient) {
        this.socialFeignClient = socialFeignClient;
    }

    //	@HystrixCommand(
//			commandProperties = {
//					@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "10"),
//					@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "10"),
//					@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "5")
//			},
//			fallbackMethod = "getFeedListFallback"
//	)
    @HystrixCommand(
            commandKey = "feed",
            fallbackMethod = "getFeedListFallback"
    )
    public List<Feed> getFeedList(Long userId) {

//		List<ServiceInstance> instances = discoveryClient.getInstances("socialservice");
//		log.info("호출 URL : " + instances.get(0).getUri().toString());
        FeedResponseDto response = socialFeignClient.getFeedByUserId(userId);

        List<FeedData> feedDataList = response.getData();
        List<Feed> feedList = new ArrayList<>();
        for (FeedData data : feedDataList) {
            feedList.add(new Feed(data.getUserId(), data.getFolloweeId(), data.getPostId()));
        }

        return feedList;
    }

    @HystrixCommand(commandKey = "feed")
    public void addFeeds(Long userId, Long postId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        FeedRequestDto requestDto = new FeedRequestDto(userId, postId);

//		HttpEntity<FeedRequestDto> entity = new HttpEntity<FeedRequestDto>(requestDto, headers);

        socialFeignClient.postFeed(requestDto);
    }

    public List<Feed> getFeedListFallback(Long userId) {
        List<Feed> feedList = new ArrayList<>();

        feedList.add(new Feed(userId, 57L, 298L));
        feedList.add(new Feed(userId, 57L, 233L));
        feedList.add(new Feed(userId, 57L, 234L));

        return feedList;
    }
}
