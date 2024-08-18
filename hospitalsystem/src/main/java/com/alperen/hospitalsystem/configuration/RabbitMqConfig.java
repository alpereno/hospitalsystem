package com.alperen.hospitalsystem.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;
    @Value("${rabbitmq.second.exchange.name}")
    private String secondExchangeName;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;
    @Value("${rabbitmq.second.routing.key}")
    private String secondRoutingKey;

    @Value("${rabbitmq.queue.name}")
    private String queueName;
    @Value("${rabbitmq.second.queue.name}")
    private String secondQueueName = "second_patient_queue";

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(exchangeName);
    }

    @Bean
    public DirectExchange secondExchange() {
        return new DirectExchange(secondExchangeName);
    }

    @Bean
    public Queue firstStepQueue() {
        return new Queue(queueName, true);
    }

    @Bean
    public Queue secondStepQueue() {
        return new Queue(secondQueueName, true);
    }

    @Bean
    public Binding binding(Queue firstStepQueue, DirectExchange exchange) {
        return BindingBuilder.bind(firstStepQueue).to(exchange).with(routingKey);
    }

    @Bean
    public Binding secondBinding(Queue secondStepQueue, DirectExchange secondExchange) {
        return BindingBuilder.bind(secondStepQueue).to(secondExchange).with(secondRoutingKey);
    }

    @Bean
    public MessageConverter jsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }
}
