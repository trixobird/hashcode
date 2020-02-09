package pizza.model;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.Collection;

@NoArgsConstructor
@Data
@Accessors(chain = true)
public class Pizza {
    private int maxSlices;
    private Collection<Integer> pizzasSlices;
}
