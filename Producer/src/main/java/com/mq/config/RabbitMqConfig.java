package com.mq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置类
 * @author 风亦未止
 * @date 2022/7/4 16:09
 */
@Configuration
public class RabbitMqConfig {
    /**
     * 交换机名称
     */
    @Value("${mq.exchange_name}")
    public String exchange_name;

    /**
     * 队列名称
     */
    @Value("${mq.queue_name}")
    public String queue_name;

    /**
     *  声明交换机
     * @return
     */
    @Bean("itemTopicExchange")
    public Exchange topicExchange(){
        return ExchangeBuilder.topicExchange(exchange_name).durable(true).build();
    }

    /**
     *  声明正常的队列，设置x-dead-letter-exchange和x-dead-letter-routing-key
     *  过期时间 100000
     */
    @Bean("itemTopicQueue")
    public Queue topicQueue(){
        return QueueBuilder.durable(queue_name).withArgument("x-message-ttl",100000)
                //x-dead-letter-exchange：死信交换机名称
                .withArgument("x-dead-letter-exchange","exchange_dlx")
                // x-dead-letter-routing-key：发送给死信交换机的routingkey
                .withArgument("x-dead-letter-routing-key","dlx.e").build();
    }

    /**
     * 声明死信队列
     * @return
     */
    @Bean("dlxQueue")
    public Queue dlxQueue(){
        return QueueBuilder.durable("queue_dlx").build();
    }

    /**
     * 声明死信交换机
     * @return
     */
    @Bean("dlxTopicQueue")
    public Exchange dlxExchange(){
        return ExchangeBuilder.topicExchange("exchange_dlx").durable(true).build();
    }
    /**
     * 绑定交换机
     */
    @Bean
    public Binding dlxQueueExchange(@Qualifier("dlxTopicQueue") Exchange exchange,
                                     @Qualifier("dlxQueue") Queue queue){
        //routingkey为绑定规则
        //   #通配多级
        return BindingBuilder.bind(queue).to(exchange).with("dlx.#").noargs();
    }



    /**
     * 绑定交换机
     */
    @Bean
    public Binding itemQueueExchange(@Qualifier("itemTopicExchange") Exchange exchange,
                                     @Qualifier("itemTopicQueue") Queue queue){
        //routingkey为绑定规则
        //   #通配多级
        return BindingBuilder.bind(queue).to(exchange).with("item.#").noargs();
    }


}
