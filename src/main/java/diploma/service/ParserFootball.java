package diploma.service;

import diploma.domain.football.FootballMatch;
import diploma.exception.ParserException;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Slf4j
public class ParserFootball {

    final private List<FootballMatch> matches = new ArrayList<>();
    final private String url;

    public ParserFootball(String url) {
        this.url = url;
    }

    public List<FootballMatch> parseData() throws IOException {
        try {
            Document doc = Jsoup.connect(url).get();
            ParserHelper parserHelper = new ParserHelper(matches);
            parserHelper.parse(doc);
            return matches;
        } catch (ParserException e) {
            log.error(e.setAndReturnMessage(url));
        } catch (HttpStatusException e) {
            log.error(e.getUrl());
        }
        return matches;
    }


}
