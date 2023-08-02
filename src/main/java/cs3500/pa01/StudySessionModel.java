package cs3500.pa01;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * A model managing data and question/answer content during
 * a Study Session.
 */
public class StudySessionModel {
  // fields
  private final File srFile;
  private final ArrayList<QnaContent> questionList;
  private QnaContent currentQuestion;
  private final int questionCount;
  private int easyToHard;
  private int hardToEasy;
  private int numberQuestionsShown;
  private final ArrayList<QnaContent> hardQuestions;
  private final ArrayList<QnaContent> easyQuestions;
  private final Random rand;

  /**
   * Instantiates a StudySessionModel that randomly chooses
   * questions, starting with hard questions.
   *
   * @param srFile  The file with the question bank to be studied.
   * @param questionCount  The number of questions the user wants to study.
   */
  public StudySessionModel(File srFile, int questionCount) {
    this(srFile, questionCount, new Random());
  }

  /**
   * Instantiates a StudySessionModel that can be used for testing
   * and given a random object.
   *
   * @param srFile  The file with the question bank to be studied.
   * @param questionCount  The number of questions the user wants to study.
   * @param rand  A Random object to be used to select 'random' questions.
   */
  public StudySessionModel(File srFile, int questionCount, Random rand) {
    this.srFile = srFile;
    this.questionCount = questionCount;
    this.questionList = extractQnaContent();
    this.rand = rand;
    this.easyQuestions = new ArrayList<>();
    this.hardQuestions = new ArrayList<>();
    getHardAndEasyQuestions(easyQuestions, hardQuestions);
    this.easyToHard = 0;
    this.hardToEasy = 0;
    this.numberQuestionsShown = 0;
  }

  /**
   * Given a .sr File, extracts all Qna content from it (questions and answers).
   *
   * @return  An ArrayList of QnaContent, a list of questions from the question bank.
   */
  private ArrayList<QnaContent> extractQnaContent() {
    Scanner scanner;
    ArrayList<QnaContent> result = new ArrayList<>();
    try {
      scanner = new Scanner(srFile.toPath());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      String qna = line.substring(0, line.length() - 6);
      String difficulty = line.substring(line.length() - 3, line.length() - 2);
      QuestionDifficulty difficultyEnum;
      if (difficulty.equals("E")) {
        difficultyEnum = QuestionDifficulty.EASY;
      } else {
        difficultyEnum = QuestionDifficulty.HARD;
      }
      QnaContent qnaContent = new QnaContent(qna, difficultyEnum);
      result.add(qnaContent);
    }
    return result;
  }

  /**
   * Randomly select a question, starting with hard questions first,
   * and easy questions after all hard questions are shown.
   *
   * @return  String, a question.
   */
  public String generateQuestion() {
    ArrayList<QnaContent> getQuestionsFrom;
    if (!hardQuestions.isEmpty()) {
      getQuestionsFrom = hardQuestions;
    } else {
      getQuestionsFrom = easyQuestions;
    }
    int index = this.rand.nextInt(getQuestionsFrom.size());
    QnaContent question = getQuestionsFrom.get(index);
    this.currentQuestion = question;
    String questionString = getQuestionsFrom.get(index).getQuestion();
    getQuestionsFrom.remove(question);
    this.numberQuestionsShown = this.numberQuestionsShown + 1;
    return questionString;
  }

  /**
   * Generates the answer of the current question (the question
   * that the user is currently being shown).
   *
   * @return  A String representing the answer to the current question.
   */
  public String generateAnswer() {
    return this.currentQuestion.getAnswer();
  }

  /**
   * Returns whether the study session is complete.
   *
   * @return  true if the study session is over, false if it is not.
   */
  public boolean sessionComplete() {
    return (this.hardQuestions.isEmpty() && this.easyQuestions.isEmpty())
            || (this.numberQuestionsShown >= this.questionCount);
  }

  /**
   * Given a boolean determining if the current question should be
   * set as easy, sets a question's difficulty.
   *
   * @param difficulty  True if the question should be marked as easy, false if difficult.
   */
  public void setDifficulty(QuestionDifficulty difficulty) {
    QuestionDifficulty initial = this.currentQuestion.getQuestionDifficulty();
    if (!(initial == difficulty)) {
      this.currentQuestion.setDifficulty(difficulty);
      if (initial == QuestionDifficulty.EASY) {
        easyToHard++;
      } else {
        hardToEasy++;
      }
    }
  }

  /**
   * Updates the .sr file at the end of the session to
   * reflect any updates to question difficulty.
   */
  public void updateSrFile() {
    try {
      FileWriter writer = new FileWriter(srFile.getAbsolutePath());
      for (QnaContent q : this.questionList) {
        writer.write(q.format(false) + "\n");
      }
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
      throw new IllegalArgumentException("Invalid file given.");
    }
  }

  /**
   * Adds all easy questions from this question list to the first
   * given array list, and adds all hard questions from this question
   * list to the second array list.
   *
   * @param easy  An array list where all easy questions will be added to.
   * @param hard  An array list where all hard questions will be added to.
   */
  private void getHardAndEasyQuestions(ArrayList<QnaContent> easy, ArrayList<QnaContent> hard) {
    for (QnaContent q : questionList) {
      if (q.getQuestionDifficulty() == QuestionDifficulty.EASY) {
        easy.add(q);
      } else {
        hard.add(q);
      }
    }
  }

  /**
   * Gets all study session statistics and formats them as a String.
   * (Including number questions studied, number of types of questions
   * in the question bank, and how many questions changed difficulty.)
   *
   * @return  A String with all study session statistics.
   */
  public String getStatistics() {
    ArrayList<QnaContent> easy = new ArrayList<>();
    ArrayList<QnaContent> hard = new ArrayList<>();
    this.getHardAndEasyQuestions(easy, hard);
    return "NICE! You completed the Study Session :D \n"
        + "HERE ARE YOUR STATS: \n"
        + "    You answered a crazy " + this.numberQuestionsShown + " questions!\n"
        + "    Easy -> Hard: " + this.easyToHard + "\n"
        + "    Hard -> Easy: " + this.hardToEasy + "\n"
        + "Question Bank: \n"
        + "    " + hard.size() + " hard questions"
        + "    " + easy.size() + " easy questions";
  }
}
