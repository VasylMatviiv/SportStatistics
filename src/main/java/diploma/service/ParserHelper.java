package diploma.service;

import diploma.domain.football.FootballMatch;
import diploma.domain.football.TotalCoef;
import diploma.domain.football.WinCoef;
import diploma.domain.football.WinOrDrawCoef;
import diploma.exception.ParserException;
import diploma.repo.FootballRepo;
import diploma.utils.ParserUtils;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.StringTokenizer;

@Slf4j
public class ParserHelper {
    @Autowired
    FootballRepo repo;
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
            System.out.println("Exception: region =" + region + " or league " + league);
        }
    }

    private LocalDateTime parseDate(Element element) {
        StringBuilder time = new StringBuilder(element.getAllElements().get(3).text());
        return ParserUtils.parseDate(time);
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
