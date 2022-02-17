package com.example.login.controllers;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.login.models.ERole;
import com.example.login.models.Provincia;
import com.example.login.models.Role;
import com.example.login.models.User;
import com.example.login.payload.request.LoginRequest;
import com.example.login.payload.request.SignupRequest;
import com.example.login.payload.response.MessageResponse;
import com.example.login.payload.response.UserInfoResponse;
import com.example.login.repository.RoleRepository;
import com.example.login.repository.UserRepository;
import com.example.login.security.jwt.JwtUtils;
import com.example.login.security.services.UserDetailsImpl;
import com.example.utils.Utils;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class AuthController {
	
	   private static final String URL_API_PROVINCIA_TEST =
	            "https://apis.datos.gob.ar/georef/api/provincias?nombre=Sgo. del Estero";
	   private static final String URL_API_PROVINCIA =
	            "https://apis.datos.gob.ar/georef/api/provincias";
	   
  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;
  
  @Autowired
  RestTemplate restTemplate;
  
  @PostMapping("/ingresar")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

    Authentication authentication = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

    ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

    List<String> roles = userDetails.getAuthorities().stream()
        .map(item -> item.getAuthority())
        .collect(Collectors.toList());

    return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
        .body(new UserInfoResponse(userDetails.getId(),
                                   userDetails.getUsername(),
                                   userDetails.getEmail()));
  }

  @PostMapping("/crear")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      return ResponseEntity.badRequest().body(new MessageResponse("Error: El Usuario ya existe!"));
    }

    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity.badRequest().body(new MessageResponse("Error: El Email ya existe!"));
    }

    // Create new user's account
    User user = new User(signUpRequest.getUsername(),
                         signUpRequest.getEmail(),
                         encoder.encode(signUpRequest.getPassword()));

    //Set<String> strRoles = signUpRequest.getRole();
    //Set<Role> roles = new HashSet<>();
    //user.setRoles(roles);
    
    userRepository.save(user);

    return ResponseEntity.ok(new MessageResponse("Usuario registrado exitosamente!"));
  }

  @PostMapping("/salir")
  public ResponseEntity<?> logoutUser() {
    ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
    return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
        .body(new MessageResponse("You've been signed out!"));
  }
  
  @GetMapping("/provincia-test")
  public Object getProvinciaTest(){
      Object prov = restTemplate.getForObject(URL_API_PROVINCIA_TEST,Object.class);
  	return prov;
  }
  
  @GetMapping("/provincia/{criterio}")
  public Object getProvincia( @PathVariable String criterio){
	  String criterion = "{criterio}";
	  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(URL_API_PROVINCIA).queryParam("nombre", criterio);
	  Object prov = restTemplate.getForObject(builder.build().toUri(),Object.class);
  	return prov;
  }
}
