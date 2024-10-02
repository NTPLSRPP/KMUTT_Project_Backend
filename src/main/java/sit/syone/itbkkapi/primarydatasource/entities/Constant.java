package sit.syone.itbkkapi.primarydatasource.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "numconstants")
@AllArgsConstructor
@NoArgsConstructor
public class Constant {
    @Id
    @Column(updatable = false, insertable = false)
    private String constName;
    @Column(updatable = false, insertable = false)
    private Integer value;
}
