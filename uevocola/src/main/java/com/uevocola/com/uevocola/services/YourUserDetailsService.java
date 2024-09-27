package com.uevocola.com.uevocola.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.uevocola.com.uevocola.models.UserModel;
import com.uevocola.com.uevocola.repositories.UserRepository;

import java.util.Optional;

@Service
public class YourUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Busca o usuário pelo email no banco de dados
        Optional<UserModel> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            UserModel user = userOptional.get();
            return org.springframework.security.core.userdetails.User
                    .withUsername(user.getEmail())
                    .password(user.getPassword())
                    .accountLocked(false) // Para permitir login, deve ser false
                    .disabled(false) // Para permitir login, deve ser false
                    .build();
        } else {
            throw new UsernameNotFoundException("Usuário não encontrado com email: " + email);
        }
    }
}
