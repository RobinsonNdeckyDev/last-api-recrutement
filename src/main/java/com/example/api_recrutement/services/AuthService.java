package com.example.api_recrutement.services;

      import com.example.api_recrutement.models.*;
      import com.example.api_recrutement.repository.AdminRepository;
      import com.example.api_recrutement.repository.CandidatRepository;
      import org.springframework.dao.DataIntegrityViolationException;
      import com.example.api_recrutement.repository.UserRepository;
      import com.example.api_recrutement.utils.JwtUtil;
      import jakarta.transaction.Transactional;
      import org.springframework.beans.factory.annotation.Autowired;
      import org.springframework.dao.DataIntegrityViolationException;
      import org.springframework.security.authentication.AuthenticationManager;
      import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
      import org.springframework.security.core.userdetails.UserDetails;
      import org.springframework.security.core.userdetails.UserDetailsService;
      import org.springframework.security.crypto.password.PasswordEncoder;
      import org.springframework.stereotype.Service;
      import org.springframework.web.bind.annotation.PostMapping;

@Service
      public class AuthService {
          public final AuthenticationManager authenticationManager;
          public final UserDetailsService userDetailsService;
          public final UserRepository userRepository;
          public final AdminRepository adminRepository;
          public final CandidatRepository candidatRepository;
          public final PasswordEncoder passwordEncoder;
          public final JwtUtil jwtUtil;

          @Autowired
          public AuthService(
                  AuthenticationManager authenticationManager,
                  UserDetailsService userDetailsService,
                  UserRepository userRepository,
                  PasswordEncoder passwordEncoder,
                  AdminRepository adminRepository,
                  CandidatRepository candidatRepository,
                  JwtUtil jwtUtil
          ) {
              this.authenticationManager = authenticationManager;
              this.userDetailsService = userDetailsService;
              this.userRepository = userRepository;
              this.passwordEncoder = passwordEncoder;
              this.adminRepository = adminRepository;
              this.candidatRepository = candidatRepository;
              this.jwtUtil = jwtUtil;
          }

          @PostMapping(value = "/login", produces = "application/json")
          public AuthResponse login(AuthRequest authRequest) {
              authenticationManager.authenticate(
                      new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
              );

              final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getEmail());
              final String jwt = jwtUtil.generateToken(userDetails);

              // Recuperer l'utilisateur connecte
              User user = userRepository.findByEmail(authRequest.getEmail())
                      .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

              // Verifier si l'utilisateur est un Admin
              Admin admin = adminRepository.findByEmail(user.getEmail()).orElse(null);
              if (admin != null) {
                  AuthResponse.UserInfo userInfo = new AuthResponse.UserInfo(
                          admin.getPrenom(),
                          admin.getNom(),
                          admin.getEmail(),
                          user.getRole().name(),
                          admin.getTelephone(),
                          admin.getDescription(),
                          admin.getAdresse(),
                          admin.getPhotoProfil()
                  );
                  return new AuthResponse(jwt, userInfo);
              }

              // Vérifier si l'utilisateur est un Candidat
              Candidat candidat = candidatRepository.findByEmail(user.getEmail()).orElse(null);
              if (candidat != null) {
                  AuthResponse.UserInfo userInfo = new AuthResponse.UserInfo(
                          candidat.getPrenom(),
                          candidat.getNom(),
                          candidat.getEmail(),
                          user.getRole().name(),
                          candidat.getTelephone(),
                          candidat.getDescription(),
                          candidat.getAdresse(),
                          candidat.getPhotoProfil()
                  );
                  return new AuthResponse(jwt, userInfo);
              }

              // Si aucun Admin ou Candidat n'est trouvé, renvoyer des valeurs par defaut
              AuthResponse.UserInfo userInfo = new AuthResponse.UserInfo(
                      user.getPrenom(),
                      user.getNom(),
                      user.getEmail(),
                      user.getRole().name(),
                      user.getTelephone(),
                      user.getDescription(),
                      user.getAdresse(),
                      user.getPhotoProfil()
              );

              return new AuthResponse(jwt, userInfo);

          }

          @Transactional
          public AuthResponse register(RegisterRequest request) {
              if (userRepository.findByEmail(request.getEmail()).isPresent()) {
                  throw new RuntimeException("L'utilisateur existe déjà");
              }

              try {
                  String encodedPassword = passwordEncoder.encode(request.getPassword());
                  User user = new User();
                  user.setPhotoProfil(request.getPhotoProfil());
                  user.setDescription(request.getDescription());
                  user.setEmail(request.getEmail());
                  user.setPassword(encodedPassword);
                  user.setRole(request.getRole());
                  user.setNom(request.getNom());
                  user.setPrenom(request.getPrenom());
                  user.setTelephone(request.getTelephone());
                  user.setAdresse(request.getAdresse());
                  userRepository.save(user);

                  if (Role.ADMIN.equals(request.getRole())) {
                      Admin admin = new Admin();
                      admin.setUser(user);
                      admin.setPrenom(request.getPrenom());
                      admin.setNom(request.getNom());
                      admin.setEmail(request.getEmail());
                      admin.setPassword(encodedPassword);
                      admin.setPhotoProfil(request.getPhotoProfil());
                      admin.setAdresse(request.getAdresse());
                      admin.setDescription(request.getDescription());
                      admin.setTelephone(request.getTelephone());
                      adminRepository.save(admin);
                  } else if (Role.CANDIDAT.equals(request.getRole())) {
                      Candidat candidat = getCandidat(request, user, encodedPassword);
                      candidatRepository.save(candidat);
                  }

                  final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
                  final String jwt = jwtUtil.generateToken(userDetails);

                  AuthResponse.UserInfo userInfo = new AuthResponse.UserInfo(
                          request.getPrenom(),
                          request.getNom(),
                          request.getEmail(),
                          request.getRole().name(),
                          request.getTelephone(),
                          request.getAdresse(),
                          request.getDescription(),
                          request.getPhotoProfil()
                  );
                  return new AuthResponse(jwt, userInfo);
              } catch (DataIntegrityViolationException e) {
                  throw new RuntimeException("L'utilisateur avec cet email existe déjà.");
              } catch (Exception e) {
                  throw new RuntimeException("Erreur lors de l'enregistrement de l'utilisateur: " + e.getMessage());
              }
          }
          private static Candidat getCandidat(RegisterRequest request, User user, String encodedPassword) {
              Candidat candidat = new Candidat();
              candidat.setUser(user);
              candidat.setPrenom(request.getPrenom());
              candidat.setNom(request.getNom());
              candidat.setEmail(request.getEmail());
              candidat.setPassword(encodedPassword);
              candidat.setAdresse(request.getAdresse());
              candidat.setTelephone(request.getTelephone());
              candidat.setDescription(request.getDescription());
              candidat.setPhotoProfil(request.getPhotoProfil());
              return candidat;
          }

      }