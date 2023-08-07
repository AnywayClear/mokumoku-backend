package com.anywayclear.dto.response;

import com.anywayclear.entity.Alarm;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AlarmResponse {

    private String sender;
    private String context;

    public static AlarmResponse toResponse(Alarm alarm) {
        return AlarmResponse.builder()
                .sender(alarm.getSender())
                .context(alarm.getContext())
                .build();
    }
}

