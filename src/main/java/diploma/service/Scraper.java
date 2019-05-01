package diploma.service;

import diploma.domain.football.FootballMatch;
import diploma.repo.FootballRepo;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Scraper {

    private final FootballRepo footballRepo;

    private List<String> links = new ArrayList<>();
    private List<FootballMatch> matches = new CopyOnWriteArrayList<>();


    public List<FootballMatch> parse(String url) throws IOException {
        parseLinks(url);
        matches.clear();
        return parseData();
    }

    private void parseLinks(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        ParserLinks parserLinks = new ParserLinks();
        links = parserLinks.parseLinks(doc, ParserLinks.Sport.FOOTBALL);
    }

    private List<FootballMatch> parseData() {
        ExecutorService executors = Executors.newFixedThreadPool(100);
        List<Future<List<FootballMatch>>> list = new ArrayList<>();
        links.forEach(link -> {
            Future future = executors.submit(new ParserCallable(link));
            list.add(future);
        });
        list.forEach(matchFuture -> {
            try {
                matches.addAll(matchFuture.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });
        executors.shutdown();
        footballRepo.saveAll(matches);
        return matches;
    }

    class ParserCallable implements Callable<List<FootballMatch>> {
        private String url;

        public ParserCallable(String url) {
            this.url = url;
        }

        @Override
        public List<FootballMatch> call() throws Exception {
            boolean repeat;
            Parser parser = new Parser(url);
            List<FootballMatch> list = null;
            do {
                try {
                    list = parser.parseData();
                    repeat = false;
                } catch (IOException e) {
                    repeat = true;
                }
            } while (repeat);
            return list;
        }
    }

}