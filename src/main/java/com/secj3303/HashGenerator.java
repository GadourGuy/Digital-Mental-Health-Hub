package com.secj3303;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class HashGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // 1. Generate Hash for ADMIN
        // Change "AdminStrongPass@2026" to whatever password you want for Admin
        String adminRaw = "123"; 
        String adminHash = encoder.encode(adminRaw);
        System.out.println("ADMIN HASH for DB: " + adminHash);

        // 2. Generate Hash for PROFESSIONAL
        // Change "DoctorSafePass@2026" to whatever password you want for Doctor
        String professionalRaw = "123";
        String professionalHash = encoder.encode(professionalRaw);
        System.out.println("PROFESSIONAL HASH for DB: " + professionalHash);
    }
}