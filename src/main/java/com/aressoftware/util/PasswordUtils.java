package com.aressoftware.util;
import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        // Ajuste en caso de prefijos ($2b$, $2y$) que pueden causar errores
        if (hashedPassword.startsWith("$2b$") || hashedPassword.startsWith("$2y$")) {
            hashedPassword = "$2a$" + hashedPassword.substring(4);
        }
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
