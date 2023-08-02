package cs3500.pa01;


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Examples and tests for the ImportantContent class.
 */
public class ImportantContentTest {
  ImportantContent hello = new ImportantContent("[[Hello]]");
  ImportantContent objectoriented = new ImportantContent("[[This is object oriented!]]");

  /**
   * Test format in the class important content: removes brackets and adds bullet.
   */
  @Test
  public void testImportantContent() {
    assertEquals(hello.format(true), "- Hello");
    assertEquals(hello.format(false), "- Hello");
    assertEquals(objectoriented.format(true), "- This is object oriented!");
    assertEquals(objectoriented.format(false), "- This is object oriented!");
  }
}
