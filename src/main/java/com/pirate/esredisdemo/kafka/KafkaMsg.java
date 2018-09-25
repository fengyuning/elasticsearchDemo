package com.pirate.esredisdemo.kafka;

import lombok.Data;
import org.joda.time.DateTime;

@Data
public class KafkaMsg<T> {
    private String id;
    private T msg;
    private DateTime sentTime;
}
