package com.example.demo;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.FactoryBasedNavigableListAssert.assertThat;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@Testcontainers
@SpringBootTest
@EnableKafka
public class KafkaAvroProducerTest {
    private static final String TOPIC_NAME = "test-topic";
    public static final int KAFKA_PORT = 9092;
    public static final int SCHEMA_PORT = 8081;


    @Autowired
    private KafkaTemplate<String, BookAvro> kafkaTemplate;

    @Container
    static DockerComposeContainer environment =
            new DockerComposeContainer(new File("docker-compose.yml"))
                    .withExposedService("kafka1", KAFKA_PORT)
                    .withExposedService("kafka-schema-registry", SCHEMA_PORT);


    @SpyBean
    private BookConsumer bookConsumer;

    @Captor
    ArgumentCaptor<BookAvro> bookArgumentCaptor;

    @Captor
    ArgumentCaptor<String> keyArgumentCaptor;

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", () -> "localhost:" + environment.getServicePort("kafka1", KAFKA_PORT));
        registry.add("spring.kafka.properties.schema.registry.url", () -> "http://localhost:" + environment.getServicePort("kafka-schema-registry", SCHEMA_PORT));
        registry.add("spring.kafka.consumer.properties.schema.registry.url", () -> "http://localhost:" + environment.getServicePort("kafka-schema-registry", SCHEMA_PORT));
    }


    @Test
    public void testProducer() throws InterruptedException {
        // Start a Kafka broker using Testcontainers

        String brokerUrl = environment.getServiceHost("kafka1", KAFKA_PORT)
                + ":" +
                environment.getServicePort("kafka1", KAFKA_PORT);
        String schemaUrl = "http://" + environment.getServiceHost("kafka-schema-registry", SCHEMA_PORT)
                + ":" +
                environment.getServicePort("kafka-schema-registry", SCHEMA_PORT);

/*

        // Set up the Kafka producer configuration
        Properties producerConfig = new Properties();
        producerConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, broker);
        producerConfig.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
        producerConfig.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
        producerConfig.put("schema.registry.url", schema);

        // Create a Kafka producer
        KafkaProducer<String, BookAvro> producer = new KafkaProducer<>(producerConfig);

        // Create and send a test `Book` object
        BookAvro testBook = new BookAvro(1, "Test Book");
        producer.send(new ProducerRecord<>(TOPIC_NAME, testBook));

        producer.close();

 */

        // Create and send a test `Book` object
        BookAvro testBook = new BookAvro(1, "Test Book");
        kafkaTemplate.send(TOPIC_NAME, testBook.getName().toString(), testBook);

       // Thread.sleep(10000L);

        // Read the message and assert its properties
        //verify(bookConsumer, timeout(20000).times(1))
           //     .receiveMessage(bookArgumentCaptor.capture());

      //  BookAvro bookAvro = bookArgumentCaptor.getValue();
       // System.out.println(bookAvro.getName());
        //bookConsumer.receiveMessage();
    }
}

