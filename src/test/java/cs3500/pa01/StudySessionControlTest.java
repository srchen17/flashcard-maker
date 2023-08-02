package cs3500.pa01;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.util.Scanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/**
 * Tests and examples for the class StudySessionControl
 */
public class StudySessionControlTest {
  StudySessionControl control;
  Readable userInput;
  Appendable output;

  /**
   * Set up initial conditions before each test
   */
  @BeforeEach
  public void setup() {
    userInput = new StringReader("");
    output = System.out;
    control = new StudySessionControl(userInput, output);

    File srFile = new File("src/test/dataExamples/TriviaQuestionsDuplicate.sr");
    try {
      FileWriter writer = new FileWriter(srFile);
      writer.write("""
          [[ Taylor Swift song with initials CS? ::: Cruel Summer ]] {{E}}
          [[ Where is the cafe Grace Street located? ::: Ktown ]] {{H}}
          [[ What is one place you can go whale watching?  ::: Iceland ]] {{E}}
          [[ What group is coming to Boston on September 3? ::: aespa]] {{E}}""");
      writer.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Test that when called to run with a one question
   * study session: will correctly display welcome message,
   * generate question, generate answer, and
   * generate correct stats.
   */
  @Test
  public void testOneQuestionStudySession() {
    FileWriter controlOutput;
    FileReader controlledReader;

    // create a temporary file for study session to output to.
    File tempOutput;
    try {
      tempOutput = File.createTempFile("temp", ".md");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    // create a file reader to read pre-written user input.
    try {
      controlledReader = new FileReader(
          "src/test/dataExamples/ExampleForStudySessionControl.md");
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }

    try {
      controlOutput = new FileWriter(tempOutput, true);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    // create study session control with the user input and output above
    StudySessionControl c = new StudySessionControl(controlledReader, controlOutput);
    // run the study session
    c.run();

    try {
      controlOutput.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    // test that the output is correct!

    // create scanner for output
    Scanner s;
    try {
      s = new Scanner(tempOutput);
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }

    // check welcome messages are correct
    assertEquals(s.nextLine(),
        "WELCOME to your study session! :)");
    assertEquals(s.nextLine(),
        "Type .sr file path and ENTER: ");
    assertEquals(s.nextLine(),
        "Type an INTEGER COUNT of how many questions you want to study and ENTER:");

    // input file enters valid sr file and count
    // check that question is then generated
    assertTrue(s.nextLine().startsWith("Q: "));

    // check that user is then prompted to type s
    assertEquals(s.nextLine(),
        "Type s to SHOW ANSWER or e to EXIT and ENTER:");

    // input file enters 's' for show answer
    // check that answer is then generated
    assertTrue(s.nextLine().startsWith("A: "));

    // check that user is prompted to mark as hard or easy
    assertEquals(s.nextLine(),
        "Type e to MARK AS EASY or h to MARK AS HARD and ENTER:");

    // check validation message
    assertEquals(s.nextLine(),
        "Marked as easy!");

    // check that the ending is now shown
    assertEquals(s.nextLine(),
        "NICE! You completed the Study Session :D ");
    assertEquals(s.nextLine(),
        "HERE ARE YOUR STATS: ");
    assertEquals(s.nextLine(),
        "    You answered a crazy 1 questions!");
    assertEquals(s.nextLine(),
        "    Easy -> Hard: 0");
    assertEquals(s.nextLine(),
        "    Hard -> Easy: 1");
    assertEquals(s.nextLine(),
        "Question Bank: ");
    assertEquals(s.nextLine(),
        "    0 hard questions    4 easy questions");

    // delete temporary output file
    try {
      Files.deleteIfExists(tempOutput.toPath());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Tests that control will print a statement notifying a
   * user of an invalid input, and validating after a user selects question
   * difficulty.
   */
  @Test
  public void testUserInputResponses() {
    FileWriter controlOutput;
    FileReader controlledReader;

    File tempOutput;

    // create temporary output file
    try {
      tempOutput = File.createTempFile("temp", ".md");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    // create reader to read pre-written invalid inputs
    try {
      controlledReader = new FileReader(
          "src/test/dataExamples/InvalidInputs");
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }

    try {
      controlOutput = new FileWriter(tempOutput, true);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    // create scanner for pre-written invalid inputs
    Scanner s;
    try {
      s = new Scanner(tempOutput);
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }

    // create study session control with above inputs and outputs
    StudySessionControl c = new StudySessionControl(
        controlledReader, controlOutput);

    // run study session
    c.run();

    try {
      controlOutput.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    // check that after given invalid input, will send error message
    // SKIP WELCOME LINES, ALREADY CHECKED ABOVE
    s.nextLine();
    s.nextLine();

    // check that it prompts user for file path after entering an invalid path
    assertEquals(s.nextLine(),
        "Enter a VALID .sr file path:");
    s.nextLine(); // skip line prompting integer count, user then enters invalid input

    // check that it prompts user for question count after entering an invalid count
    assertEquals(s.nextLine(),
        "Enter a VALID integer count:");

    // skip 9 lines
    for (int i = 0; i < 9; i++) {
      s.nextLine();
    }
    // check validation method for marking question difficulty
    assertEquals(s.nextLine(),
        "Marked as hard!");

    try {
      Files.deleteIfExists(tempOutput.toPath());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Tests that if a user presses e, will exit
   * in the middle of a study session and show statistics.
   */
  @Test
  public void testExitStudySession() {
    // create files for inputs and outputs of a study session
    FileReader userInputs;
    FileWriter output;
    File outputFile = new File(
        "src/test/dataExamples/PossibleUserInputs/exitOutputs");
    try {
      userInputs = new FileReader(
          "src/test/dataExamples/PossibleUserInputs/possibleInputs.md"
      );
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }
    try {
      output = new FileWriter(outputFile);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    // create a study session with an input file that
    // will exit it in the middle of a session
    StudySessionControl exitControl =
        new StudySessionControl(userInputs, output);
    exitControl.run();
    try {
      output.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    // test that the exit occurs at the right time :
    // create a scanner for output and skip first 15 lines
    Scanner outputScanner;
    try {
      outputScanner = new Scanner(outputFile);
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }
    for (int i = 0; i < 15; i++) {
      outputScanner.nextLine();
    }
    // check exit :
    assertEquals(outputScanner.nextLine(),
        "NICE! You completed the Study Session :D ");
  }
}
