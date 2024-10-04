package authentiation.authToken.entity;

import jakarta.persistence.*;
import lombok.Data;
//import org.springframework.data.annotation.Id;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
}
