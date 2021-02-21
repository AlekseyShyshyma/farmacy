package com.khpi.farmacy.controller;

import com.khpi.farmacy.config.security.annotations.CurrentUser;
import com.khpi.farmacy.config.security.*;
import com.khpi.farmacy.dtos.LoginEntityDto;
import com.khpi.farmacy.exception.AppException;
import com.khpi.farmacy.exception.ResourceNotFoundException;
import com.khpi.farmacy.model.Manager;
import com.khpi.farmacy.dtos.ManagerCodeAvailabilityDto;
import com.khpi.farmacy.dtos.ApiResponseDto;
import com.khpi.farmacy.model.Role;
import com.khpi.farmacy.repositories.ManagerRepository;

import com.khpi.farmacy.repositories.RoleRepository;
import com.khpi.farmacy.dtos.JwtAuthenticationResponseDto;
import com.khpi.farmacy.services.jwttoken.JwtTokenService;
import com.khpi.farmacy.config.security.userdetails.UserDetailsImpl;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api/manager")
public class ManagerController {

    @Setter(onMethod_ = @Autowired)
    ManagerRepository managerRepository;

    @Setter(onMethod_ = @Autowired)
    AuthenticationManager authenticationManager;

    @Setter(onMethod_ = @Autowired)
    RoleRepository roleRepository;

    @Setter(onMethod_ = @Autowired)
    PasswordEncoder passwordEncoder;

    @Setter(onMethod_ = @Autowired)
    JwtTokenService tokenProvider;


    @GetMapping("/get")
    @ResponseBody
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getManagerById(@RequestParam("managerCode") Long managerId) {
        Optional<Manager> manager = managerRepository.findById(managerId);
        return manager.map(response -> ResponseEntity.ok().body(response))
                .orElseThrow(() -> new ResourceNotFoundException("Manager", "id", managerId));
    }

    @ResponseBody
    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public Manager getCurrentUser(@CurrentUser UserDetailsImpl currentUser) {
        Manager userSummary = new Manager(currentUser.getManagerCode(), currentUser.getName(),
                currentUser.getPassword(), currentUser.getSurname(), currentUser.getPatronymic(),
                currentUser.getAddress(), currentUser.getPhoneNumber(), currentUser.getCorporatePhoneNumber(), currentUser.getPosition());
        return userSummary;
    }

    @GetMapping("/checkManagerCodeAvailability")
    @ResponseBody
    public ManagerCodeAvailabilityDto checkManagerCodeAvailability(@RequestParam(value = "managerCode") Long code) {
        Boolean isAvailable = !managerRepository.existsById(code);
        return new ManagerCodeAvailabilityDto(isAvailable);
    }

    @GetMapping("/all")
    @ResponseBody
    public List<Manager> getAllManagers() {
        return managerRepository.findAll();
    }


    @PostMapping("/new")
    @ResponseBody
    public Manager createManager(@Valid @RequestBody Manager manager) {
        String hashedPassword = passwordEncoder.encode(manager.getPassword());
        manager.setPassword(hashedPassword);
        return managerRepository.save(manager);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody Manager manager) {
        if(managerRepository.existsById(manager.getManagerCode())) {
            return new ResponseEntity(new ApiResponseDto(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        // Creating user's account
        Manager newManager = new Manager(manager.getManagerCode(), manager.getName(), manager.getPassword(),
                manager.getSurname(), manager.getPatronymic(), manager.getAddress(), manager.getPhoneNumber(),
                manager.getCorporatePhoneNumber(), manager.getPosition());

        newManager.setPassword(passwordEncoder.encode(manager.getPassword()));

        Role userRole = roleRepository.findByName(Roles.ROLE_USER)
                .orElseThrow(() -> new AppException("User Role not set."));

        newManager.setRoles(Collections.singletonList(userRole));

        Manager result = managerRepository.save(newManager);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/manager/{username}")
                .buildAndExpand(Long.toString(result.getManagerCode())).toUri();

        return ResponseEntity.created(location).body(new ApiResponseDto(true, "User registered successfully"));
    }

    @PostMapping("/signin")
    @ResponseBody
    public ResponseEntity<?> loginManager(@Valid @RequestBody LoginEntityDto loginEntityDto){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginEntityDto.getLogin().toString(),
                        loginEntityDto.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);

        Optional<Manager> manager = managerRepository.findById(loginEntityDto.getLogin());

        if(passwordEncoder.matches(loginEntityDto.getPassword(), manager.get().getPassword())) {
            return ResponseEntity.ok(new JwtAuthenticationResponseDto(jwt));
        } else {
            return new ResponseEntity<>("Unauthorised", HttpStatus.UNAUTHORIZED);
        }

    }

    @PutMapping("/update")
    @ResponseBody
    @PreAuthorize("hasRole('USER')")
    public Manager updateManagerById(@RequestParam("managerCode") Long managerId,
                                        @Valid @RequestBody Manager managerDetails) {

        Manager manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new ResourceNotFoundException("Manager", "id", managerId));

        manager.setAddress(managerDetails.getAddress());
        manager.setCorporatePhoneNumber(managerDetails.getCorporatePhoneNumber());

        manager.setName(managerDetails.getName());
        manager.setPatronymic(managerDetails.getPatronymic());
        manager.setSurname(managerDetails.getSurname());
        manager.setPosition(managerDetails.getPosition());
        manager.setPhoneNumber(managerDetails.getPhoneNumber());

        return managerRepository.save(manager);

    }

    @DeleteMapping("/delete")
    @ResponseBody
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteManager(@RequestParam("managerCode") Long managerId) {

        Manager manager = managerRepository.findById(managerId)
                            .orElseThrow(() -> new ResourceNotFoundException("Manager", "id", managerId));

        managerRepository.delete(manager);

        return ResponseEntity.ok().build();

    }
}
