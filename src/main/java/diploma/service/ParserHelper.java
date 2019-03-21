package diploma.service;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public interface ParserHelper<T> {
    public void parse(Document document) ;
}
