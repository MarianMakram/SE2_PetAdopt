package com.petadopt.petservice.client;

import com.petadopt.petservice.models.dto.NotificationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "interaction-service", url = "${interaction.service.url:http://interaction-service:8084}")
public interface InteractionServiceClient {

    @PostMapping("/api/notifications")
    void createNotification(@RequestBody NotificationRequest request);
}
