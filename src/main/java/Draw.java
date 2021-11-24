import java.util.Set;

public class Draw {
    public static final String ANSI_RESET = "\u001B[0m";
    public void hangman(int numberOfWrongLetters,String strColor){

        String[][] hangmanStrArr= {
                {" +----+", "      |", "      |", "      |", "      |", "      |", "    ====="},
                {" +----+", strColor+" O "+ANSI_RESET+"   |", "      |", "      |", "      |", "      |", "    ====="},
                {" +----+", " O    |", strColor+" | "+ANSI_RESET+"   |", strColor+" | "+ANSI_RESET+"   |", "      |", "      |", "    ====="},
                {" +----+", " O    |", strColor+"\\"+ANSI_RESET+"|    |", " |    |", "      |", "      |", "    ====="},
                {" +----+", " O    |", "\\|"+strColor+"/"+ANSI_RESET+"   |", " |    |", "      |", "      |", "    ====="},
                {" +----+", " O    |", "\\|/   |", " |    |", strColor+"/"+ANSI_RESET+"     |", "      |", "    ====="},
                {" +----+", strColor+" O "+ANSI_RESET+"   |", strColor+"\\|/"+ANSI_RESET+"   |", strColor+" |"+ANSI_RESET+"    |", strColor+"/ \\"+ANSI_RESET+"   |", "      |", "    ====="}
        };

        for (String message : hangmanStrArr[numberOfWrongLetters]) {
            System.out.println(message);
        }
    }

    public void blanks(String wordLetters,Set<String>correctLetters,Set<String>wrongLetters) {

        System.out.print(" ");
        for (Character letter : wordLetters.toCharArray()) {
            if (correctLetters.contains(String.valueOf(letter))){
                System.out.print(letter);
            } else {
                System.out.print("_");
            }
            System.out.print(" ");
        }
        System.out.println();
        System.out.print("Wrong Guesses: ");
        for (String letter : wrongLetters){
            System.out.print(letter);
        }
        System.out.println();
    }


}
