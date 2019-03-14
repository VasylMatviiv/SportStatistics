package diploma.service;

import diploma.domain.football.FootballMatch;
import diploma.exception.ParserException;
import lombok.extern.java.Log;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Log
public class ParserFootball {

    final private List<FootballMatch> matches = new ArrayList<>();

    public List<FootballMatch> parseData(String url) throws IOException {
        try {
            Document doc = Jsoup.connect(url).get();
            ParserHelper parserHelper = new ParserHelper();
            matches.addAll(parserHelper.parse(doc));
        } catch (ParserException e) {
            log.info(e.setAndReturnMessage(url));
        }
        return matches;
    }
}
