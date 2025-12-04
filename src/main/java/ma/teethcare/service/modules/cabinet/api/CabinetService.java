package ma.teethcare.service.modules.cabinet.api;

import ma.teethcare.entities.CabinetMedicale;

import java.util.List;

public interface CabinetService {
    CabinetMedicale createCabinet(CabinetMedicale cabinet);
    CabinetMedicale updateCabinet(CabinetMedicale cabinet);
    CabinetMedicale getCabinetById(Long id);
    void deleteCabinet(Long id);
    List<CabinetMedicale> getAllCabinets();
    CabinetMedicale getCabinetByEmail(String email);
    double getTotalRevenue(Long cabinetId);
    double getTotalCharges(Long cabinetId);
    double getBalance(Long cabinetId);
}
