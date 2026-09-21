package com.zhanlin.library_management_system.service;

import com.zhanlin.library_management_system.models.LibraryUser;
import com.zhanlin.library_management_system.repository.UserRepository;
import com.zhanlin.library_management_system.security.LibraryUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LibraryUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public LibraryUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


        LibraryUser user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User not found: " + username
                ));

        return new LibraryUserDetails(user);
    }
}
