package ma.teethcare.repository.modules.agenda.api;

import ma.teethcare.entities.AgendaMensuel;
import ma.teethcare.entities.Jour;
import ma.teethcare.entities.enums.Mois;
import ma.teethcare.repository.common.CrudRepository;

import java.util.List;

public interface AgendaRepository extends CrudRepository<AgendaMensuel, Long> {
    AgendaMensuel findByMoisAndUtilisateurId(Mois mois, Long utilisateurId);
    List<AgendaMensuel> findByUtilisateurId(Long utilisateurId);
    void addJourNonDisponible(Long agendaId, Jour jour);
    void removeJourNonDisponible(Long agendaId, Jour jour);
    List<Jour> getJoursNonDisponibles(Long agendaId);
    boolean isJourDisponible(Long agendaId, String nomJour);
}
