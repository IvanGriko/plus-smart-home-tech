package ru.yandex.practicum.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

@Getter
@Setter
@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
@ConfigurationProperties("analyzer")
public class AnalyzerConfig {
    List<String> hubTopics;
    Duration hubConsumeAttemptTimeout;
    Properties hubConsumerProperties;
    List<String> snapshotTopics;
    Duration snapshotConsumeAttemptTimeout;
    Properties snapshotConsumerProperties;

    @Bean
    public KafkaConsumer<String, HubEventAvro> hubConsumer() {
        return new KafkaConsumer<>(getHubConsumerProperties());
    }

    @Bean
    public KafkaConsumer<String, SensorsSnapshotAvro> consumer() {
        return new KafkaConsumer<>(getSnapshotConsumerProperties());
    }
}
