package sit.syone.itbkkapi.primarydatasource.entities;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "boards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Board {
    @Id
    @Column(name = "boardID", length = 10)
    private String boardID;
    @Column(name = "boardName", nullable = false, length = 45)
    private String boardName;
    @Column(name = "ownerID", nullable = false, length = 36)
    private String ownerID;
    @Column(name = "isPublic", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean isPublic;

    @ManyToOne
    @JoinColumn(name = "ownerID", insertable = false, updatable = false, referencedColumnName = "userID", nullable = false)
    private PrimaryUser owner;

    @PrePersist
    public void generateBoardID() {
        if (this.boardID == null || this.boardID.isEmpty()) {
            this.boardID = NanoIdUtils.randomNanoId(NanoIdUtils.DEFAULT_NUMBER_GENERATOR, NanoIdUtils.DEFAULT_ALPHABET, 10);
        }
    }
}
