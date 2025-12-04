package ma.teethcare.repository.modules.notification.api;

import ma.teethcare.entities.Notification;
import ma.teethcare.entities.enums.PrioriteNotification;
import ma.teethcare.entities.enums.TypeNotification;
import ma.teethcare.repository.common.CrudRepository;

import java.time.LocalDate;
import java.util.List;

public interface NotificationRepository extends CrudRepository<Notification, Long> {
    List<Notification> findByUtilisateurId(Long utilisateurId);
    List<Notification> findByType(TypeNotification type);
    List<Notification> findByPriorite(PrioriteNotification priorite);
    List<Notification> findByDate(LocalDate date);
    List<Notification> findUnreadByUtilisateur(Long utilisateurId);
    void markAsRead(Long notificationId);
    void markAllAsRead(Long utilisateurId);
}