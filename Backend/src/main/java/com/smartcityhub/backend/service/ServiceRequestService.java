
package com.smartcityhub.backend.service;

import com.smartcityhub.backend.model.ServiceRequest;
import com.smartcityhub.backend.repository.ServiceRequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Set;

@Service
public class ServiceRequestService {

    private final ServiceRequestRepository repository;

    private static final Set<String> VALID_STATUSES =
            Set.of("SUBMITTED", "IN_PROGRESS", "RESOLVED");

    public ServiceRequestService(ServiceRequestRepository repository) {
        this.repository = repository;
    }

    public List<ServiceRequest> getAllRequests() {
        return repository.findAll();
    }

    public ServiceRequest getRequest(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Request not found"));
    }

    public List<ServiceRequest> getByDepartment(String department) {
        return repository.findByDepartmentIgnoreCase(department);
    }

    public ServiceRequest createRequest(ServiceRequest request) {
        if (isBlank(request.getTitle())
                || isBlank(request.getDescription())
                || isBlank(request.getLocation())
                || isBlank(request.getResidentName())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Name, title, description, and location are required");
        }

        request.setStatus("SUBMITTED");
        request.setProgressNotes("Request submitted by resident.");

        // These fields are controlled by the application.
        request.setDepartment("Unassigned");

        return repository.save(request);
    }

    public ServiceRequest updateRequest(
            Long id, String status, String department, String notes) {

        ServiceRequest request = getRequest(id);

        if (status != null) {
            if (!VALID_STATUSES.contains(status)) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Invalid status");
            }
            request.setStatus(status);
        }

        if (department != null) {
            if (department.isBlank()) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Department cannot be blank");
            }
            request.setDepartment(department);
        }

        if (notes != null) {
            request.setProgressNotes(notes);
        }

        return repository.save(request);
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
