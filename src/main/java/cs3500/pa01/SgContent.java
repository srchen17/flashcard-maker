package cs3500.pa01;

/**
 *  Study guide content to be formatted before written into the study guide file.
 */
public abstract class SgContent {

  /**
   * Instantiates a piece of study guide content.
   *
   * @param content  The raw piece of content to be formatted.
   */
  public SgContent(String content) {
    this.content = content;
  }

  protected String content;

  /**
   * Returns a piece of formatted study guide content to be directly written in the study guide.
   *
   * @param firstLine  Whether this content is the first line in the sg file.
   * @return  String, the formatted version of the study guide content.
   */
  public abstract String format(boolean firstLine);

}
