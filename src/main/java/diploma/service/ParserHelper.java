package diploma.service;

import diploma.domain.football.*;
import diploma.exception.ParserException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class ParserHelper {
    private String region;
    private String league;
    final private List<FootballMatch> matches = new ArrayList<>();

    public List<FootballMatch> parse(Document document) throws ParserException {

        try {
            Elements row1 = document.select(".row1:not(.props)");
            Elements row2 = document.select(".row2:not(.props)");
            row1.addAll(row2);
            parseTitle(document.title());
            parseRows(row1);
            return matches;
        } catch (Exception e) {
            throw new ParserException();
        }
    }

    private void parseRows(Elements rows) {
        rows.forEach(element -> parseRow(element));
    }

    private void parseRow(Element element) {
        FootballMatch match = new FootballMatch();

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
    }

    private void parseTitle(String title) {
        StringTokenizer tokenizer = new StringTokenizer(title, ".");
        tokenizer.nextToken();
        region = tokenizer.nextToken();
        league = tokenizer.nextToken();
    }

    private String parseDate(Element element) {
        return element.getAllElements().get(3).text();
    }

    private String parseEvent(Element element) {
        return element.getAllElements().get(5).text();
    }

    private TotalCoef parseTotalCoef(Element element) {
        TotalCoef totalCoef = new TotalCoef();
        totalCoef.setTotal(convertDouble(element.getAllElements().get(16).text()));
        totalCoef.setOver(convertDouble(element.getAllElements().get(18).text()));
        totalCoef.setUnder(convertDouble(element.getAllElements().get(21).text()));
        return totalCoef;
    }

    private WinCoef parseWinCoef(Element element) {
        WinCoef winCoef = new WinCoef();
        winCoef.setWin1(convertDouble(element.getAllElements().get(25).text()));
        winCoef.setDraw(convertDouble(element.getAllElements().get(29).text()));
        winCoef.setWin2(convertDouble(element.getAllElements().get(31).text()));
        return winCoef;
    }

    private WinOrDrawCoef parseWinOrDrawCoef(Element element) {
        WinOrDrawCoef winOrDrawCoef = new WinOrDrawCoef();
        winOrDrawCoef.setX1(convertDouble(element.getAllElements().get(33).text()));
        winOrDrawCoef.setWithoutDraw(convertDouble(element.getAllElements().get(37).text()));
        winOrDrawCoef.setX2(convertDouble(element.getAllElements().get(39).text()));
        return winOrDrawCoef;
    }

    private TeamTotalCoef parseTeamTotalCoef(Element element) {
        TeamTotalCoef teamTotalCoef = new TeamTotalCoef();

        teamTotalCoef.setITotalHome(convertDouble(getFirstNumber(element.getAllElements().get(42).getAllElements().get(0), "b")));
        teamTotalCoef.setITotalAway(convertDouble(getSecondNumber(element.getAllElements().get(42).getAllElements().get(0), "b")));
        teamTotalCoef.setOverHome(convertDouble(getFirstNumber(element.getAllElements().get(45).getAllElements().get(0), "a")));
        teamTotalCoef.setOverAway(convertDouble(getSecondNumber(element.getAllElements().get(45).getAllElements().get(0), "a")));
        teamTotalCoef.setUnderHome(convertDouble(getFirstNumber(element.getAllElements().get(50).getAllElements().get(0), "u")));
        teamTotalCoef.setUnderAway(convertDouble(getSecondNumber(element.getAllElements().get(50).getAllElements().get(0), "u")));
        return teamTotalCoef;
    }

    private String getFirstNumber(Element element, String tag) {
        Elements els = element.getAllElements().select(tag);
        return els.get(0).text();
    }

    private String getSecondNumber(Element element, String tag) {
        Elements els = element.getAllElements().select(tag);
        return els.get(1).text();
    }

    private double convertDouble(String number) {
        if (number.isEmpty()) {
            return 0;
        } else {
            return Double.valueOf(number);
        }
    }
}
