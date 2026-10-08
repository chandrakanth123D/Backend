package com.hospital.service;

import com.hospital.dto.Dto.Register;
import com.hospital.model.*;
import com.hospital.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service @RequiredArgsConstructor
public class PatientService {
    private final PatientRepository repo;
    private final UserService userService;
    private final CurrentUser me;

    public List<Patient> all() { return repo.findAll(); }
    public Patient get(Long id) { return repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient not found")); }

    public Patient create(Register r) {
        Patient p = new Patient();
        p.setUser(userService.create(r.name(), r.email(), r.password(), User.Role.PATIENT));
        return repo.save(apply(p, r));
    }

    public Patient update(Long id, Register r) {
        Patient p = get(id);
        userService.update(p.getUser(), r.name(), r.email(), r.password());
        return repo.save(apply(p, r));
    }

    public Patient updateMe(Register r) { return update(me.patient().getId(), r); }
    public void delete(Long id) { repo.delete(get(id)); }

    private Patient apply(Patient p, Register r) {
        p.setAge(r.age()); p.setGender(r.gender()); p.setPhone(r.phone()); p.setAddress(r.address()); p.setBloodGroup(r.bloodGroup());
        return p;
    }
}
