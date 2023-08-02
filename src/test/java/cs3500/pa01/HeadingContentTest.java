package cs3500.pa01;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Examples and tests to test the HeadingContent class.
 */
public class HeadingContentTest {
  HeadingContent h1;
  HeadingContent h2;

  /**
   * Test that headings will be formatted with a new line if
   * they are not the first line in the study guide.
   */
  @Test
  public void headingFormatTest() {
    h1 = new HeadingContent("#Hello");
    h2 = new HeadingContent("#New heading!");

    assertEquals(h1.format(true), "#Hello");
    assertEquals(h1.format(false), "\n#Hello");
    assertEquals(h2.format(true), "#New heading!");
    assertEquals(h2.format(false), "\n#New heading!");
  }
}
