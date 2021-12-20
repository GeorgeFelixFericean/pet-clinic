package com.example.petclinic.persistence.repository;

import com.example.petclinic.persistence.entities.PetEntity;
import com.example.petclinic.persistence.entities.TreatmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TreatmentRepository extends JpaRepository<TreatmentEntity, Long>, JpaSpecificationExecutor<TreatmentEntity> {

    List<TreatmentEntity> findAllByPetAndTreatmentDateBetween(PetEntity petEntity, LocalDate from, LocalDate until);

    List<TreatmentEntity> findAllByPet(PetEntity petEntity);
}
