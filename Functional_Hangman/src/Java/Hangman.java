import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Hangman {
    static Scanner keyboard = new Scanner(System.in);
    public static void main(String[] args) {
        boolean checking = true, valid = true;
        String response, filename = "src\\words.txt";
        List<String> words;
        words = getWords(filename);

        do {
            playGame(words);  // call to play one game
            while(valid){
                System.out.print("\n\nWould you like to play again? (yes or no): ");
                response = keyboard.next().toLowerCase();
                if (response.equals("no")){
                    valid = false;
                    checking = false;
                }else if (response.equals("yes")){
                    System.out.println("Starting a new game\n");
                    keyboard.nextLine();
                    break;
                }else{
                    System.out.print("Please enter \"yes\" or \"no\"");
                }
            }
        } while (checking);
        System.out.println("Thanks for playing!");
    }

    static void playGame(List<String> words) {
        String word = randomWord(words);
        if (word == null){
            System.exit(0);
        }
        char guess = ' ';
        boolean validInput = true, validName = true;
        int score = 0;
        String name = "";

        while(validName) {
            System.out.print("What is your name? ");
            name = keyboard.nextLine().toUpperCase();
            validName = validInput(name);
        }

        ArrayList<Character> missed = new ArrayList<>();
        ArrayList<Character> guessed = new ArrayList<>();
        int remaining = word.length();
        while (score < 6 && remaining > 0){
            System.out.println(displayImage(score));
            System.out.println("Missed Characters: " + missed);
            System.out.println(mapLettersToUnderscores(word,String.valueOf(guessed)));

            while (validInput){
                System.out.print("Guess a letter: ");
                String userInput = keyboard.nextLine().toLowerCase();
                if(userInput.isEmpty()){
                    System.out.println("You didn't enter anything, try again");
                }else if(missed.contains(userInput.charAt(0)) || guessed.contains(userInput.charAt(0))) {
                    System.out.println("You already guessed this letter. Try a new one");
                }else{
                        validInput = validInput(userInput);
                        guess = userInput.charAt(0);
                }
            }
            validInput = true;
            if(word.indexOf(guess)==-1){
                score++;
                missed.add(guess);
                displayImage(score);
            }else if(!guessed.contains(guess)){
                guessed.add(guess);
            }
            remaining = 0;
            for(char c : word.toCharArray()){
                if(!guessed.contains(c)){
                    remaining ++;
                }
            }
        }
        if(score == 6){
            System.out.println(displayImage(score));
            System.out.print("Game Over, You Lost\nThe word was: "+ word);
        }else{
            System.out.println("\nYou won. You found the correct word: "+word);
            try{
                int points = word.length()*2 - score;
                List<Integer> scores = Files.readAllLines(Paths.get("src\\scores.txt")).stream()
                        .map(x -> Integer.valueOf(x.substring(x.indexOf(":") + 1)))
                        .sorted()
                        .collect(Collectors.toList());

                int high_score;
                if (scores.isEmpty()){
                    high_score = 0;
                }else {
                    high_score = scores.get(scores.size() - 1);
                }

                if (high_score < points){
                    System.out.printf("You set a new high score of %d points",points);
                }else if(high_score == points){
                    System.out.printf("You have matched the high score of %d points",points);
                }else{
                    System.out.print("Your Score: "+points+"\nCurrent High Score: "+high_score);
                }
                BufferedWriter br = new BufferedWriter(new FileWriter("./src/scores.txt",true));
                br.write(name + ":" + points+"\n");
                br.close();
            }catch(IOException e){
                System.out.println("Error! Unable to locate file with the scores.");
            }
        }
    }

    public static String displayImage(int score){
        String gameImage = "";
        try {
            gameImage = Files.readAllLines(Paths.get("src\\hangman.txt")).get(score);
            gameImage = gameImage.replaceAll(",","\n");
        }catch (IOException e) {
            System.out.println("Error! Unable to locate file with the hangman drawing");
        }
        return gameImage;
    }

    public static boolean validInput(String word){
        if (word.matches("[a-zA-Z]+")){
            return false;
        }else{
            System.out.println("Please enter a valid character (a-z)");
            return true;
        }
    }

    public static String randomWord(List<String> words){
        Collections.shuffle(words);
        return words.stream().reduce((first,second) -> first).orElse(null);
    }

    public static List<String> getWords(String filename){
        List<String> result = new ArrayList<>();
        try{
            try(Stream<String> lines = Files.lines(Path.of(filename))){
                result = lines.collect(Collectors.toList());
            }
        }catch(IOException e){
            System.out.println("Error! Unable to locate file");
            System.exit(0);
        }
        return result;
    }

    public static String mapLettersToUnderscores(String secretWord, String guesses) {
        return Arrays.stream(secretWord.split(""))
                       .map(sl -> guesses.contains(sl) ? sl : " _ ")
                       .reduce(String::concat)
                       .orElse(null);
    }
}
