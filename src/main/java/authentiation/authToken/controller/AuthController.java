package authentiation.authToken.controller;

import authentiation.authToken.entity.User;
import authentiation.authToken.repository.UserRepository;
import authentiation.authToken.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@RestController
//@RequestMapping("/auth")
//public class AuthController {
//
//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private JwtUtil jwtUtil;
//
//    @Autowired
//    private UserDetailsService userDetailsService;
//
//    @PostMapping("/register")
//    public ResponseEntity<String> register(@RequestBody User user) {
//        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
//        userRepository.save(user);
//        return ResponseEntity.ok("User registered successfully");
//    }
//
////    @PostMapping("/authenticate")
////    public ResponseEntity<String> authenticate(@RequestBody User user) {
////        try {
////            authenticationManager.authenticate(
////                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
////            );
////        } catch (Exception e) {
////            return ResponseEntity.status(401).body("Invalid credentials");
////        }
////
////        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
////        String jwt = jwtUtil.generateToken(userDetails.getUsername());
////
////        return ResponseEntity.ok(jwt);
////    }
//@PostMapping("/authenticate")
//public ResponseEntity<String> authenticate(@RequestBody User user) {
//    try {
//        // This will automatically compare raw and encoded passwords using the encoder
//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
//        );
//    } catch (Exception e) {
//        return ResponseEntity.status(401).body("Invalid credentials");
//    }
//
//    UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
//    String jwt = jwtUtil.generateToken(userDetails.getUsername());
//
//    return ResponseEntity.ok(jwt);
//}
//
//}


@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    // Inject BCryptPasswordEncoder
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Use the injected encoder
        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate(@RequestBody User user) {
        // Retrieve the user from the database
        User existingUser = userRepository.findByUsername(user.getUsername()).orElse(null);

        // Check if user exists and validate password
        if (existingUser != null && passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
            // If the password matches, generate JWT token
            String jwt = jwtUtil.generateToken(existingUser.getUsername());
            return ResponseEntity.ok(jwt);
        } else {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }
}
