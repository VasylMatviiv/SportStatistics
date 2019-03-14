package diploma.domain.football;

import lombok.Data;

@Data
public class TeamTotalCoef {

    private double iTotalHome;
    private double iTotalAway;
    private double overHome;
    private double overAway;
    private double underHome;
    private double underAway;
}
