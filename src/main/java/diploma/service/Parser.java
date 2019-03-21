package diploma.service;

import diploma.domain.football.FootballMatch;
import diploma.domain.tennis.TennisMatch;
import diploma.exception.ParserException;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Slf4j
public class Parser {

    final private List<FootballMatch> matchesFootball = new ArrayList<>();
    final private List<TennisMatch> matchesTennis = new ArrayList<>();
    final private String url;

    public Parser(String url) {
        this.url = url;

    }

    public List<FootballMatch> parseData() throws IOException {
        try {
            Document doc = Jsoup.connect(url).get();

            ParserFootballHelper parser =new ParserFootballHelper(matchesFootball);
            parser.parse(doc);
        } catch (ParserException e) {
            log.error(e.setAndReturnMessage(url));
        } catch (HttpStatusException e) {
            log.error(e.getUrl());
        }
        return matchesFootball;
    }
}
