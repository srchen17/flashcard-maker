package cs3500.pa01;

/**
 * Represents raw important info that will be written in the study guide.
 */
public class ImportantContent extends SgContent {
  /**
   * Instantiates a piece of important info.
   *
   * @param content Raw important info extracted from a markdown file, including brackets.
   */
  public ImportantContent(String content) {
    super(content);
  }

  /**
   * Formats this important content into a String form to be directly written in a study guide.
   *
   * @param firstLine whether this important content is the first line in a study guide.
   * @return String  the final String form to be written into the study guide.
   */
  public String format(boolean firstLine) {
    int bracketLength = 2;
    return "- " + this.content.substring(bracketLength, this.content.length() - bracketLength);
  }
}
