package com.zhanlin.library_management_system.security.service;

import com.zhanlin.library_management_system.exceptions.BusinessRuleException;
import com.zhanlin.library_management_system.exceptions.ErrorCode;
import com.zhanlin.library_management_system.messages.ApiErrorMessage;
import com.zhanlin.library_management_system.models.LibraryUser;
import com.zhanlin.library_management_system.models.Reader;
import com.zhanlin.library_management_system.models.Role;
import com.zhanlin.library_management_system.repository.ReaderRepository;
import com.zhanlin.library_management_system.repository.UserRepository;
import com.zhanlin.library_management_system.security.dto.RegisterRequest;
import com.zhanlin.library_management_system.security.web.RestAuthenticationEntryPoint;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ReaderRepository readerRepository;

    public AuthService(
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            ReaderRepository readerRepository,
            RestAuthenticationEntryPoint restAuthenticationEntryPoint) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.readerRepository = readerRepository;
    }


    public String login(String email, String password) {

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                email,
                password
        );

        Authentication authenticated = authenticationManager.authenticate(authentication);

        return jwtService.generateToken(authenticated);
    }

    //TODO: сделать кастомное исключение
    @Transactional(
            propagation = Propagation.REQUIRED,
            isolation = Isolation.DEFAULT,
            readOnly = false
    )
    public String register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessRuleException(
                    ErrorCode.EMAIL_ALREADY_EXISTS,
                    ApiErrorMessage.EMAIL_ALREADY_EXISTS.getMessage(request.getEmail())
            );
        }

        if (readerRepository.existsByPhone(request.getPhone())) {
            throw new BusinessRuleException(
                    ErrorCode.PHONE_ALREADY_EXISTS,
                    ApiErrorMessage.PHONE_ALREADY_EXISTS.getMessage(request.getPhone())
            );
        }

        Reader reader = new Reader();

        reader.setFirstName(request.getFirstName());
        reader.setLastName(request.getLastName());
        reader.setEmail(request.getEmail());
        reader.setPhone(request.getPhone());

        readerRepository.save(reader);

        String passwordHash = passwordEncoder.encode(request.getPassword());

        LibraryUser user = new LibraryUser(
                request.getEmail(),
                passwordHash,
                Role.USER
        );

        user.setReader(reader);

        userRepository.save(user);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        );

        Authentication authenticated = authenticationManager.authenticate(authentication);

        return jwtService.generateToken(authenticated);
    }

}
