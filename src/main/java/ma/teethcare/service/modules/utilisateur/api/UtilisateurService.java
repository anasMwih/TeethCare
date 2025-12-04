package ma.teethcare.service.modules.utilisateur.api;

import ma.teethcare.entities.Utilisateur;

import java.util.List;

public interface UtilisateurService {
    Utilisateur createUtilisateur(Utilisateur utilisateur);
    Utilisateur updateUtilisateur(Utilisateur utilisateur);
    Utilisateur getUtilisateurById(Long id);
    void deleteUtilisateur(Long id);
    List<Utilisateur> getAllUtilisateurs();
    Utilisateur login(String login, String password);
    List<Utilisateur> getUtilisateursByType(String type);
    List<Utilisateur> getUtilisateursByCabinet(Long cabinetId);
    List<Utilisateur> searchUtilisateurs(String keyword);
}