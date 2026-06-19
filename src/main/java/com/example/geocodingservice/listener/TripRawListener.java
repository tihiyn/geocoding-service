package com.example.geocodingservice.listener;

import com.example.geocodingservice.api.AddressClient;
import com.example.geocodingservice.config.KafkaConfig;
import com.example.geocodingservice.dto.Notification;
import com.example.geocodingservice.dto.TripRawNotification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TripRawListener {
    private final AddressClient addressClient;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(topics = KafkaConfig.TRIP_CREATED_RAW_TOPIC, groupId = "geocoding-service")
    public void onTripCreated(TripRawNotification raw) {
        log.info("Получено сырое уведомление о поездке regNum={}", raw.getRegNum());
        String start = addressClient.getAddressByCoords(raw.getBeginLon(), raw.getBeginLat());
        String finish = addressClient.getAddressByCoords(raw.getEndLon(), raw.getEndLat());

        Notification enriched = new Notification();
        enriched.setStart(start);
        enriched.setFinish(finish);
        enriched.setEnterprise(raw.getEnterprise());
        enriched.setRegNum(raw.getRegNum());
        enriched.setManagers(raw.getManagers());

        kafkaTemplate.send(KafkaConfig.TRIP_CREATED_ENRICHED_TOPIC, raw.getRegNum(), enriched)
            .whenComplete((result, ex) -> {
                if (ex != null) {
                    log.error("Не удалось опубликовать обогащённое уведомление regNum={}", raw.getRegNum(), ex);
                } else {
                    log.info("Опубликовано обогащённое уведомление regNum={} в топик {}",
                        raw.getRegNum(), KafkaConfig.TRIP_CREATED_ENRICHED_TOPIC);
                }
            });
    }
}
