import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class RandomWords {
    public static String getWord() throws IOException {
        String topNum = String.valueOf(randInt(1,1000));
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://most-common-words.herokuapp.com/api/search?top="+topNum))
                .timeout(Duration.ofSeconds(10))
                .build();
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply((x) -> {
                    try {
                        return RandomWords.parseJSON(x);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return x;
                })
                .join()
                .toUpperCase();
    }

    private static int randInt(int start, int stop) {
        if (start < 0 ) {
            return (int) Math.round(Math.random() * (Math.abs(start - stop)) + start);
        }
        return (int) Math.round(Math.random() * stop + start);
    }

    private static String parseJSON (String responseBody) throws ParseException {
        JSONParser parse = new JSONParser();
        JSONObject words = (JSONObject) parse.parse(responseBody);
        return words.get("word").toString();
    }
}
