package com.teethcare.entites;

public enum StatutFacture {
    PAYEE, EN_ATTENTE, ANNULEE, PARTIELLEMENT_PAYEE
}

public enum StatutFinancier {
    SOLVABLE, EN_RETARD, IMPAYE, EN_NEGOCIATION
}

public enum EnPromo {
    OUI, NON, PROMO_SPECIALE
}

public enum Sexe {
    MASCULIN, FEMININ, AUTRE
}

public enum Assurance {
    CNSS, CNOPS, RAMED, AXA, AUTRE, SANS_ASSURANCE
}

public enum NiveauRisque {
    FAIBLE, MOYEN, ELEVE, TRES_ELEVE
}

public enum StatutConsultation {
    PLANIFIEE, TERMINEE, ANNULEE, REPORTEE
}

public enum AgendaDocteur {
    DISPONIBLE, OCCUPE, CONGE, URGENCE
}

public enum FormeMedicament {
    COMPRIME, SIROP, GELULE, POMMADE, INJECTABLE, SPRAY
}

public enum StatutRdv {
    PLANIFIE, CONFIRME, ANNULE, TERMINE, ABSENT
}

public enum StatutFileAttente {
    EN_ATTENTE, EN_COURS, TERMINE, ANNULE
}