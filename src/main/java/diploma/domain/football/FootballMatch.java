package diploma.domain.football;

import lombok.Data;


@Data
public class FootballMatch {

    private long id;
    private String date;
    private String  event;
    private String region;
    private String league;
    private WinCoef winCoef;
    private WinOrDrawCoef winOrDrawCoef;
    private TotalCoef totalCoef;
    private TeamTotalCoef teamTotalCoef;


}
