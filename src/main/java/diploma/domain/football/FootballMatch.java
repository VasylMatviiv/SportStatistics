package diploma.domain.football;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@Document(collection = "Football")
public class FootballMatch {
    @Id
    private String id;

    private LocalDateTime date;

    private String event;

    private String region;

    private String league;

    private WinCoef winCoef;

    private WinOrDrawCoef winOrDrawCoef;

    private TotalCoef totalCoef;
}
