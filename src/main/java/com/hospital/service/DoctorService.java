package com.hospital.service;

import com.hospital.dto.Dto.DoctorReq;
import com.hospital.model.*;
import com.hospital.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service @RequiredArgsConstructor
public class DoctorService {
    private final DoctorRepository repo;
    private final UserService userService;

    public List<Doctor> all() { return repo.findAll(); }
    public Doctor get(Long id) { return repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Doctor not found")); }

    public Doctor create(DoctorReq r) {
        Doctor d = new Doctor();
        d.setUser(userService.create(r.name(), r.email(), r.password(), User.Role.DOCTOR));
        return repo.save(apply(d, r));
    }

    public Doctor update(Long id, DoctorReq r) {
        Doctor d = get(id);
        userService.update(d.getUser(), r.name(), r.email(), r.password());
        return repo.save(apply(d, r));
    }

    public void delete(Long id) { repo.delete(get(id)); }

    private Doctor apply(Doctor d, DoctorReq r) {
        d.setSpecialization(r.specialization()); d.setQualification(r.qualification()); d.setPhone(r.phone());
        d.setAvailability(r.availability()); d.setFee(r.fee());
        return d;
    }
}
