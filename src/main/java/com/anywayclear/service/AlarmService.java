package com.anywayclear.service;

import com.anywayclear.dto.response.*;
import com.anywayclear.entity.Alarm;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashSet;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlarmService  {

    // topic에 메시지 발행을 기다리는 Listener
    private final RedisMessageListenerContainer redisMessageListener;

    // 리포지토리 대신 템플릿 사용
    private final RedisTemplate<String, Alarm> redisAlarmTemplate;


    // 발행 서비스
    private final RedisPublishService redisPublishService;

    // 구독 서비스
    private final RedisSubscribeService redisSubscribeService;

    // 구독 목록 불러오기 위한 서비스
    private final SubscribeService subscribeService;

    // 찜 목록 불러오기 위한 서비스
    private final DibService dibService;

    // topic 이름으로 topic 정보를 가져와 메시지를 발송할 수 있도록 Map에 저장
    private Map<String, ChannelTopic> channels;

    @PostConstruct
    public void init() { // topic 정보를 담을 Map을 초기화
        channels = new ConcurrentHashMap<>(); // topicName : ChannelTopic obj
    }

    public String createTopic(String topicName) { // 신규 Topic을 생성하고 Listener 등록 및 Topic Map에 저장
        ChannelTopic topic = new ChannelTopic(topicName); // 토픽 생성
        redisMessageListener.addMessageListener(redisSubscribeService, topic); // 리스너 등록
        channels.put(topicName, topic);
        return topicName;
    }

    public void subscribeTopic(String topicName) {
//        RedisPubSubAdapter<String, String> listener = new RedisPubSubAdapter<String, String>() {
//            @Override
//            public void message(String channel, String message) {
//                System.out.println(String.format("subscribe -> Channel: %s, Message: %s", channel, message));
//            }
//        };
//
//        StatefulRedisPubSubConnection<String, String> pubsubConn = Main.redisClient.connectPubSub();
//        pubsubConn.addListener(listener);
//        RedisPubSubAsyncCommands<String, String> async = pubsubConn.async();
//        async.subscribe(topicName);
//
//// application flow continues
        ChannelTopic topic = channels.get(topicName);
        redisMessageListener.addMessageListener(redisSubscribeService, topic);
    }

    public void pushAlarm(String topicName, String sender, String context) { // 알람 전송
        ChannelTopic topic = channels.get(topicName); // 토픽 객체 불러오기
        Alarm alarm = Alarm.builder().sender(sender).context(context).build(); // 알람 객체 생성
        redisPublishService.publish(topic, alarm); // 알람 전송
    }

    public void deleteTopic(String topicName) { // 토픽 제거
        ChannelTopic topic = channels.get(topicName); // 토픽 객체 불러오기
        redisMessageListener.removeMessageListener(redisSubscribeService, topic); // 토픽 제거
        channels.remove(topicName); // HashMap 제거
    }

    public AlarmResponseList getSubscribeAlarmList(String memberId) { // 해당 유저의 알림 리스트 불러오기
        // 패턴 매칭 사용 -> member:memberId:alarm:*

        // [1] 유저의 구독 목록 불러오기
        SubscribeResponseList subscribeResponseList = subscribeService.getSubscribeList(memberId);
        Set<String> keys = new HashSet<>(); // Set을 사용하여 중복된 값 제거
        for (SubscribeResponse response : subscribeResponseList.getSubscribeResponseList()) {
            String seller = response.getSeller().getUserId(); // Response 객체에서 sender 필드 값을 추출
            keys.add(seller); // keys 집합에 sender 값 추가
        }

        // [2] 반복문으로 해당 판매자 userId가 키로 포함된 알림 내역 불러오기
        Set<String> subKeys = new HashSet<>();
        for (String key : keys) {
            subKeys.addAll(redisAlarmTemplate.keys("member:" + key + ":alarm:*"));
        }

        // [3] 반복문으로 Value(알람 객체) 불러와 저장
        List<Alarm> alarmList = subKeys.stream().map(k -> redisAlarmTemplate.opsForValue().get(k)).collect(Collectors.toList()); // 해당 키의 알람 리스트 저장
        return new AlarmResponseList(alarmList);
    }
  
    public AlarmResponseList getDibAlarmList(String memberId) { // 해당 유저의 알림 리스트 불러오기
        // 패턴 매칭 사용 -> member:memberId:alarm:*

        // [1] 유저의 찜 목록 불러오기
        DibResponseList dibResponseList = dibService.getDibList(memberId);
        Set<String> keys = new HashSet<>(); // Set을 사용하여 중복된 값 제거
        for (DibResponse response : dibResponseList.getDibResponseList()) {
            String produce = response.getProduce().getId().toString(); // Response 객체에서 sender 필드 값을 추출
            keys.add(produce); // keys 집합에 sender 값 추가
        }

        // [2] 반복문으로 해당 경매글의 Id가 키로 포함된 알림 내역 불러오기
        Set<String> Dibskeys = new HashSet<>();
        for (String key : keys) {
            Dibskeys.addAll(redisAlarmTemplate.keys("member:" + key + ":alarm:*"));
        }

        // [3] 반복문으로 Value(알람 객체) 불러와 저장
        List<Alarm> alarmList = Dibskeys.stream().map(k -> redisAlarmTemplate.opsForValue().get(k)).collect(Collectors.toList()); // 해당 키의 알람 리스트 저장
        return new AlarmResponseList(alarmList);
    }
}
