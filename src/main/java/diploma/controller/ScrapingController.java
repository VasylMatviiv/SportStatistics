package diploma.controller;

import diploma.domain.football.FootballMatch;
import diploma.repo.FootballRepo;
import diploma.service.Scraper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ScrapingController {
    private final Scraper scraper;
    private final FootballRepo footballRepo;

    @GetMapping("/scrap")
    List<FootballMatch> scrap() throws IOException {
        String url = "https://www.parimatch.com/en/";
        footballRepo.deleteAll();
        List<FootballMatch> matches = scraper.parse(url);
        return matches;
    }
}
