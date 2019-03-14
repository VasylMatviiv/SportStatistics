package diploma.exception;

import java.io.IOException;

public class ParserException extends IOException {

    public String setAndReturnMessage(String url) {
        return "Error while parsing page : " + url;
    }
}
