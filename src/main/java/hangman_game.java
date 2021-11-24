import java.util.Scanner;

public class hangman_game {
    public static Scanner userInput = new Scanner(System.in);
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_BOLD ="\u001B[1m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_ITALIC_INTENSE_WHITE = "\u001B[3;97m";
    public static GuessingLetters game = new GuessingLetters();
    public static Draw drawing = new Draw();
    public static int oldSetSize = 0;


    public static void main (String[] args) {
        boolean play;
        boolean gameOver;

        try {
            do {
                String word = RandomWords.getWord();
                game.setWord(word);
                do {
                    drawHangman();
                    gameOver = guessLetters();
                } while(!gameOver);
                play=playAgain();
                game.reset();
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
    public static boolean guessLetters() {
        try {
            System.out.println("Guess a Letter: ");
            game.setGuess(userInput.next());
            if (game.alreadyGuessed()){
                System.out.printf(ANSI_BOLD+"You've guessed '%s' already!\n"+ANSI_RESET,game.getGuess());
            } else {
                game.checkLetters();
            }
        } catch (Exception e) {
            e.printStackTrace();
            userInput.nextLine();
        }
        if (game.win()){
            drawHangman();
            System.out.print(ANSI_BOLD+ANSI_GREEN+"You Win! "+ANSI_RESET);
            System.out.printf("The word is %s.\n",game.getWord());
        } else if (game.lose()) {
            drawHangman();
            System.out.println(ANSI_BOLD+ANSI_RED+"You Lose."+ANSI_RESET);
        }
        return game.finished();
    }
    public static void drawHangman(){
        System.out.println(ANSI_BOLD+ANSI_ITALIC_INTENSE_WHITE+"\nH A N G M A N"+ANSI_RESET);
        String strColor;
        if (oldSetSize == game.getWrongLetters().size()) {
            strColor = ANSI_RESET;
        } else {
            strColor = ANSI_RED;
            oldSetSize = game.getWrongLetters().size();
        }
        drawing.hangman(game.getWrongLetters().size(),strColor);
        drawing.blanks(game.getWord(),game.getCorrectLetters(),game.getWrongLetters());
    }



}
