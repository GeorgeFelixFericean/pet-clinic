package com.example.petclinic.persistence.repository;

import com.example.petclinic.persistence.entities.OwnerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface OwnerRepository extends JpaRepository<OwnerEntity, Long>, JpaSpecificationExecutor<OwnerEntity> {

}
