package com.chone.server.domains.auth.service;

import com.chone.server.domains.auth.dto.CustomUserDetails;
import com.chone.server.domains.user.domain.User;
import com.chone.server.domains.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {


    private final UserRepository  userRepository; //사용자 정보를 조회하기 위한 Repository

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //DB에서 조회
        User userData = userRepository.findByUsername(username).orElseThrow();
        if(userData != null) {
            //UserDetails에 담아서 return하면 AutenticationManager가 검증 함
            return new CustomUserDetails(userData);
        }
        return null;
    }
}
