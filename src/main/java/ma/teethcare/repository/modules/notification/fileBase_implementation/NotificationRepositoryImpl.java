package ma.teethcare.repository.modules.notification.fileBase_implementation;

import ma.teethcare.entities.Notification;
import ma.teethcare.repository.modules.notification.api.NotificationRepository;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class NotificationRepositoryImpl implements NotificationRepository {

    private static final String CLASSPATH_FILE = "fileBase/notifications.psv";
    private static final String HEADER = "ID|Titre|Message|Date|Time|Type|Priorite";

    private final Path localFilePath =
            Paths.get(System.getProperty("user.home"), ".teethcare", "fileBase", "notifications.psv");

    public NotificationRepositoryImpl() {
        initializeLocalCopy();
    }

    private void initializeLocalCopy() {
        try {
            if (!Files.exists(localFilePath)) {
                Files.createDirectories(localFilePath.getParent());
                URL resource = getClass().getClassLoader().getResource(CLASSPATH_FILE);
                if (resource != null) {
                    Path src = Paths.get(resource.toURI());
                    Files.copy(src, localFilePath, StandardCopyOption.REPLACE_EXISTING);
                } else {
                    Files.write(localFilePath, List.of(HEADER), StandardCharsets.UTF_8);
                }
            }
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException("Erreur d'initialisation du fichier notifications.psv", e);
        }
    }

    private List<String> readAllLines() {
        try {
            return Files.readAllLines(localFilePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Erreur de lecture du fichier notifications.psv", e);
        }
    }

    private void writeAllLines(List<String> lines) {
        try {
            Files.write(localFilePath, lines, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Erreur d'écriture dans notifications.psv", e);
        }
    }

    @Override
    public List<Notification> findAll() {
        return readAllLines().stream()
                .skip(1)
                .filter(line -> !line.isBlank())
                .map(this::toNotification)
                .collect(Collectors.toList());
    }

    @Override
    public Notification findById(Long id) {
        return findAll().stream()
                .filter(n -> Objects.equals(n.getId(), id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void create(Notification notification) {
        List<Notification> notifications = findAll();
        long newId = notifications.stream()
                .mapToLong(n -> n.getId() == null ? 0 : n.getId())
                .max().orElse(0) + 1;
        notification.setId(newId);
        if (notification.getDate() == null) {
            notification.setDate(LocalDate.now());
        }
        if (notification.getTime() == null) {
            notification.setTime(LocalTime.now());
        }
        notifications.add(notification);
        saveAll(notifications);
    }

    @Override
    public void update(Notification notification) {
        List<Notification> notifications = findAll();
        for (int i = 0; i < notifications.size(); i++) {
            if (Objects.equals(notifications.get(i).getId(), notification.getId())) {
                notifications.set(i, notification);
                saveAll(notifications);
                return;
            }
        }
        throw new RuntimeException("Notification avec ID " + notification.getId() + " introuvable.");
    }

    @Override
    public void delete(Notification notification) {
        if (notification != null && notification.getId() != null)
            deleteById(notification.getId());
    }

    @Override
    public void deleteById(Long id) {
        List<Notification> notifications = findAll().stream()
                .filter(n -> !Objects.equals(n.getId(), id))
                .collect(Collectors.toList());
        saveAll(notifications);
    }

    @Override
    public List<Notification> findByUserId(Long userId) {
        // Association User-Notification à implémenter selon la logique métier
        return findAll();
    }

    @Override
    public List<Notification> findByType(String type) {
        return findAll().stream()
                .filter(n -> type != null && type.equalsIgnoreCase(n.getType()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Notification> findByPriorite(String priorite) {
        return findAll().stream()
                .filter(n -> priorite != null && priorite.equalsIgnoreCase(n.getPriorité()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Notification> findByDate(LocalDate date) {
        return findAll().stream()
                .filter(n -> date != null && date.equals(n.getDate()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Notification> findBetweenDates(LocalDate startDate, LocalDate endDate) {
        return findAll().stream()
                .filter(n -> n.getDate() != null
                        && !n.getDate().isBefore(startDate)
                        && !n.getDate().isAfter(endDate))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteOlderThan(LocalDate date) {
        List<Notification> notifications = findAll().stream()
                .filter(n -> n.getDate() == null || !n.getDate().isBefore(date))
                .collect(Collectors.toList());
        saveAll(notifications);
    }

    private void saveAll(List<Notification> notifications) {
        List<String> lines = new ArrayList<>();
        lines.add(HEADER);
        for (Notification n : notifications) {
            lines.add(String.join("|",
                    stringOrNull(n.getId()),
                    stringOrNull(n.getTitre()),
                    stringOrNull(n.getMessage()),
                    stringOrNull(n.getDate()),
                    stringOrNull(n.getTime()),
                    stringOrNull(n.getType()),
                    stringOrNull(n.getPriorité())
            ));
        }
        writeAllLines(lines);
    }

    private Notification toNotification(String line) {
        String[] t = line.split("\\|", -1);
        Notification n = new Notification();
        n.setId(parseLong(t[0]));
        n.setTitre(parseNullableString(t[1]));
        n.setMessage(parseNullableString(t[2]));
        n.setDate(parseNullableLocalDate(t[3]));
        n.setTime(parseNullableLocalTime(t[4]));
        n.setType(parseNullableString(t[5]));
        n.setPriorité(parseNullableString(t[6]));
        return n;
    }

    private String parseNullableString(String s) {
        return (s == null || s.isBlank() || s.equalsIgnoreCase("null")) ? null : s.trim();
    }

    private LocalDate parseNullableLocalDate(String s) {
        if (s == null || s.isBlank() || s.equalsIgnoreCase("null")) return null;
        try { return LocalDate.parse(s.trim()); } catch (Exception e) { return null; }
    }

    private LocalTime parseNullableLocalTime(String s) {
        if (s == null || s.isBlank() || s.equalsIgnoreCase("null")) return null;
        try { return LocalTime.parse(s.trim()); } catch (Exception e) { return null; }
    }

    private Long parseLong(String s) {
        try { return (s == null || s.isBlank() || s.equalsIgnoreCase("null")) ? null : Long.parseLong(s.trim()); }
        catch (NumberFormatException e) { return null; }
    }

    private String stringOrNull(Object o) {
        return (o == null) ? "null" : o.toString();
    }
}