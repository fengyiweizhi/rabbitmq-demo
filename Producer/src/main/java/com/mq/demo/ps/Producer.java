package com.mq.demo.ps;

import com.mq.demo.utils.ConnectionUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 发布订阅模式
 * @author 风亦未止
 */
public class Producer {

    public static String FANOUT_EXCHAGE = "fanout_exchage";
    public static String FANOUT_QUEUE_1 = "fanout_queue_1";
    public static String FANOUT_QUEUE_2 = "fanout_queue_2";


    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtils.getConnection();
        //创建频道
        Channel channel = connection.createChannel();

        /**声明交换机
         * 参数1：交换机名称
         * 参数2：交换机类型，fanout, topic, direct, headers
         */
        channel.exchangeDeclare(FANOUT_EXCHAGE, BuiltinExchangeType.FANOUT);

        //声明(创建)队列
        /**
         * 参数1：队列名称
         * 参数2：是否定义持久化队列
         * 参数3：是否独占本次连接，只能有一个Consumer监听这个队列
         * 参数4：是否在不使用的时候自动删除队列,当没有Consumer时，自动删除
         * 参数5：队列其它参数
         */
        channel.queueDeclare(FANOUT_QUEUE_1, true, false, false, null);
        channel.queueDeclare(FANOUT_QUEUE_2, true, false, false, null);

        //队列绑定交换机
        channel.queueBind(FANOUT_QUEUE_1, FANOUT_EXCHAGE, "");
        channel.queueBind(FANOUT_QUEUE_2, FANOUT_EXCHAGE, "");


        for (int i = 1; i <= 10; i++) {
            //发送消息
            String message = "你好:小兔子~ fanout 模式---" + i;
            /**
             * 参数1：交换机名称，如果没有指定则使用默认Default Exchage
             * 参数2：路由key,简单模式可以传递队列名称
             * 参数3：消息其它属性
             * 参数4：消息内容
             */
            channel.basicPublish(FANOUT_EXCHAGE, "", null, message.getBytes());
            System.out.println("已发送消息：" + message);
        }

        //释放资源
        channel.close();
        connection.close();

    }

}
