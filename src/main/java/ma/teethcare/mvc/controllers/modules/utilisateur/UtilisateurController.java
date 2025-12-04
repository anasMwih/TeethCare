
package ma.teethcare.mvc.controllers.modules.utilisateur;

import ma.teethcare.entities.Utilisateur;
import ma.teethcare.service.modules.utilisateur.api.UtilisateurService;

import java.util.List;

public class UtilisateurController {

    private final UtilisateurService utilisateurService;

    public UtilisateurController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    public Utilisateur createUtilisateur(Utilisateur utilisateur) {
        return utilisateurService.createUtilisateur(utilisateur);
    }

    public Utilisateur updateUtilisateur(Utilisateur utilisateur) {
        return utilisateurService.updateUtilisateur(utilisateur);
    }

    public Utilisateur getUtilisateurById(Long id) {
        return utilisateurService.getUtilisateurById(id);
    }

    public void deleteUtilisateur(Long id) {
        utilisateurService.deleteUtilisateur(id);
    }

    public List<Utilisateur> getAllUtilisateurs() {
        return utilisateurService.getAllUtilisateurs();
    }

    public Utilisateur login(String login, String password) {
        return utilisateurService.login(login, password);
    }

    public List<Utilisateur> getUtilisateursByType(String type) {
        return utilisateurService.getUtilisateursByType(type);
    }

    public List<Utilisateur> getUtilisateursByCabinet(Long cabinetId) {
        return utilisateurService.getUtilisateursByCabinet(cabinetId);
    }

    public List<Utilisateur> searchUtilisateurs(String keyword) {
        return utilisateurService.searchUtilisateurs(keyword);
    }
}