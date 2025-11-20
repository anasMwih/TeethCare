package ma.teethcare.repository.modules.agenda.api;

import ma.teethcare.entities.AgendaMensuel;
import ma.teethcare.repository.common.CrudRepository;

import java.util.List;

public interface AgendaMensuelRepository extends CrudRepository<AgendaMensuel, Long> {

    AgendaMensuel findByMois(String mois);

    List<AgendaMensuel> findByMedecinId(Long medecinId);

    AgendaMensuel findCurrentByMedecinId(Long medecinId);
}