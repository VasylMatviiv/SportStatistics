package diploma.service;

import diploma.domain.football.FootballMatch;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class Scraper {
    private List<String> links = new ArrayList<>();
    private List<FootballMatch> matches = new ArrayList<>();

    public List<FootballMatch> parse(String url) throws IOException {
        parseLinks(url);
        return parseData();
    }

    private void parseLinks(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        ParserLinks parserLinks = new ParserLinks();
        links = parserLinks.parse(doc);
    }

    private List<FootballMatch> parseData() {
        links.forEach(link -> {
            ParserFootball parser = new ParserFootball(matches);
            try {
                parser.parseData(link);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        System.out.println(matches.size());
        return matches;
    }
}