package com.evolutionnext.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class MyProducer {
    @SuppressWarnings("Duplicates")
    public static void main(String[] args) throws InterruptedException, IOException {
        Properties properties = new Properties();
        try (InputStream inputStream = MyProducer.class.getResourceAsStream("/producer.properties")) {
            properties.load(inputStream);
        }

        try (KafkaProducer<String, Integer> producer = new KafkaProducer<>(properties)) {
            String stateString = "AK,AL,AZ,AR,CA,CO,CT,DE,FL,GA," +
                    "HI,ID,IL,IN,IA,KS,KY,LA,ME,MD," +
                    "MA,MI,MN,MS,MO,MT,NE,NV,NH,NJ," +
                    "NM,NY,NC,ND,OH,OK,OR,PA,RI,SC," +
                    "SD,TN,TX,UT,VT,VA,WA,WV,WI,WY";

            AtomicBoolean done = new AtomicBoolean(false);

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                done.set(true);
            }));

            Random random = new Random();

            while (!done.get()) {
                String[] states = stateString.split(",");
                String state = states[random.nextInt(states.length)];
                int amount = random.nextInt(100000 - 50 + 1) + 50;

                ProducerRecord<String, Integer> producerRecord = new ProducerRecord<>("my_orders", state, amount);

                producer.send(producerRecord, (metadata, e) -> {
                    if (e != null) {
                        System.out.println("ERROR! ");
                        String firstException = Arrays.stream(e.getStackTrace())
                                .findFirst()
                                .map(StackTraceElement::toString)
                                .orElse("Undefined Exception");
                        System.out.println(e);
                        System.out.println(firstException);
                    } else if (metadata != null) {
                        System.out.println(producerRecord.key());
                        System.out.println(producerRecord.value());

                        if (metadata.hasOffset()) {
                            System.out.format("offset: %d%n",
                                    metadata.offset());
                        }
                        System.out.format("partition: %d%n",
                                metadata.partition());
                        System.out.format("timestamp: %d%n",
                                metadata.timestamp());
                        System.out.format("topic: %s%n", metadata.topic());
                        System.out.format("toString: %s%n",
                                metadata.toString());
                    }
                });

                Thread.sleep(random.nextInt(30000 - 1000 + 1) + 1000);
            }
        }
    }
}
