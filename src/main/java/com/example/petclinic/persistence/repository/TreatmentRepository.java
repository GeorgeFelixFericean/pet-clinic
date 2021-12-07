package com.example.petclinic.persistence.repository;

import com.example.petclinic.persistence.entities.TreatmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TreatmentRepository extends JpaRepository<TreatmentEntity, Long>, JpaSpecificationExecutor<TreatmentEntity> {
}
