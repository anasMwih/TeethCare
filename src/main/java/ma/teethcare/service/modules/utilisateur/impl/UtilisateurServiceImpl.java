package ma.teethcare.service.modules.utilisateur.impl;

import ma.teethcare.entities.Utilisateur;
import ma.teethcare.repository.modules.utilisateur.api.UtilisateurRepository;
import ma.teethcare.service.modules.utilisateur.api.UtilisateurService;

import java.util.List;

public class UtilisateurServiceImpl implements UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;

    public UtilisateurServiceImpl(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    @Override
    public Utilisateur createUtilisateur(Utilisateur utilisateur) {
        // Validation
        if (utilisateurRepository.existsByLogin(utilisateur.getLogin())) {
            throw new IllegalArgumentException("Login already exists");
        }
        if (utilisateurRepository.existsByEmail(utilisateur.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        utilisateurRepository.create(utilisateur);
        return utilisateur;
    }

    @Override
    public Utilisateur updateUtilisateur(Utilisateur utilisateur) {
        Utilisateur existing = getUtilisateurById(utilisateur.getIdUser());
        if (existing == null) {
            throw new IllegalArgumentException("User not found");
        }

        utilisateurRepository.update(utilisateur);
        return utilisateur;
    }

    @Override
    public Utilisateur getUtilisateurById(Long id) {
        return utilisateurRepository.findById(id);
    }

    @Override
    public void deleteUtilisateur(Long id) {
        utilisateurRepository.deleteById(id);
    }

    @Override
    public List<Utilisateur> getAllUtilisateurs() {
        return utilisateurRepository.findAll();
    }

    @Override
    public Utilisateur login(String login, String password) {
        Utilisateur utilisateur = utilisateurRepository.findByLogin(login)
                .orElseThrow(() -> new IllegalArgumentException("Invalid login"));

        if (!utilisateur.getMotDePass().equals(password)) {
            throw new IllegalArgumentException("Invalid password");
        }

        return utilisateur;
    }

    @Override
    public List<Utilisateur> getUtilisateursByType(String type) {
        return utilisateurRepository.findByType(type);
    }

    @Override
    public List<Utilisateur> getUtilisateursByCabinet(Long cabinetId) {
        return utilisateurRepository.findByCabinetMedicaleId(cabinetId);
    }

    @Override
    public List<Utilisateur> searchUtilisateurs(String keyword) {
        return utilisateurRepository.searchByNom(keyword);
    }
}