package com.example.vueadminjava2.rabbitmq;

import com.example.vueadminjava2.config.RabbitMqConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description :
 * @Author :kono淇杭哒、
 * @Create 2021-12-09 23:04
 */
@Component
public class Publisher{
    @Autowired
    private RabbitTemplate rabbitTemplate;
    /**
     * 默认的ttl为半小时
     */
    private final Integer default_ttl = 30 * 60 * 1000;

    public void publishDelayedMessage(String msg) {
        rabbitTemplate.convertAndSend(RabbitMqConfig.DELAYED_EXCHANGE_NAME, RabbitMqConfig.DELAYED_ROUTING_KEY, msg,
                correlationData -> {
                    correlationData.getMessageProperties().setDelay(default_ttl);
                    return correlationData;
                });
    }

    public void publishDelayedMessageWithSpecifigTTl(String msg, Integer ttl) {
        rabbitTemplate.convertAndSend(RabbitMqConfig.DELAYED_EXCHANGE_NAME, RabbitMqConfig.DELAYED_ROUTING_KEY, msg,
                correlationData -> {
                    correlationData.getMessageProperties().setDelay(ttl);
                    return correlationData;
                });
    }

}
