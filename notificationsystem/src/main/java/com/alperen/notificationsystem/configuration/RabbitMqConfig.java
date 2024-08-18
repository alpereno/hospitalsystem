package com.alperen.notificationsystem.configuration;

import com.rabbitmq.client.AMQP;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    @Value("${rabbitmq.second.exchange.name}")
    private String secondExchangeName;
    @Value("${rabbitmq.second.routing.key}")
    private String secondRoutingKey;

    public static final String QUEUE_NAME = "patient_queue";
    public static final String SECOND_QUEUE_NAME = "second_patient_queue";

    @Bean
    public Queue secondQueue() {
        return new Queue(SECOND_QUEUE_NAME, true);
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(secondExchangeName);
    }

    @Bean
    public Binding binding(Queue secondQueue, DirectExchange exchange) {
        return BindingBuilder.bind(secondQueue).to(exchange).with(secondRoutingKey);
    }

    @Bean
    public Queue firstStepQueue(){
        return new Queue(QUEUE_NAME, false);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
