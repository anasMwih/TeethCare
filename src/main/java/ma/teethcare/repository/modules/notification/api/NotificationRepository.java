package ma.teethcare.repository.modules.notification.api;

import ma.teethcare.entities.Notification;
import ma.teethcare.repository.common.CrudRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface NotificationRepository extends CrudRepository<Notification, Long> {

    List<Notification> findByUserId(Long userId);

    List<Notification> findByType(String type);

    List<Notification> findByPriorite(String priorite);

    List<Notification> findByDate(LocalDate date);

    List<Notification> findBetweenDates(LocalDate startDate, LocalDate endDate);

    void deleteOlderThan(LocalDate date);
}