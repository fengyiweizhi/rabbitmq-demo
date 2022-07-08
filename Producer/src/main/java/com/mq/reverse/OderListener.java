package com.mq.reverse;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

/**
 * @author 风亦未止
 * @date 2022/7/6 23:20
 */
@Component
public class OderListener implements ChannelAwareMessageListener {
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            //1.接收消息
            System.out.println(new String(message.getBody()));
            //2.业务处理逻辑
            System.out.println("业务逻辑...判断订单状态，是否支付成功，回滚");
            //3.手动签收
            channel.basicAck(deliveryTag,true);
        } catch (Exception e) {
            //4.拒绝签收
            channel.basicNack(deliveryTag,true,false);
            //channel.basicReject(deliveryTag,true)
        }
    }

    @Override
    public void onMessage(Message message) {
        ChannelAwareMessageListener.super.onMessage(message);
    }
}
