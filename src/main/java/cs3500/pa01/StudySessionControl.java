package cs3500.pa01;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * Controls user inputs during Study Session mode
 */
public class StudySessionControl {
  private final Scanner inputScanner;
  private StudySessionModel model;
  private final Appendable appendable;

  /**
   * Instantiates a StudySessionControl object, which can manage
   * and handle user input during a Study Session.
   *
   * @param userInput  A Readable, from which the StudySessionControl object reads user input.
   * @param appendable  An Appendable, to which the object appends output statements/messages to.
   */
  public StudySessionControl(Readable userInput, Appendable appendable) {
    this.inputScanner = new Scanner(userInput);
    this.appendable = appendable;
  }

  /**
   * Runs the study session, and starts it by welcoming the user.
   */
  public void run() {
    welcomeUser();
  }

  /**
   * Welcomes the user, and prompts the user to input a valid .sr file path
   * and how many questions they would like to study.
   */
  private void welcomeUser() {
    try {
      this.appendable.append("""
        WELCOME to your study session! :)
        Type .sr file path and ENTER:\s
          """);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    boolean receivedFile = false;
    File srFile;
    while (!receivedFile) {
      if (inputScanner.hasNextLine()) {
        String userInput = inputScanner.nextLine();
        srFile = new File(userInput);
        if (srFile.exists() && userInput.endsWith(".sr")) {
          receivedFile = true;
          this.promptQuestionCount(srFile);
        } else {
          this.signalInvalidInput(".sr file path");
        }
      }
    }
  }

  /**
   * Requests and accepts the question count for a study session
   * from the user.Then, creates study session model using
   * given srFile and inputted question count.
   */
  private void promptQuestionCount(File srFile) {
    boolean receivedQuestionCount = false;
    int numberOfQuestions = 0;
    this.promptUserInput("an INTEGER COUNT of how many questions you want to study");
    while (!receivedQuestionCount) {
      if (inputScanner.hasNextLine()) {
        String userInput = inputScanner.nextLine();
        try {
          numberOfQuestions = Integer.parseInt(userInput);
          if (!(numberOfQuestions > 0)) {
            this.signalInvalidInput("integer count GREATER than 0");
          }
          else {
            receivedQuestionCount = true;
          }
        } catch (NumberFormatException e) {
          this.signalInvalidInput("integer count");
        }
      }
    }
    this.model = new StudySessionModel(srFile, numberOfQuestions);
    this.showQuestion();
  }

  /**
   * Displays a question to the user and prompts the user to press s
   * to show the answer.
   */
  private void showQuestion() {
    if (this.model.sessionComplete()) {
      showEnd();
    } else {
      String question = this.model.generateQuestion();
      this.displayQuestionAnswer(question, true);
      this.promptUserInput("s to SHOW ANSWER or e to EXIT");
      String userInput;
      boolean validInputReceived = false;
      while (!validInputReceived) {
        if (inputScanner.hasNext()) {
          userInput = inputScanner.next();
          if (userInput.equals("s")) {
            validInputReceived = true;
            showAnswer();
          } else if (userInput.equals("e")) {
            validInputReceived = true;
            showEnd();
          }
        }
      }
    }
  }

  /**
   * Displays an answer to a question to a user and prompts the user
   * to mark it as easy or hard.
   */
  private void showAnswer() {
    this.displayQuestionAnswer(this.model.generateAnswer(), false);
    this.promptUserInput("e to MARK AS EASY or h to MARK AS HARD");
    boolean receivedValidInput = false;
    String input;
    while (!receivedValidInput) {
      if (inputScanner.hasNextLine()) {
        input = inputScanner.nextLine();
        if (input.equals("e")) {
          this.model.setDifficulty(QuestionDifficulty.EASY);
          signalDifficultyChange("easy");
          receivedValidInput = true;
        } else {
          if (input.equals("h")) {
            this.model.setDifficulty(QuestionDifficulty.HARD);
            signalDifficultyChange("hard");
            receivedValidInput = true;
          }
        }
      }
    }
    showQuestion();
  }

  /**
   * Signals to a user that they have inputted
   * an invalid input by printing a message, given
   * the desired input.
   *
   * @param desiredInput  The valid input that the study session needs
   */
  private void signalInvalidInput(String desiredInput) {
    this.outputMessage("Enter a VALID ");
    this.outputMessage(desiredInput);
    this.outputMessage(":\n");
  }

  /**
   * Signals to a user that they have changed the difficulty of
   * a question by printing a message, given the difficulty level
   * they have changed it to.
   *
   * @param difficultyLevel Either easy or hard, the new difficulty of the question.
   */
  private void signalDifficultyChange(String difficultyLevel) {
    this.outputMessage("Marked as ");
    this.outputMessage(difficultyLevel);
    this.outputMessage("!\n");
  }


  /**
   * Prompts a user to input some specific input by printing a message,
   * given the desired valid input.
   *
   * @param desiredUserInput  The valid input that the user must type in order to proceed.
   */
  private void promptUserInput(String desiredUserInput) {
    this.outputMessage("Type ");
    this.outputMessage(desiredUserInput);
    this.outputMessage(" and ENTER:\n");
  }

  /**
   * Displays a question or an answer by printing a message, given
   * the question/answer to display and whether it is a question or answer.
   *
   * @param questionOrAnswer  The question or answer to be shown
   * @param question Whether the given string is a question or answer
   */
  private void displayQuestionAnswer(String questionOrAnswer, boolean question) {
    if (question) {
      this.outputMessage("Q: ");
      this.outputMessage(questionOrAnswer);
      this.outputMessage("\n");
    } else {
      this.outputMessage("A: ");
      this.outputMessage(questionOrAnswer);
      this.outputMessage("\n");
    }
  }

  /**
   * End a study session by printing statistics
   * and an ending message and updating the .sr file to
   * reflect any question difficulty changes.
   */
  private void showEnd() {
    this.model.updateSrFile();
    this.outputMessage(this.model.getStatistics());
  }

  /**
   * Appends given message to output and
   * handles exception if unable to append to output.
   *
   * @param message The message to be outputted to the user.
   */
  private void outputMessage(String message) {
    try {
      this.appendable.append(message);
    } catch (IOException e) {
      System.err.println("Could not append message to output.");
      throw new RuntimeException(e);
    }
  }
}
