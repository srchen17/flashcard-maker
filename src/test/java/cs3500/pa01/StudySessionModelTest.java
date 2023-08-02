package cs3500.pa01;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/**
 * Tests and examples for the class StudySessionModel
 */
public class StudySessionModelTest {
  File srFile;
  StudySessionModel model;
  StudySessionModel setDifficultyModel;
  StudySessionModel completelyRandom;

  /**
   * Sets up initial conditions before each test.
   */
  @BeforeEach
  public void setup() {
    srFile = new File("src/test/dataExamples/ExampleTrivia.sr");
    try {
      FileWriter writer = new FileWriter(srFile);
      writer.write("""
          [[ Taylor Swift song with initials CS? ::: Cruel Summer ]] {{H}}
          [[ Where is the cafe Grace Street located? ::: Ktown ]] {{H}}
          [[ What is one place you can go whale watching?  ::: Iceland ]] {{E}}
          [[ What group is coming to Boston on September 3? ::: aespa]] {{E}}""");
      writer.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    model = new StudySessionModel(srFile, 4, new Random(8));

    // reset file
    setDifficultyModel = new StudySessionModel(
        new File("src/test/dataExamples/ExamplesForSetDifficulty.sr"),
        4, new Random(8));
    try {
      FileWriter writer = new FileWriter("src/test/dataExamples/ExamplesForSetDifficulty.sr");
      writer.write("""
          [ easy question 1::: easy ]]] {{E}}
          [[ easy question 2::: easy ]]] {{E}}
          [[ hard question 1::: hard ]]] {{H}}
          [[ hard question 2::: hard ]]] {{H}}
          """);
      writer.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    completelyRandom = new StudySessionModel(srFile, 5);
  }

  /**
   * Test the method getStatistics in the class StudySessionModel
   */
  @Test
  public void testGetStatistics() {
    // verify that it properly formats and
    // properly displays the statistics.

    srFile = new File("src/test/dataExamples/ExampleTrivia.sr");

    assertEquals(model.getStatistics(),
        """
            NICE! You completed the Study Session :D\s
            HERE ARE YOUR STATS:\s
                You answered a crazy 0 questions!
                Easy -> Hard: 0
                Hard -> Easy: 0
            Question Bank:\s
                2 hard questions    2 easy questions""");

  }

  /**
   * Test the method setDifficulty in the class StudySessionModel, and that
   * it properly updates all question difficulties as prompted by user.
   */
  @Test
  public void testSetDifficulty() {

    // check before counts
    assertEquals(setDifficultyModel.getStatistics(),
        """
            NICE! You completed the Study Session :D\s
            HERE ARE YOUR STATS:\s
                You answered a crazy 0 questions!
                Easy -> Hard: 0
                Hard -> Easy: 0
            Question Bank:\s
                2 hard questions    2 easy questions""");

    // generates a hard question
    setDifficultyModel.generateQuestion();

    // verify it is hard
    Scanner scanner;
    try {
      scanner = new Scanner(
          new File("src/test/dataExamples/ExamplesForSetDifficulty.sr").toPath());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      if (line.contains("hard question 2")) {
        assertEquals(line.substring(line.length() - 5), "{{H}}");
      }
    }

    // sets it to hard, does nothing
    setDifficultyModel.setDifficulty(QuestionDifficulty.HARD);

    // generates a hard question
    setDifficultyModel.generateQuestion();
    // sets it to easy
    setDifficultyModel.setDifficulty(QuestionDifficulty.EASY);
    // now the question's difficulty is easy, check at end of study session

    // check hardToEasy count
    assertEquals(setDifficultyModel.getStatistics(),
        """
            NICE! You completed the Study Session :D\s
            HERE ARE YOUR STATS:\s
                You answered a crazy 2 questions!
                Easy -> Hard: 0
                Hard -> Easy: 1
            Question Bank:\s
                1 hard questions    3 easy questions""");

    // generate an easy question
    setDifficultyModel.generateQuestion();

    // check that it is easy
    try {
      scanner = new Scanner(
          new File("src/test/dataExamples/ExamplesForSetDifficulty.sr").toPath());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      if (line.contains("easy question 2")) {
        assertEquals(line.substring(line.length() - 5), "{{E}}");
      }
    }

    // set an easy question to hard
    setDifficultyModel.setDifficulty(QuestionDifficulty.HARD);

    // check easy to hard count
    assertEquals(setDifficultyModel.getStatistics(),
        """
            NICE! You completed the Study Session :D\s
            HERE ARE YOUR STATS:\s
                You answered a crazy 3 questions!
                Easy -> Hard: 1
                Hard -> Easy: 1
            Question Bank:\s
                2 hard questions    2 easy questions""");


    // generate easy question
    setDifficultyModel.generateQuestion();

    // verify it is easy
    try {
      scanner = new Scanner(
          new File("src/test/dataExamples/ExamplesForSetDifficulty.sr").toPath());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      if (line.contains("easy question 1")) {
        assertEquals(line.substring(line.length() - 5), "{{E}}");
      }
    }

    // set it to easy, does nothing
    setDifficultyModel.setDifficulty(QuestionDifficulty.EASY);

    // update the .sr file, mimics what control would call at
    // end of session
    setDifficultyModel.updateSrFile();

    // Now check that all questions just updated were properly updated:
    try {
      scanner = new Scanner(
          new File("src/test/dataExamples/ExamplesForSetDifficulty.sr").toPath());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      String difficultySubstring = line.substring(line.length() - 5);

      // check hard question 1 was set to easy
      if (line.contains("hard question 1")) {
        assertEquals(difficultySubstring, "{{E}}");
      } else {
        // check easy question 2 was set to hard
        if (line.contains("easy question 2")) {
          assertEquals(difficultySubstring, "{{H}}");
        } else {
          // check hard question 2 is still set to hard
          if (line.contains("hard question 2")) {
            assertEquals(difficultySubstring, "{{H}}");
          } else {
            // check easy question 1 is still set to easy
            if (line.contains("easy question 1")) {
              assertEquals(difficultySubstring, "{{E}}");
            }
          }
        }
      }
    }

    // Catch runtime exception if invalid filepath
    assertThrows(RuntimeException.class, () -> new StudySessionModel(
        new File("/src/totallyInvalid"), 3));
  }

  /**
   * Test the method generates correct answers and questions in the class StudySessionModel.
   *
   */
  @Test
  public void testGenerateAnswerAndQuestion() {
    //   * Test that it returns the correct question and answer strings when called
    //   * one after another. Check that the questions generated first are hard questions.

    StudySessionModel genAnswerModel = new StudySessionModel(
        srFile, 3, new Random(6));
    Scanner scanner;

    // generate first question
    assertEquals(genAnswerModel.generateQuestion(),
        " Where is the cafe Grace Street located? ");
    // generate answer and check that it is the correct answer
    assertEquals(genAnswerModel.generateAnswer(), " Ktown ");

    // check that it is a hard question
    try {
      scanner = new Scanner(
          srFile.toPath());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      if (line.contains("Grace Street")) {
        assertEquals(line.substring(line.length() - 5), "{{H}}");
      }
    }

    // generate second question
    assertEquals(genAnswerModel.generateQuestion(),
        " Taylor Swift song with initials CS? ");
    // generate answer and check that it is correct
    assertEquals(genAnswerModel.generateAnswer(), " Cruel Summer ");

    // check that it is a hard question
    try {
      scanner = new Scanner(
          srFile.toPath());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      if (line.contains("Cruel Summer")) {
        assertEquals(line.substring(line.length() - 5), "{{H}}");
      }
    }

    // generate a third question (now all hard questions have been shown)
    assertEquals(genAnswerModel.generateQuestion(),
        " What group is coming to Boston on September 3? ");
    // generate an answer and check that answer is correct
    assertEquals(genAnswerModel.generateAnswer(), " aespa");
    // check that it is an easy question (now that all hard have been shown)
    try {
      scanner = new Scanner(
          srFile.toPath());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      if (line.contains("September")) {
        assertEquals(line.substring(line.length() - 5), "{{E}}");
      }
    }
  }

  /**
   * Test the method session complete in StudySessionModel, and
   * how it responds to empty/nonempty question lists
   */
  @Test
  public void testSessionComplete() {
    // already tested through test runs done in
    // study session model tests, but will check
    // specific cases below:

    // test if hard and easy questions are empty
    File studyFile = new File("src/test/dataExamples/sg1.sr");
    StudySessionModel sessionModel =
        new StudySessionModel(studyFile, 10);
    assertTrue(sessionModel.sessionComplete());

    // if only hard questions is empty
    studyFile = new File("src/test/dataExamples/OnlyEasyQuestions.sr");
    sessionModel =
        new StudySessionModel(studyFile, 10);
    assertFalse(sessionModel.sessionComplete());

    // if only easy questions is empty
    studyFile = new File("src/test/dataExamples/OnlyHardQuestions.sr");
    sessionModel =
        new StudySessionModel(studyFile, 10);
    assertFalse(sessionModel.sessionComplete());

    // if both are not empty
    studyFile = new File("src/test/dataExamples/QnaRunTests.sr");
    sessionModel =
        new StudySessionModel(studyFile, 10);
    assertFalse(sessionModel.sessionComplete());
  }
}
