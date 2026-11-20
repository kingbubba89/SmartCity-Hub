
package com.smartcityhub.backend.controller;

import com.smartcityhub.backend.model.ServiceRequest;
import com.smartcityhub.backend.service.ServiceRequestService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
public class ServiceRequestController {

    private final ServiceRequestService service;

    public ServiceRequestController(ServiceRequestService service) {
        this.service = service;
    }

    @GetMapping
    public List<ServiceRequest> getRequests(
            @RequestParam(required = false) String department) {

        if (department != null && !department.isBlank()) {
            return service.getByDepartment(department);
        }

        return service.getAllRequests();
    }

    @GetMapping("/{id}")
    public ServiceRequest getRequest(@PathVariable Long id) {
        return service.getRequest(id);
    }

    @PostMapping
    public ResponseEntity<ServiceRequest> createRequest(
            @RequestBody ServiceRequest request) {

        ServiceRequest created = service.createRequest(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }

    @PatchMapping("/{id}")
    public ServiceRequest updateRequest(
            @PathVariable Long id,
            @RequestBody RequestUpdate update) {

        return service.updateRequest(
                id,
                update.status(),
                update.department(),
                update.progressNotes()
        );
    }

    public record RequestUpdate(
            String status,
            String department,
            String progressNotes
    ) {}
}
