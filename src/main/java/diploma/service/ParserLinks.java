package diploma.service;


import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ParserLinks {
    private List<String> links = new ArrayList<String>();

    List<String> parse(Document document) {
        Element element = document.select(".hidden.groups").first();
        String url = element.baseUri();
        Elements elements=element.getAllElements().select("a");
        String finalUrl = url.substring(0, url.length() - 4);

        elements.forEach(
                el -> links.add(finalUrl + el.attr("href"))
        );

        return links.stream().distinct().collect(Collectors.toList());
    }


}
