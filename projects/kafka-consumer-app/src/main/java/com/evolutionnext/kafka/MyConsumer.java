package com.evolutionnext.kafka;

import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class MyConsumer {

    private static String collectionTopicPartitionToString
            (Collection<TopicPartition> topicPartitions) {
        return topicPartitions.stream()
                              .map(tp -> tp.topic() + " - " + tp.partition())
                              .collect(Collectors.joining(","));
    }

    @SuppressWarnings({"Duplicates"})
    public static void main(String[] args) throws IOException {
        Properties properties = new Properties();
        try (InputStream inputStream = MyConsumer.class.getResourceAsStream("/consumer.properties")) {
            properties.load(inputStream);
        }

        KafkaConsumer<String, Integer> consumer =
                new KafkaConsumer<>(properties);

        consumer.subscribe(Collections.singletonList("my_orders"),
                new ConsumerRebalanceListener() {
                    @Override
                    public void onPartitionsRevoked(Collection<TopicPartition> collection) {
                        System.out.println("Partition revoked:" +
                                collectionTopicPartitionToString(collection));
                        consumer.commitAsync();
                    }

                    @Override
                    public void onPartitionsAssigned(Collection<TopicPartition> collection) {
                        System.out.println("Partition assigned:" +
                                collectionTopicPartitionToString(collection));
                    }
                });

        AtomicBoolean done = new AtomicBoolean(false);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> done.set(true)));

        while (!done.get()) {
            ConsumerRecords<String, Integer> records =
                    consumer.poll(Duration.of(500, ChronoUnit.MILLIS));
            for (ConsumerRecord<String, Integer> record : records) {
                System.out.format("offset: %d%n", record.offset());
                System.out.format("partition: %d%n", record.partition());
                System.out.format("timestamp: %d%n", record.timestamp());
                System.out.format("timeStampType: %s%n",
                        record.timestampType());
                System.out.format("topic: %s%n", record.topic());
                System.out.format("key: %s%n", record.key());
                System.out.format("value: %s%n", record.value());
            }

            consumer.commitAsync((offsets, exception) -> {

            });
        }
        consumer.commitSync();
        consumer.close();
    }
}
