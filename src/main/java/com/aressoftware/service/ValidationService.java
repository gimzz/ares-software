package com.aressoftware.service;

public class ValidationService {

   
    public boolean validateCedula(String cedula) {
        if (cedula == null || cedula.isBlank()) return false;
        return cedula.matches("^[0-9]{6,12}$");
    }

 
    public boolean validateRIF(String rif) {
        if (rif == null || rif.isBlank()) return false;
        return rif.matches("^[JVGEP]-\\d{6,9}-\\d$");
    }

   
    public boolean validateEmail(String email) {
        if (email == null || email.isBlank()) return false;
        return email.matches("^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,6}$");
    }

   
    public boolean validatePhone(String phone) {
        if (phone == null || phone.isBlank()) return false;
        return phone.matches("^0(412|414|424|416|426)\\d{7}$");
    }
}
