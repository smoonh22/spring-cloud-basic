package com.msa.repository;

import com.msa.repository.dto.FeedRequestDto;
import com.msa.repository.dto.FeedResponseDto;
import com.msa.repository.dto.FollowResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("socialservice")
public interface SocialFeignClient {

    @PostMapping("feed")
    String postFeed(@RequestBody FeedRequestDto feedRequestDto);

    @GetMapping("/followee")
    FollowResponseDto getFollowee(@RequestParam("userId") long userId,
                                  @RequestParam("userIds") List<Long> userIds);

    @GetMapping("/feed")
    FeedResponseDto getFeedByUserId(@RequestParam("userId") long userId);

}
