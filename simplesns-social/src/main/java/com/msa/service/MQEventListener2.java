package com.msa.service;


import com.netflix.discovery.converters.Auto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.SubscribableChannel;

@EnableBinding(MQEventListener2.FeedMsgProcessor.class)
public class MQEventListener2 {
    private static Logger log = LoggerFactory.getLogger(MQEventListener2.class);

    private final FeedService feedService;

    public MQEventListener2(FeedService feedService) {
        this.feedService = feedService;
    }

    @StreamListener(FeedMsgProcessor.INPUT)
    public void processEvent(String msg) {
        log.info("Feed Info Consumed : " + msg);
        String[] msgArr = msg.split(":");
        log.info("Feed Info userId : " + msgArr[0]);
        log.info("Feed Info postId : " + msgArr[1]);
        feedService.addFeeds(Long.valueOf(msgArr[0]), Long.valueOf(msgArr[1]));

    }


    public interface FeedMsgProcessor {
        String INPUT = "input2";

        @Input(INPUT)
        SubscribableChannel aInput();

    }


}
