package diploma.service;

import diploma.domain.tennis.TennisMatch;
import org.jsoup.nodes.Document;

import java.util.List;

public class ParserTennisHelper implements ParserHelper<TennisMatch> {
    private List<TennisMatch> matches;

    public ParserTennisHelper(List<TennisMatch> matches) {
        this.matches = matches;
    }

    @Override
    public void parse(Document document) {

    }
}
