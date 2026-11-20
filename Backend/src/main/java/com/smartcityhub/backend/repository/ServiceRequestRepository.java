
package com.smartcityhub.backend.repository;

import com.smartcityhub.backend.model.ServiceRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ServiceRequestRepository
        extends JpaRepository<ServiceRequest, Long> {

    List<ServiceRequest> findByDepartmentIgnoreCase(String department);

    List<ServiceRequest> findByStatusIgnoreCase(String status);
}
