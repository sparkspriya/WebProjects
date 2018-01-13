package com.spark.kafka;

import java.io.IOException;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.spark.constants.AppConstants;

public class KafkaMessagePublisher {
	Producer<String,String> producer;
public KafkaMessagePublisher() {
	Properties props=new Properties();
	try {
		props.load(KafkaMessagePublisher.class.getClassLoader().getResourceAsStream("producer.properties"));
	} catch (IOException e) {
		e.printStackTrace();
	}
	producer=new KafkaProducer<String,String>(props);
}

public void publishMessage(String msg) {
	producer.send(new ProducerRecord<String, String>(AppConstants.TOPIC,msg));
	System.out.println("Published.");
	//producer.close();
}
}
