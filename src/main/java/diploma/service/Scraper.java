package diploma.service;

import diploma.domain.football.FootballMatch;
import diploma.repo.FootballRepo;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Component
@Slf4j
public class Scraper {
    @Autowired
    FootballRepo footballRepo;

    private List<String> links = new ArrayList<>();
    private List<FootballMatch> matches = new CopyOnWriteArrayList<>();
    private ExecutorService executors = Executors.newFixedThreadPool(100);

    public List<FootballMatch> parse(String url) throws IOException {
        parseLinks(url);
        return parseData();
    }

    private void parseLinks(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        ParserLinks parserLinks = new ParserLinks();
        links = parserLinks.parse(doc);
        footballRepo.save(new FootballMatch());
    }

    private List<FootballMatch> parseData() {
        List<Future<List<FootballMatch>>> list = new ArrayList<>();
        links.forEach(link -> {
            Future future = executors.submit(new Parser(link));
            list.add(future);
        });
        list.forEach(footballMatchFuture -> {
            try {
                matches.addAll(footballMatchFuture.get());
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

    class Parser implements Callable<List<FootballMatch>> {
        private String url;

        public Parser(String url) {
            this.url = url;
        }

        @Override
        public List<FootballMatch> call() throws Exception {
            boolean repeat;
            ParserFootball parser = new ParserFootball(url);
            List<FootballMatch> list = null;
            do {
                try {
                    list = parser.parseData();
                    repeat = false;
                } catch (SocketTimeoutException e) {
                    repeat = true;
                }
            } while (repeat);
            return list;
        }
    }

}