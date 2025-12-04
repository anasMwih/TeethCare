
package ma.teethcare.mvc.controllers.modules.cabinet;

import ma.teethcare.entities.CabinetMedicale;
import ma.teethcare.service.modules.cabinet.api.CabinetService;

import java.util.List;

public class CabinetController {

    private final CabinetService cabinetService;

    public CabinetController(CabinetService cabinetService) {
        this.cabinetService = cabinetService;
    }

    public CabinetMedicale createCabinet(CabinetMedicale cabinet) {
        return cabinetService.createCabinet(cabinet);
    }

    public CabinetMedicale updateCabinet(CabinetMedicale cabinet) {
        return cabinetService.updateCabinet(cabinet);
    }

    public CabinetMedicale getCabinetById(Long id) {
        return cabinetService.getCabinetById(id);
    }

    public void deleteCabinet(Long id) {
        cabinetService.deleteCabinet(id);
    }

    public List<CabinetMedicale> getAllCabinets() {
        return cabinetService.getAllCabinets();
    }

    public CabinetMedicale getCabinetByEmail(String email) {
        return cabinetService.getCabinetByEmail(email);
    }

    public double getBalance(Long cabinetId) {
        return cabinetService.getBalance(cabinetId);
    }
}