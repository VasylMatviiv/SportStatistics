package diploma.service;

import diploma.domain.common.TotalCoef;
import diploma.domain.football.FootballMatch;
import diploma.domain.football.WinCoef;
import diploma.domain.football.WinOrDrawCoef;
import diploma.exception.ParserException;
import diploma.utils.ParserUtils;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
public class ParserFootballHelper implements ParserHelper<FootballMatch>{
    final private List<FootballMatch> matches;
    private  String title;

    public ParserFootballHelper(List<FootballMatch> matches) {
        this.matches = matches;
    }

    public void parse(Document document) {
        Elements row1 = document.select(".row1:not(.props)");
        Elements row2 = document.select(".row2:not(.props)");
        row1.addAll(row2);
        title = parseTitle(document.title());
        parseRows(row1);
    }

    private void parseRows(Elements rows) {
        rows.forEach(this::parseRow);
    }

    private void parseRow(Element element) {

        FootballMatch match = new FootballMatch();
        try {
            Element tr = element.select("tr").get(0);
            match.setTitle(parseTitle(title));
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


    private String parseTitle(String title) {
        return ParserUtils.parseTitle(title);
    }

    private LocalDateTime parseDate(Element element) {
        StringBuilder time = new StringBuilder(element.getAllElements().get(3).text());
        return ParserUtils.parseDate(time);
    }

    private String parseEvent(Element element) {
        return element.getAllElements().get(5).getAllElements().select("a").text();
    }

    private TotalCoef parseTotalCoef(Element element) {
        TotalCoef totalCoef = new TotalCoef();
        totalCoef.setTotal(ParserUtils.convertDouble(getCoef(element, 16)));
        totalCoef.setOver(ParserUtils.convertDouble(getCoef(element, 18)));
        totalCoef.setUnder(ParserUtils.convertDouble(getCoef(element, 21)));
        return totalCoef;
    }

    private WinCoef parseWinCoef(Element element) {
        WinCoef winCoef = new WinCoef();
        winCoef.setWin1(ParserUtils.convertDouble(getCoef(element, 25)));
        winCoef.setDraw(ParserUtils.convertDouble(getCoef(element, 29)));
        winCoef.setWin2(ParserUtils.convertDouble(getCoef(element, 31)));
        return winCoef;
    }

    private WinOrDrawCoef parseWinOrDrawCoef(Element element) {
        WinOrDrawCoef winOrDrawCoef = new WinOrDrawCoef();
        winOrDrawCoef.setX1(ParserUtils.convertDouble(getCoef(element, 33)));
        winOrDrawCoef.setWithoutDraw(ParserUtils.convertDouble(getCoef(element, 37)));
        winOrDrawCoef.setX2(ParserUtils.convertDouble(getCoef(element, 39)));
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
}
