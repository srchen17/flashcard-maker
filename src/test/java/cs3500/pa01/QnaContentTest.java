package cs3500.pa01;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Tests and examples for the class QnAContent
 */
public class QnaContentTest {
  QnaContent hardAddition = new QnaContent("[[2 + 2 ::: 4]]");
  QnaContent easyAddition = new QnaContent("[[3 + 3 ::: 6]]", QuestionDifficulty.EASY);
  QnaContent mathWithPunctuation = new QnaContent("[[ Is (5+2) the same as [5 + 2]? ::: Yes! ]]");

  QnaContent longStringWithSpaces = new QnaContent(
      "[[  Why does Kierkegaard say Abraham experiences distress? "
          + "       :::       "
          + "He says, Abraham is silent, but cannot speak, "
          + "therein lies the distress and anguish.\" "
          + "(Because he cannot make himself understood.) ]]", QuestionDifficulty.HARD);


  /**
   * Test the format method in the class QnAContent. Test that it can
   * format questions and answers with punctuation ands spaces, and
   * adds the appropriate letter to hard / easy questions.
   */
  @Test
  public void testFormatMethod() {
    // False / true produce same
    // format hard question
    assertEquals(hardAddition.format(false),
        "[[2 + 2 ::: 4]] {{H}}");
    assertEquals(hardAddition.format(true),
        "[[2 + 2 ::: 4]] {{H}}");

    // format easy question
    assertEquals(easyAddition.format(false),
        "[[3 + 3 ::: 6]] {{E}}");
    assertEquals(easyAddition.format(true),
        "[[3 + 3 ::: 6]] {{E}}");

    // format string with more punctuation
    assertEquals(mathWithPunctuation.format(true),
        "[[ Is (5+2) the same as [5 + 2]? ::: Yes! ]] {{H}}");

    // format long string
    assertEquals(longStringWithSpaces.format(true),
        "[[  Why does Kierkegaard say Abraham experiences distress? "
            + "       :::       "
            + "He says, Abraham is silent, but cannot speak, "
            + "therein lies the distress and anguish.\" "
            + "(Because he cannot make himself understood.) ]] {{H}}");
  }

  /**
   * Test that setting the difficulty changes a question's difficulty.
   * If given true, set a question to easy. If given false, set to hard.
   */
  @Test
  public void testSetDifficultyMethod() {
    QnaContent easyQuestion = new QnaContent("[[question ::: ans ]]]", QuestionDifficulty.EASY);
    assertEquals(easyQuestion.getQuestionDifficulty(), QuestionDifficulty.EASY);
    // set difficulty to easy
    easyQuestion.setDifficulty(QuestionDifficulty.EASY);
    assertEquals(easyQuestion.getQuestionDifficulty(), QuestionDifficulty.EASY);
    // set difficulty to hard
    easyQuestion.setDifficulty(QuestionDifficulty.HARD);
    assertEquals(easyQuestion.getQuestionDifficulty(), QuestionDifficulty.HARD);

    QnaContent hardQuestion = new QnaContent("[[question ::: ans ]]]", QuestionDifficulty.HARD);
    assertEquals(hardQuestion.getQuestionDifficulty(), QuestionDifficulty.HARD);
    // set difficulty to hard
    hardQuestion.setDifficulty(QuestionDifficulty.HARD);
    assertEquals(hardQuestion.getQuestionDifficulty(), QuestionDifficulty.HARD);
    // set difficulty to easy
    hardQuestion.setDifficulty(QuestionDifficulty.EASY);
    assertEquals(hardQuestion.getQuestionDifficulty(), QuestionDifficulty.EASY);
  }

}
