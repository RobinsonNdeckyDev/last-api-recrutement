package com.example.api_recrutement.services;

import com.example.api_recrutement.models.Admin;
import com.example.api_recrutement.repository.AdminRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// Service pour la gestion des administrateurs
@Service
public class AdminService {
    private final AdminRepository adminRepository;

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    public Optional<Admin> getAdminById(Long id) {
        return adminRepository.findById(id);
    }

    public Admin createAdmin(Admin admin) {
        return adminRepository.save(admin);
    }

    public Admin updateAdmin(Long id, Admin adminDetails) {
        return adminRepository.findById(id).map(admin -> {
            return adminRepository.save(adminDetails);
        }).orElseThrow(() -> new RuntimeException("Admin non existant"));
    }

    public void deleteAdmin(Long id) {
        adminRepository.deleteById(id);
    }
}
