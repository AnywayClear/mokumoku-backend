package com.anywayclear.dto.response;

import com.anywayclear.entity.Alarm;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class AlarmResponse {

    private int type;
    private String senderId;
    private String senderName;
    private String context;
    private LocalDateTime createdAt;

    public static AlarmResponse toResponse(Alarm alarm) {
        return AlarmResponse.builder()
                .type(alarm.getType())
                .senderId(alarm.getSenderId())
                .senderName(alarm.getSenderName())
                .context(alarm.getContext())
                .createdAt(alarm.getCreatedAt())
                .build();
    }
}

