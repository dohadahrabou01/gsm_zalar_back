package com.gsm_zalar.Repositories;

import com.gsm_zalar.Models.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification,Integer> {
}
