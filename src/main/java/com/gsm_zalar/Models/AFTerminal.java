package com.gsm_zalar.Models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Optional;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@DiscriminatorValue("AFTerminal")  // Utilisation de @DiscriminatorValue
public class AFTerminal extends Affectation {

    @ManyToOne
    @JoinColumn(name = "terminal_id")
    private Terminal terminal;

    private int duree_max = 3;



    public long getDureeMax() {
        return duree_max;
    }

    public void setTerminal(Optional<Terminal> byId) {
        terminal=byId.get();
    }

    public void setDureeMax(int duree) {
        this.duree_max=duree;
    }


}
