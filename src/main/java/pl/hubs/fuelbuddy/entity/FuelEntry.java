package pl.hubs.fuelbuddy.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "fuel_entries")
public class FuelEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    private Integer mileage;

    private Double fuelVolume;

    private Double pricePerUnit;

    private Double discount;

    private String fuelType;

    @Override
    public String toString() {
        return "FuelEntry{" +
                "id=" + id +
                ", date=" + date +
                ", mileage=" + mileage +
                ", fuelVolume=" + fuelVolume +
                ", pricePerUnit=" + pricePerUnit +
                ", discount=" + discount +
                ", fuelType='" + fuelType + '\'' +
                ", gasStation='" + gasStation + '\'' +
                ", notes='" + notes + '\'' +
                ", vehicle=" + (vehicle != null ? vehicle.getId() : null) +
                '}';
    }

    private String gasStation;

    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;
}
