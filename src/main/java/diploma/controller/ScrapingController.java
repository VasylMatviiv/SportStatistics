package diploma.controller;

import diploma.domain.football.FootballMatch;
import diploma.service.Scraper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class ScrapingController {
    @Autowired
    Scraper scraper;

    @GetMapping("/scrap")
    List<FootballMatch> scrap() throws IOException {
        String url = "https://www.parimatch.com/en/";
        return scraper.parse(url);
    }
}
