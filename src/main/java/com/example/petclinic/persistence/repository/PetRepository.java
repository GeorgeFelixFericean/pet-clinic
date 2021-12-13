package com.example.petclinic.persistence.repository;

import com.example.petclinic.persistence.entities.OwnerEntity;
import com.example.petclinic.persistence.entities.PetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PetRepository extends JpaRepository<PetEntity, Long>, JpaSpecificationExecutor<PetEntity> {

    List<PetEntity> findPetEntitiesByOwner(OwnerEntity ownerEntity);

    Optional<List<PetEntity>> findPetEntitiesByTreatmentsIsNull();

}
