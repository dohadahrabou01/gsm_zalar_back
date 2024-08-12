package com.gsm_zalar.Services;

import com.gsm_zalar.DTO.NotificationDTO;
import com.gsm_zalar.Models.Notification;
import com.gsm_zalar.Models.Validation;
import com.gsm_zalar.Repositories.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    @Autowired
    NotificationRepository notificationRepository;

    public List<NotificationDTO> getOngoingAssignments() {
        List<Notification> notifications= notificationRepository.findAll().stream()
                .filter(notification -> Validation.ENCOURS.equals(notification.getAfTerminal().getValidation()))
                .filter(notification -> !(notification.getAfTerminal().getDeleted()))
                .collect(Collectors.toList());

        List<NotificationDTO> NotificationDTOs=new ArrayList<>();
        for(Notification notification:notifications)
        {
            NotificationDTO notificationDTO=new NotificationDTO();
            notificationDTO.setId(notification.getId());
            notificationDTO.setMessage(notification.getMessage());
            NotificationDTOs.add(notificationDTO);
        }
        return  NotificationDTOs;
    }

}
