package com.gdg.workfit.service;

import com.gdg.workfit.domain.Company;
import com.gdg.workfit.repository.CompanyRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company getOrCreate(Long companyId, String name, String location, String iconUrl) {
        if (companyId != null) {
            Optional<Company> existing = companyRepository.findById(companyId);
            if (existing.isPresent()) {
                return existing.get();
            }
        }
        if (name == null || location == null || iconUrl == null) {
            throw new IllegalArgumentException("companyName/companyLocation/companyIconUrl are required when companyId is null.");
        }
        return companyRepository.save(new Company(name, location, iconUrl));
    }
}
