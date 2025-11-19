package ma.teethcare.repository.modules.notification.inMemDB_implementation;

import ma.teethcare.entities.Notification;
import ma.teethcare.repository.modules.notification.api.NotificationRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class NotificationRepositoryImpl implements NotificationRepository {

    private final List<Notification> data = new ArrayList<>();
    private Long nextId = 1L;

    public NotificationRepositoryImpl() {
        initializeTestData();
    }

    private void initializeTestData() {
        LocalDate today = LocalDate.now();

        Notification n1 = Notification.builder()
                .id(nextId++)
                .titre("Rappel RDV")
                .message("Rendez-vous avec Dr. Alami à 14h00")
                .date(today)
                .time(LocalTime.of(14, 0))
                .type("RAPPEL")
                .priorité("HAUTE")
                .build();
        data.add(n1);

        Notification n2 = Notification.builder()
                .id(nextId++)
                .titre("Confirmation RDV")
                .message("RDV confirmé pour Mme Bennani demain 10h")
                .date(today)
                .time(LocalTime.of(10, 0))
                .type("CONFIRMATION")
                .priorité("MOYENNE")
                .build();
        data.add(n2);

        Notification n3 = Notification.builder()
                .id(nextId++)
                .titre("Annulation RDV")
                .message("Patient M. Zaidi a annulé son RDV")
                .date(today.minusDays(1))
                .time(LocalTime.of(16, 30))
                .type("ANNULATION")
                .priorité("BASSE")
                .build();
        data.add(n3);

        Notification n4 = Notification.builder()
                .id(nextId++)
                .titre("Stock Faible")
                .message("Stock de gants chirurgicaux faible")
                .date(today)
                .time(LocalTime.of(9, 0))
                .type("ALERTE")
                .priorité("HAUTE")
                .build();
        data.add(n4);

        Notification n5 = Notification.builder()
                .id(nextId++)
                .titre("Paiement Reçu")
                .message("Paiement de 500 DH reçu de Mme Alaoui")
                .date(today)
                .time(LocalTime.of(11, 30))
                .type("PAIEMENT")
                .priorité("MOYENNE")
                .build();
        data.add(n5);

        data.sort(Comparator.comparing(Notification::getDate).reversed()
                .thenComparing(Comparator.comparing(Notification::getTime).reversed()));
    }

    @Override
    public List<Notification> findAll() {
        return new ArrayList<>(data);
    }

    @Override
    public Notification findById(Long id) {
        return data.stream()
                .filter(n -> n.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void create(Notification notification) {
        if (notification.getId() == null) {
            notification.setId(nextId++);
        }
        if (notification.getDate() == null) {
            notification.setDate(LocalDate.now());
        }
        if (notification.getTime() == null) {
            notification.setTime(LocalTime.now());
        }
        data.add(notification);
    }

    @Override
    public void update(Notification notification) {
        deleteById(notification.getId());
        data.add(notification);
        data.sort(Comparator.comparing(Notification::getDate).reversed()
                .thenComparing(Comparator.comparing(Notification::getTime).reversed()));
    }

    @Override
    public void delete(Notification notification) {
        data.removeIf(n -> n.getId().equals(notification.getId()));
    }

    @Override
    public void deleteById(Long id) {
        data.removeIf(n -> n.getId().equals(id));
    }

    @Override
    public List<Notification> findByUserId(Long userId) {
        // Dans une vraie implémentation, filtrer par user
        return findAll();
    }

    @Override
    public List<Notification> findByType(String type) {
        return data.stream()
                .filter(n -> type != null && type.equalsIgnoreCase(n.getType()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Notification> findByPriorite(String priorite) {
        return data.stream()
                .filter(n -> priorite != null && priorite.equalsIgnoreCase(n.getPriorité()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Notification> findByDate(LocalDate date) {
        return data.stream()
                .filter(n -> date != null && date.equals(n.getDate()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Notification> findBetweenDates(LocalDate startDate, LocalDate endDate) {
        return data.stream()
                .filter(n -> n.getDate() != null
                        && !n.getDate().isBefore(startDate)
                        && !n.getDate().isAfter(endDate))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteOlderThan(LocalDate date) {
        data.removeIf(n -> n.getDate() != null && n.getDate().isBefore(date));
    }
}