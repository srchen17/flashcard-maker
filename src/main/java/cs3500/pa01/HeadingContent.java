package cs3500.pa01;

/**
 * Represents raw heading info that will be written in the study guide.
 */
public class HeadingContent extends SgContent {
  /**
   * Instantiates a piece of heading content with the given string.
   *
   * @param content Raw heading content extracted from a markdown file, including hashtags.
   */
  public HeadingContent(String content) {
    super(content);
  }

  /**
   * Formats this heading content into a String form to be directly written in a study guide.
   *
   * @param firstLine whether this heading content is the first line in a study guide.
   * @return String  the final String form to be written into the study guide.
   */
  public String format(boolean firstLine) {
    if (firstLine) {
      return this.content;
    } else {
      return "\n" + this.content;
    }
  }
}
