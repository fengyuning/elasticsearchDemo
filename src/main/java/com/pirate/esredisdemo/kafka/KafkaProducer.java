package com.pirate.esredisdemo.kafka;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.UUID;

@Component
@Slf4j
public class KafkaProducer {
    @Resource
    private KafkaTemplate<Long,String> kafkaTemplate;

    public void testSent(){
        KafkaMsg<String> msg = new KafkaMsg<>();
        msg.setId(UUID.randomUUID().toString());
        msg.setMsg("测试kafka的使用");
        msg.setSentTime(DateTime.now());
        kafkaTemplate.send("test-topic", JSON.toJSONString(msg));
    }


}
