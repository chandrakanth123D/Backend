package com.hospital.service;

import com.hospital.model.Medicine;
import com.hospital.repository.MedicineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service @RequiredArgsConstructor
public class MedicineService {
    private final MedicineRepository repo;

    public List<Medicine> all() { return repo.findAll(); }
    public Medicine get(Long id) { return repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Medicine not found")); }
    public Medicine create(Medicine m) { m.setId(null); return repo.save(m); }

    public Medicine update(Long id, Medicine in) {
        Medicine m = get(id);
        m.setName(in.getName()); m.setCategory(in.getCategory()); m.setStock(in.getStock());
        m.setPrice(in.getPrice()); m.setExpiryDate(in.getExpiryDate());
        return repo.save(m);
    }

    public Medicine purchase(Long id, int qty) { Medicine m = get(id); m.setStock(m.getStock() + qty); return repo.save(m); }
    public void delete(Long id) { repo.delete(get(id)); }
}
