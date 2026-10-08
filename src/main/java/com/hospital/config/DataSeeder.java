package com.hospital.config;

import com.hospital.model.*;
import com.hospital.model.User.Role;
import com.hospital.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.time.LocalDate;

/** Creates demo data on first start so every role can be tried immediately. */
@Component @RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {
    private final UserRepository users;
    private final DoctorRepository doctors;
    private final PatientRepository patients;
    private final MedicineRepository medicines;
    private final PasswordEncoder encoder;

    private User user(String name, String email, String pw, Role role) {
        User u = new User();
        u.setName(name); u.setEmail(email); u.setPassword(encoder.encode(pw)); u.setRole(role);
        return u;
    }

    @Override
    public void run(String... args) {
        if (users.count() > 0) return;

        users.save(user("System Admin", "admin@hospital.com", "admin123", Role.ADMIN));

        Object[][] docs = {
            {"Dr. Asha Rao", "asha@hospital.com", "Cardiology", "MD, DM Cardiology", "Mon-Fri 09:00-16:00", 800.0},
            {"Dr. Rohan Mehta", "rohan@hospital.com", "Orthopedics", "MS Orthopedics", "Mon-Sat 10:00-15:00", 600.0},
            {"Dr. Neha Iyer", "neha@hospital.com", "Pediatrics", "MD Pediatrics", "Mon-Fri 09:00-14:00", 500.0},
            {"Dr. Vikram Shah", "vikram@hospital.com", "General Medicine", "MBBS, MD", "Mon-Sat 09:00-17:00", 400.0}};
        for (Object[] d : docs) {
            Doctor doc = new Doctor();
            doc.setUser(user((String) d[0], (String) d[1], "doctor123", Role.DOCTOR));
            doc.setSpecialization((String) d[2]); doc.setQualification((String) d[3]);
            doc.setAvailability((String) d[4]); doc.setFee((Double) d[5]); doc.setPhone("9000000000");
            doctors.save(doc);
        }

        Patient p = new Patient();
        p.setUser(user("Demo Patient", "patient@hospital.com", "patient123", Role.PATIENT));
        p.setAge(34); p.setGender("Female"); p.setPhone("9111111111"); p.setAddress("Hyderabad"); p.setBloodGroup("O+");
        patients.save(p);

        Object[][] meds = {
            {"Paracetamol 500mg", "Analgesic", 200, 2.5, 18}, {"Amoxicillin 250mg", "Antibiotic", 120, 6.0, 12},
            {"Cetirizine 10mg", "Antihistamine", 150, 3.0, 20}, {"Pantoprazole 40mg", "Antacid", 15, 8.0, 9},
            {"Metformin 500mg", "Antidiabetic", 90, 4.0, 3}, {"Atorvastatin 10mg", "Statin", 60, 9.5, 24}};
        for (Object[] m : meds) {
            Medicine med = new Medicine();
            med.setName((String) m[0]); med.setCategory((String) m[1]); med.setStock((Integer) m[2]);
            med.setPrice((Double) m[3]); med.setExpiryDate(LocalDate.now().plusMonths((Integer) m[4]));
            medicines.save(med);
        }
        System.out.println("Seeded demo data. Admin: admin@hospital.com / admin123");
    }
}
