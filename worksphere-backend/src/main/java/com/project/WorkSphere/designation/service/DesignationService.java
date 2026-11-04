package com.project.WorkSphere.designation.service;

import com.project.WorkSphere.designation.dto.DesignationRequest;
import com.project.WorkSphere.designation.dto.DesignationResponse;
import com.project.WorkSphere.designation.entity.Designation;
import com.project.WorkSphere.designation.repository.DesignationRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DesignationService {

    private final DesignationRepository designationRepository;

    public DesignationService(DesignationRepository designationRepository) {
        this.designationRepository = designationRepository;
    }

    // Create designation
    @Transactional
    public DesignationResponse createDesignation(DesignationRequest request) {

        String name = request.getName().trim();

        if (designationRepository.existsByNameIgnoreCase(name)) {
            throw new IllegalArgumentException("Designation already exists");
        }

        Designation designation = new Designation();
        designation.setName(name);
        designation.setDescription(request.getDescription());
        designation.setActive(true);

        Designation savedDesignation = designationRepository.save(designation);

        return mapToResponse(savedDesignation);
    }

    // Get all designations
    @Transactional(readOnly = true)
    public List<DesignationResponse> getAllDesignations() {

        return designationRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // Get designation by ID
    @Transactional(readOnly = true)
    public DesignationResponse getDesignationById(Long id) {

        Designation designation = designationRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Designation not found with ID: " + id));

        return mapToResponse(designation);
    }

    // Update designation
    @Transactional
    public DesignationResponse updateDesignation(
            Long id, DesignationRequest request) {

        Designation designation = designationRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Designation not found with ID: " + id));

        String newName = request.getName().trim();

        if (!designation.getName().equalsIgnoreCase(newName)
                && designationRepository.existsByNameIgnoreCase(newName)) {
            throw new IllegalArgumentException(
                    "Designation name already exists");
        }

        designation.setName(newName);
        designation.setDescription(request.getDescription());

        return mapToResponse(designationRepository.save(designation));
    }

    // Deactivate designation
    @Transactional
    public void deactivateDesignation(Long id) {

        Designation designation = designationRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Designation not found with ID: " + id));

        designation.setActive(false);

        designationRepository.save(designation);
    }

    // Get active designations
    @Transactional(readOnly = true)
    public List<DesignationResponse> getActiveDesignations() {

        return designationRepository.findByActiveTrue()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // Entity to DTO mapping
    private DesignationResponse mapToResponse(Designation designation) {

        return new DesignationResponse(
                designation.getId(),
                designation.getName(),
                designation.getDescription(),
                designation.isActive(),
                designation.getCreatedAt()
        );
    }
}
