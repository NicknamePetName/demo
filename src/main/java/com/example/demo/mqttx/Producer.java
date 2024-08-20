package com.example.demo.mqttx;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.UUID;

//@EnableScheduling
//@Configuration
public class Producer {

    private String broker = "tcp://192.168.20.215:1883";
    private String topic = "mqtt/test";
    private String username = "emqx";
    private String password = "public";
    private String clientid = "publish_client";
    private String content = "Hello MQTT:";
    private int qos = 1;

    @Scheduled(cron ="*/6 * * * * ?")
    public void sendMessage() {
        try {
            MqttClient client = new MqttClient(broker, clientid, new MemoryPersistence());
            // 连接参数
            MqttConnectOptions options = new MqttConnectOptions();
            // 设置用户名和密码
            options.setUserName(username);
            options.setPassword(password.toCharArray());
            options.setConnectionTimeout(60);
            options.setKeepAliveInterval(60);
            // 连接
            client.connect(options);
            // 创建消息并设置 QoS
            content += UUID.randomUUID().toString();

            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);
            // 发布消息

            client.publish(topic, message);
            System.out.println("Message published -------------------------");
            System.out.println("topic: " + topic);
            System.out.println("message content: " + content);
            // 关闭连接
//           client.disconnect();
            // 关闭客户端
//           client.close();
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }
}
