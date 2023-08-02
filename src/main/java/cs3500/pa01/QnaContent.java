package cs3500.pa01;

/**
 * Represents question and answer content extracted
 * from a file
 */
public class QnaContent extends SgContent {
  private final String question;
  private final String answer;
  private QuestionDifficulty difficulty;

  /**
   * Instantiates a piece of QnA content that represents a question and answer
   * when given content and whether this question is easy.
   *
   * @param content  a String extracted from a file
   *                 containing QnA content
   * @param difficulty an enumeration representing the difficulty of this question
   */
  public QnaContent(String content, QuestionDifficulty difficulty) {
    super(content);
    this.difficulty = difficulty;
    int bracketStart = content.indexOf("[[");
    int bracketEnd = content.indexOf("]]");
    int colonDivider = content.indexOf(":::");
    this.question =
        content.substring(bracketStart + 2, colonDivider);
    this.answer =
        content.substring(colonDivider + 3, bracketEnd);
  }

  /**
   * Instantiates a piece of QnA content that represents a question and answer
   * when given content
   *
   * @param content  a String extracted from a file
   *                 containing QnA content
   */
  public QnaContent(String content) {
    this(content, QuestionDifficulty.HARD);
  }

  /**
   * Formats a piece of q and a content to be written into a .sr file.
   *
   * @param firstLine  Whether this content is the first line in the sg file.
   * @return  String, formatted string to be written in a .sr file.
   */
  public String format(boolean firstLine) {
    String difficultyLevel;
    if (this.difficulty == QuestionDifficulty.EASY) {
      difficultyLevel = "{{E}}";
    } else {
      difficultyLevel = "{{H}}";
    }
    return this.content + " " + difficultyLevel;
  }

  /**
   * Change the difficulty of this question to easy or hard.
   *
   * @param difficulty  Whether this question should be set to easy or hard.
   */
  public void setDifficulty(QuestionDifficulty difficulty) {
    this.difficulty = difficulty;
  }

  /**
   * Get the question for this qna content.
   *
   * @return  the question associated with this q and a content.
   */
  public String getQuestion() {
    return this.question;
  }

  /**
   * Get the answer for this qna content.
   *
   * @return  the answer associated with this q and a content.
   */
  public String getAnswer() {
    return this.answer;
  }

  /**
   * Returns the question difficulty for this qna content.
   *
   * @return QuestionDifficulty, the difficulty level of this q and a content.
   */
  public QuestionDifficulty getQuestionDifficulty() {
    return this.difficulty;
  }
}
