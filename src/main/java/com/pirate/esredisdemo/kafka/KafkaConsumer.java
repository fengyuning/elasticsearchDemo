package com.pirate.esredisdemo.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class KafkaConsumer {

    @KafkaListener(topics = "test-topic-0")
    public void listen(ConsumerRecord<?, ?> record) {
        if (Optional.ofNullable(record.value()).isPresent()) {
            Object value = record.value();
            System.out.println(value.toString());
        }
    }
}
