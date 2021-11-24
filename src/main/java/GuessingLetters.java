import java.util.HashSet;
import java.util.Set;

public class GuessingLetters {
    public String word ="";
    public String guess ="";
    public Set<String> wordLetters = new HashSet<>();
    public Set<String> correctLetters = new HashSet<>();
    public Set<String> wrongLetters = new HashSet<>();

    //======= Setters
    public void setWord(String word) {
        this.word = word;
        this.setWordLetters(word);
    }

    public void setGuess(String guess) {
        this.guess = guess.toUpperCase();
    }

    public void setWordLetters(Set<String> wordLetters) {
        this.wordLetters = wordLetters;
    }

    public void setWordLetters(String word) {
        for (Character letter : word.toCharArray()){
            wordLetters.add(String.valueOf(letter));
        }
    }

    private void setCorrectLetters(Set<String> correctLetters) {
        this.correctLetters = correctLetters;
    }

    private void setCorrectLetter(String guess) {
        for (char letter : this.word.toCharArray()) {
            if (String.valueOf(letter).equalsIgnoreCase(guess)) {
                correctLetters.add(guess);
            }
        }
    }

    private void setWrongLetters(Set<String> wrongLetters) {
        this.wrongLetters = wrongLetters;
    }

    private void setWrongLetter(String guess){
        wrongLetters.add(guess);
        wrongLetters.removeAll(correctLetters);
    }

    //======== Getters
    public String getWord() {
        return word;
    }

    public String getGuess() {
        return guess;
    }

    public Set<String> getWordLetters() {
        return wordLetters;
    }

    public Set<String> getCorrectLetters() {
        return correctLetters;
    }

    public Set<String> getWrongLetters() {
        return wrongLetters;
    }
    //==== Methods

    public void reset(){
        wordLetters.clear();
        wrongLetters.clear();
        correctLetters.clear();
    }

    public boolean alreadyGuessed(){
        return wrongLetters.contains(this.guess);
    }
    public boolean win() {
        return this.getCorrectLetters().size() == this.getWordLetters().size();
    }
    public boolean lose() {
        return this.wrongLetters.size() == 6;
    }
    public boolean finished(){
        return this.win() || this.lose();
    }

    public void checkLetters() {
        this.setCorrectLetter(this.guess);
        this.setWrongLetter(this.guess);
    }
}
