import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class hangman_game {
    public static Set<String> wordLetters = new HashSet<>();
    public static Set<String> correctLetters = new HashSet<>();
    public static Set<String> wrongLetters = new HashSet<>();
    public static Scanner userInput = new Scanner(System.in);
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_BOLD ="\u001B[1m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_ITALIC_INTENSE_WHITE = "\u001B[3;97m";
    public static int oldSetSize = 0;


    public static void main (String[] args) {
        boolean play;
        boolean gameOver;

        try {
            do {
                String word = randWordApi().toUpperCase();
                do {
                    draw(word);
                    gameOver = guessLetter(word);
                } while(!gameOver);
                play=playAgain();
                wrongLetters.clear();
                correctLetters.clear();
                wordLetters.clear();
            } while (play);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static boolean playAgain(){
        String ans ="";
        while (!ans.equalsIgnoreCase("y") && !ans.equalsIgnoreCase("n")) {
            try {
                System.out.println(ANSI_ITALIC_INTENSE_WHITE+"Would you like to play again? "+ANSI_BOLD+"(Y or N)");
                ans = userInput.next();
            } catch (Exception e){
                System.err.println("Input Error");
                userInput.nextLine();
            }
        }
        return ans.equalsIgnoreCase("y");
    }
    public static String randWordApi() throws IOException {
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
                        return hangman_game.parseJSON(x);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return x;
                })
                .join();
    }
    public static String parseJSON (String responseBody) throws ParseException {
            JSONParser parse = new JSONParser();
            JSONObject words = (JSONObject) parse.parse(responseBody);
        return words.get("word").toString();
    }
    public static boolean guessLetter(String word) {

        try {
            System.out.println("Guess a Letter: ");
            String guess = userInput.next().toUpperCase();
            if (wrongLetters.contains(guess) || correctLetters.contains(guess)){
                System.out.printf(ANSI_BOLD+"You've guessed '%s' already!\n"+ANSI_RESET,guess);
            } else {
                for (char letter : word.toCharArray()) {
                    if (String.valueOf(letter).equalsIgnoreCase(guess)) {
                        correctLetters.add(guess);
                    } else {
                        wrongLetters.add(guess);
                    }
                    wordLetters.add(Character.toString(letter));
                }
                wrongLetters.removeAll(correctLetters);
            }
        } catch (Exception e) {
            e.printStackTrace();
            userInput.nextLine();
        }
        if (correctLetters.size() == wordLetters.size()){
            draw(word);
            System.out.print(ANSI_BOLD+ANSI_GREEN+"You Win! "+ANSI_RESET);
            System.out.printf("The word is %s.\n",word);
        } else if (wrongLetters.size() == 6) {
            draw(word);
            System.out.println(ANSI_BOLD+ANSI_RED+"You Lose."+ANSI_RESET);
        }
        return correctLetters.size() == wordLetters.size() || wrongLetters.size() == 6;
    }
    public static void badGuesses(){
        System.out.print("Wrong Guesses: ");
        for (String letter : wrongLetters){
            System.out.print(letter);
        }
        System.out.println();
    }
    public static void draw(String word){
        System.out.println(ANSI_BOLD+ANSI_ITALIC_INTENSE_WHITE+"\nH A N G M A N"+ANSI_RESET);
        drawHangman(wrongLetters == null ? 0 : wrongLetters.size());
        drawBlanks(word);
        badGuesses();
    }
    public static void drawHangman(int numWrongLetters){
        String strColor;
        if (oldSetSize == wrongLetters.size()) {
            strColor = ANSI_RESET;
        } else {
            strColor = ANSI_RED;
            oldSetSize = wrongLetters.size();
        }
        String[][] hangmanStrArr= {
                {" +----+", "      |", "      |", "      |", "      |", "      |", "    ====="},
                {" +----+", strColor+" O "+ANSI_RESET+"   |", "      |", "      |", "      |", "      |", "    ====="},
                {" +----+", " O    |", strColor+" | "+ANSI_RESET+"   |", strColor+" | "+ANSI_RESET+"   |", "      |", "      |", "    ====="},
                {" +----+", " O    |", strColor+"\\"+ANSI_RESET+"|    |", " |    |", "      |", "      |", "    ====="},
                {" +----+", " O    |", "\\|"+strColor+"/"+ANSI_RESET+"   |", " |    |", "      |", "      |", "    ====="},
                {" +----+", " O    |", "\\|/   |", " |    |", strColor+"/"+ANSI_RESET+"     |", "      |", "    ====="},
                {" +----+", strColor+" O "+ANSI_RESET+"   |", strColor+"\\|/"+ANSI_RESET+"   |", strColor+" |"+ANSI_RESET+"    |", strColor+"/ \\"+ANSI_RESET+"   |", "      |", "    ====="}
        };

        for (String message : hangmanStrArr[numWrongLetters]) {
            System.out.println(message);
        }

    }
    public static void drawBlanks(String word) {
        System.out.print(" ");
        char[] letters = word.toCharArray();
        for (Character letter : letters) {
            if (correctLetters.contains(String.valueOf(letter))){
                System.out.print(letter);
            } else {
                System.out.print("_");
            }
            System.out.print(" ");
        }
        System.out.println();
    }
    public static int randInt(int start, int stop) {
        if (start < 0 ) {
            return (int) Math.round(Math.random() * (Math.abs(start - stop)) + start);
        }
        return (int) Math.round(Math.random() * stop + start);
    }

}
