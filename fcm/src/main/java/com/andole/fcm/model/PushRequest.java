package com.andole.fcm.model;

import lombok.Data;

@Data
public class PushRequest {
    private String title;
    private String body;
}
