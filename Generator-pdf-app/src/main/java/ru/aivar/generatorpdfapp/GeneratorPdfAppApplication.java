package ru.aivar.generatorpdfapp;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Map;

@SpringBootApplication
public class GeneratorPdfAppApplication {

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange("pdf_files_topic_exchange");
    }

    @Bean
    public Queue pdfSimpleQueue() {
        return QueueBuilder.nonDurable().withArgument("x-dead-letter-exchange", "deadLetterExchange")
                .withArgument("x-dead-letter-routing-key", "deadLetter").build();
    }

    @Bean
    public Queue pdfListQueue() {
        return QueueBuilder.nonDurable().withArgument("x-dead-letter-exchange", "deadLetterExchange")
                .withArgument("x-dead-letter-routing-key", "deadLetter").build();
    }

    @Bean
    public Binding pdfSimpleQueueBinding(TopicExchange topicExchange) {
        return BindingBuilder.bind(pdfSimpleQueue()).to(topicExchange).with("files.pdf.simple");
    }

    @Bean
    public Binding pdfListQueueBinding(TopicExchange topicExchange) {
        return BindingBuilder.bind(pdfListQueue()).to(topicExchange).with("files.pdf.list");
    }

    @Bean
    public RabbitListenerContainerFactory<SimpleMessageListenerContainer> containerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setPrefetchCount(10);
        factory.setConcurrentConsumers(5);
        return factory;
    }

    public static void main(String[] args) {
        SpringApplication.run(GeneratorPdfAppApplication.class, args);
    }

}
