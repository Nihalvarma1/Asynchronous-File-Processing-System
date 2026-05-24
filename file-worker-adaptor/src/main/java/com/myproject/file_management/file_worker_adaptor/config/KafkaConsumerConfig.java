package com.myproject.file_management.file_worker_adaptor.config;


import com.myproject.file_management.file_worker_adaptor.dto.FileEventDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, FileEventDto> factory(
            ConsumerFactory<String, FileEventDto> consumerFactory) {

        ConcurrentKafkaListenerContainerFactory<String, FileEventDto> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory);
        return factory;
    }
}