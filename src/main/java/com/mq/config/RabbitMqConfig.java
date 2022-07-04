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
     *  声明队列
     * @return
     */
    @Bean("itemTopicQueue")
    public Queue topicQueue(){
        return QueueBuilder.durable(queue_name).build();
    }

    /**
     * 绑定交换机
     */
    @Bean
    public Binding itemQueueExchange(@Qualifier("itemTopicExchange") Exchange exchange,
                                     @Qualifier("itemTopicQueue") Queue queue){
        //routingkey为绑定规则
        //#通配多级
        return BindingBuilder.bind(queue).to(exchange).with("item.#").noargs();
    }


}
