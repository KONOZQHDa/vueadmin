package com.example.vueadminjava2.rabbitmq;

import com.example.vueadminjava2.config.RabbitMqConfig;
import com.example.vueadminjava2.service.OrderService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description :
 * @Author :kono淇杭哒、
 * @Create 2021-12-09 22:26
 */
@Component
public class Consumer{
    @Autowired
    private OrderService orderService;

    /**
     * 监听死信队列
     */
    @RabbitListener(queues = RabbitMqConfig.DELAYED_QUEUE_NAME)
    public void listenDelayedQueueMessage(String msg) throws InterruptedException {
        //若监听到的消息为订单消息
        if (msg.startsWith("order:")) {
            int index = msg.indexOf(":");
            //取出订单号
            String orderNumber = msg.substring(index + 1);
            orderService.checkOrder(orderNumber);
        }
    }
}
