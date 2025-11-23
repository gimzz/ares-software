package com.aressoftware.service;

import com.aressoftware.dao.ClientTypeDAO;
import com.aressoftware.dao.PaymentTermsDAO;
import com.aressoftware.dao.TypeIdentificationDAO;
import com.aressoftware.model.configuration.ClientType;
import com.aressoftware.model.configuration.PaymentTerms;
import com.aressoftware.model.configuration.TypeIdentification;

import java.util.List;

/**
 * ConfiguracionService
 *
 * Servicio encargado de centralizar el acceso a los catálogos de:
 *  - Tipos de Cliente
 *  - Condiciones de Pago
 *  - Tipos de Identificación
 *
 * Mantiene el código limpio y organizado, permitiendo que cualquier
 * parte del sistema consulte estos catálogos fácilmente.
 */
public class ConfigurationService {

    private final ClientTypeDAO clientTypeDAO;
    private final PaymentTermsDAO paymentTermsDAO;
    private final TypeIdentificationDAO typeIdentificationDAO;

    public ConfigurationService() {
        this.clientTypeDAO = new ClientTypeDAO();
        this.paymentTermsDAO = new PaymentTermsDAO();
        this.typeIdentificationDAO = new TypeIdentificationDAO();
    }

    // ============================================================
    //          CARGA DE CATÁLOGOS
    // ============================================================

    public List<ClientType> getClientTypes() {
        return clientTypeDAO.findAll();
    }

    public List<PaymentTerms> getPaymentTerms() {
        return paymentTermsDAO.findAll();
    }

    public List<TypeIdentification> getIdentificationTypes() {
        return typeIdentificationDAO.findAll();
    }

    // ============================================================
    //          VALIDACIONES BÁSICAS
    // ============================================================

    public boolean isValidClientType(int id) {
        ClientType type = clientTypeDAO.findById(id);
        return type != null;
    }

    public boolean isValidPaymentTerm(int id) {
        PaymentTerms term = paymentTermsDAO.findById(id);
        return term != null;
    }

    public boolean isValidIdentificationType(int id) {
        TypeIdentification identification = typeIdentificationDAO.findById(id);
        return identification != null;
    }
}
