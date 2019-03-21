package diploma.domain.tennis;
import diploma.domain.common.TotalCoef;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@Document(collection = "Tennis")
public class TennisMatch {
    @Id
    private String id;

    private LocalDateTime date;

    private String event;

    private String info;

    private WinCoef winCoef;

    private TotalCoef totalCoef;

    private Map<Result, Double> resultDoubleMap;
}
