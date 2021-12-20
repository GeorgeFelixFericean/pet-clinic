package com.example.petclinic.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.petclinic.model.OwnerDetails;
import com.example.petclinic.persistence.entities.OwnerEntity;
import com.example.petclinic.persistence.repository.OwnerRepository;

@Service
public class OwnerDetailsService implements UserDetailsService {

    @Autowired
    OwnerRepository ownerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        OwnerEntity owner = ownerRepository.findOwnerEntityByUsername(username).get();
        return new OwnerDetails(owner);
    }
}
