import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HangmanTest {

    @Test
    void displayWord(){
        assertEquals(Hangman.mapLettersToUnderscores("cat",""), " _  _  _ ");
        assertEquals(Hangman.mapLettersToUnderscores("cat","c"), "c _  _ ");
        assertEquals(Hangman.mapLettersToUnderscores("cat","t"), " _  _ t");
        assertEquals(Hangman.mapLettersToUnderscores("cat","at"), " _ at");
        assertEquals(Hangman.mapLettersToUnderscores("cat","aapt"), " _ at");
    }

    @Test
    void validInput() {
        assertFalse(Hangman.validInput("a"));
        assertTrue(Hangman.validInput("1"));
        assertTrue(Hangman.validInput("?"));
    }

    @Test
    void displayHangman(){
        String option1 = "H A N G M A N\n" +
                " +---+\n" +
                "     |\n" +
                "     |\n" +
                "     |\n" +
                "     |\n" +
                "=======";

        String option3 = "H A N G M A N\n" +
                " +---+\n" +
                " O   |\n" +
                "/|   |\n" +
                "     |\n" +
                "     |\n" +
                "=======";

        assertEquals(option1,Hangman.displayImage(0));
        assertEquals(option3,Hangman.displayImage(3));

    }

    @Test
    void retrieveWordFromList(){
        List<String> list = List.of("Alex");
        assertEquals("Alex",Hangman.randomWord(list));
    }
}