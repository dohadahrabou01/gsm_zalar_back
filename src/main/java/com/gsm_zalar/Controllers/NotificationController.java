package com.gsm_zalar.Controllers;

import com.gsm_zalar.DTO.NotificationDTO;
import com.gsm_zalar.Services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/ongoing")
    public List<NotificationDTO> getOngoingAssignments() {
        return notificationService.getOngoingAssignments();
    }
}
