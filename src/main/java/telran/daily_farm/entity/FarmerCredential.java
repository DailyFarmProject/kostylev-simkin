package telran.daily_farm.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Setter
@Getter
@Table(name = "farmer_credentials")
public class FarmerCredential {
    @Id
    @GeneratedValue
    UUID id;

    @OneToOne
    @JoinColumn(name = "farmer_id", nullable = false)
    Farmer farmer;

    @Column(nullable = false)
    private String hashedPassword;

    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @Column(nullable = false)
    private boolean isVerificated;
    
    @Column(nullable = false)
    private LocalDateTime password_last_updated;
    
  
    private String refreshToken;
}
