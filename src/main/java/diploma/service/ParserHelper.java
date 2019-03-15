package diploma.service;

import diploma.domain.football.*;
import diploma.exception.ParserException;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;
import java.util.StringTokenizer;

@Slf4j
public class ParserHelper {
    private String region;
    private String league;
    final private List<FootballMatch> matches;

    public ParserHelper(List<FootballMatch> matches) {
        this.matches = matches;
    }

    public void parse(Document document) throws ParserException {
        Elements row1 = document.select(".row1:not(.props)");
        Elements row2 = document.select(".row2:not(.props)");
        row1.addAll(row2);
        parseTitle(document.title());
        parseRows(row1);
    }

    private void parseRows(Elements rows) {
        rows.forEach(this::parseRow);
    }

    private void parseRow(Element element) {
        FootballMatch match = new FootballMatch();
        try {
            Element tr = element.select("tr").get(0);
            match.setRegion(region);
            match.setLeague(league);
            match.setDate(parseDate(tr));
            match.setEvent(parseEvent(tr));
            match.setTotalCoef(parseTotalCoef(tr));
            match.setWinCoef(parseWinCoef(tr));
            match.setWinOrDrawCoef(parseWinOrDrawCoef(tr));
            match.setTeamTotalCoef(parseTeamTotalCoef(tr));
            matches.add(match);
        } catch (Exception e) {
            log.error(new ParserException().setAndReturnMessage(element.baseUri()));
        }
    }

    private void parseTitle(String title) {
        try {
            StringTokenizer tokenizer = new StringTokenizer(title, ".");
            tokenizer.nextToken();
            region = tokenizer.nextToken();
            league = tokenizer.nextToken();
        } catch (Exception e) {
            System.out.println("Exceprion: region =" + region + " or league " + league);
        }
    }

    private String parseDate(Element element) {
        return element.getAllElements().get(3).text();
    }

    private String parseEvent(Element element) {
        return element.getAllElements().get(5).text();
    }

    private TotalCoef parseTotalCoef(Element element) {
        TotalCoef totalCoef = new TotalCoef();
        totalCoef.setTotal(convertDouble(getCoef(element, 16)));
        totalCoef.setOver(convertDouble(getCoef(element, 18)));
        totalCoef.setUnder(convertDouble(getCoef(element, 21)));
        return totalCoef;
    }

    private WinCoef parseWinCoef(Element element) {
        WinCoef winCoef = new WinCoef();
        winCoef.setWin1(convertDouble(getCoef(element, 25)));
        winCoef.setDraw(convertDouble(getCoef(element, 29)));
        winCoef.setWin2(convertDouble(getCoef(element, 31)));
        return winCoef;
    }

    private WinOrDrawCoef parseWinOrDrawCoef(Element element) {
        WinOrDrawCoef winOrDrawCoef = new WinOrDrawCoef();
        winOrDrawCoef.setX1(convertDouble(getCoef(element, 33)));
        winOrDrawCoef.setWithoutDraw(convertDouble(getCoef(element, 37)));
        winOrDrawCoef.setX2(convertDouble(getCoef(element, 39)));
        return winOrDrawCoef;
    }

    private TeamTotalCoef parseTeamTotalCoef(Element element) {
        TeamTotalCoef teamTotalCoef = new TeamTotalCoef();

        teamTotalCoef.setITotalHome(convertDouble(getFirstNumber(element, "b", 42)));
        teamTotalCoef.setITotalAway(convertDouble(getSecondNumber(element, "b", 42)));
        teamTotalCoef.setOverHome(convertDouble(getFirstNumber(element, "a", 45)));
        teamTotalCoef.setOverAway(convertDouble(getSecondNumber(element, "a", 45)));
        teamTotalCoef.setUnderHome(convertDouble(getFirstNumber(element, "a", 50)));
        teamTotalCoef.setUnderAway(convertDouble(getSecondNumber(element, "a", 50)));
        return teamTotalCoef;
    }

    private String getCoef(Element element, int id) {
        String result;
        Element el = element.getAllElements().get(id);
        if (el.getAllElements().size() < 1) {
            result = "0";
        } else {
            result = el.text();
        }
        return result;
    }

    private String getFirstNumber(Element element, String tag, int id) {
        String result ;
            Element element1 = element.getAllElements().get(id).getAllElements().get(0);
            Elements els = element1.getAllElements().select(tag);
            if (els.size() <= 1) {
                result = "0";
            } else {
                result = els.get(0).text();
            }
        return result;
    }

    private String getSecondNumber(Element element, String tag, int id) {
        String result ;
            Element element1 = element.getAllElements().get(id).getAllElements().get(0);
            Elements els = element1.getAllElements().select(tag);
            if (els.size() <= 1) {
                result = "0";
            } else {
                result = els.get(1).text();
            }
        return result;
    }


    private double convertDouble(String number) {
        double result;
        try {
            result = Double.valueOf(number);
        } catch (NumberFormatException e) {
            result = 0;
        }
        return result;
    }
}
