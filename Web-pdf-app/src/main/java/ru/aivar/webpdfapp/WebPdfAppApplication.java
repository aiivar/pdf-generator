package ru.aivar.webpdfapp;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.aivar.webpdfapp.models.ERole;
import ru.aivar.webpdfapp.models.Role;
import ru.aivar.webpdfapp.repositories.RoleRepository;

@SpringBootApplication
public class WebPdfAppApplication {

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange("pdf_files_topic_exchange");
    }

    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable("deadLetterQueue").build();
    }

    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange("deadLetterExchange");
    }

    @Bean
    public Binding DlqBinding() {
        return BindingBuilder.bind(deadLetterQueue()).to(deadLetterExchange()).with("deadLetter");
    }

    @Bean
    public CommandLineRunner roleInsert(RoleRepository roleRepository) {
        return args -> {
            if (!roleRepository.existsByName(ERole.ROLE_ADMIN)) {
                roleRepository.save(Role.builder()
                        .name(ERole.ROLE_ADMIN)
                        .build());
            }
            if (!roleRepository.existsByName(ERole.ROLE_USER)) {
                roleRepository.save(Role.builder()
                        .name(ERole.ROLE_USER)
                        .build());
            }
        };
    }

    @Bean
    public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        return container;
    }

    public static void main(String[] args) {
        SpringApplication.run(WebPdfAppApplication.class, args);
    }

}
