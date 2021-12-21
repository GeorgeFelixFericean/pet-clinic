package com.example.petclinic.persistence.repository;


import com.example.petclinic.persistence.entities.CampaignEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface CampaignRepository extends JpaRepository<CampaignEntity, Long>, JpaSpecificationExecutor<CampaignEntity> {

    Optional<CampaignEntity> findCampaignEntityByMonthAndYear(Integer month, Integer year);
}
