

// ma/teethcare/service/modules/cabinet/impl/CabinetServiceImpl.java
package ma.teethcare.service.modules.cabinet.impl;

import ma.teethcare.entities.CabinetMedicale;
import ma.teethcare.repository.modules.cabinet.api.CabinetRepository;
import ma.teethcare.repository.modules.finances.api.ChargesRepository;
import ma.teethcare.repository.modules.finances.api.RevenuesRepository;
import ma.teethcare.service.modules.cabinet.api.CabinetService;

import java.util.List;

public class CabinetServiceImpl implements CabinetService {

    private final CabinetRepository cabinetRepository;
    private final ChargesRepository chargesRepository;
    private final RevenuesRepository revenuesRepository;

    public CabinetServiceImpl(CabinetRepository cabinetRepository,
                              ChargesRepository chargesRepository,
                              RevenuesRepository revenuesRepository) {
        this.cabinetRepository = cabinetRepository;
        this.chargesRepository = chargesRepository;
        this.revenuesRepository = revenuesRepository;
    }

    @Override
    public CabinetMedicale createCabinet(CabinetMedicale cabinet) {
        if (cabinetRepository.existsByEmail(cabinet.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        if (cabinetRepository.existsByCin(cabinet.getCin())) {
            throw new IllegalArgumentException("CIN already exists");
        }

        cabinetRepository.create(cabinet);
        return cabinet;
    }

    @Override
    public CabinetMedicale updateCabinet(CabinetMedicale cabinet) {
        CabinetMedicale existing = getCabinetById(cabinet.getIdUser());
        if (existing == null) {
            throw new IllegalArgumentException("Cabinet not found");
        }

        cabinetRepository.update(cabinet);
        return cabinet;
    }

    @Override
    public CabinetMedicale getCabinetById(Long id) {
        return cabinetRepository.findById(id);
    }

    @Override
    public void deleteCabinet(Long id) {
        cabinetRepository.deleteById(id);
    }

    @Override
    public List<CabinetMedicale> getAllCabinets() {
        return cabinetRepository.findAll();
    }

    @Override
    public CabinetMedicale getCabinetByEmail(String email) {
        return cabinetRepository.findByEmail(email).orElse(null);
    }

    @Override
    public double getTotalRevenue(Long cabinetId) {
        return revenuesRepository.getTotalRevenuesByCabinet(cabinetId);
    }

    @Override
    public double getTotalCharges(Long cabinetId) {
        return chargesRepository.getTotalChargesByCabinet(cabinetId);
    }

    @Override
    public double getBalance(Long cabinetId) {
        double totalRevenues = getTotalRevenue(cabinetId);
        double totalCharges = getTotalCharges(cabinetId);
        return totalRevenues - totalCharges;
    }
}