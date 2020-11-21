package com.krego.farmacy.helpers;

import com.krego.farmacy.model.Manager;
import com.krego.farmacy.repositories.ManagerRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Setter (onMethod_ = @Autowired)
    ManagerRepository managerRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Manager manager = managerRepository.findById(Long.parseLong(username))
                .orElseThrow(() ->
                        new UsernameNotFoundException("User was not found with username: " + username));

        return UserPrincipal.create(manager);
    }

    @Transactional
    public UserDetails loadUserById(Long code) {
        Manager manager = managerRepository.findById(code).orElseThrow(
                () -> new UsernameNotFoundException("User not found with id: " + code)
        );

        return UserPrincipal.create(manager);
    }
}
