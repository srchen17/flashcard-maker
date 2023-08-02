package cs3500.pa01;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Examples and tests to test the NameComparator class.
 */
public class NameComparatorTest {
  NameComparator name = new NameComparator();
  File f1 = new File("src/test/SampleData/folder1/A.md");
  File f2 = new File("src/test/SampleData/folder1/Z.md");
  File f3 = new File("src/test/SampleData/folder1/A.md");

  File apple;
  File berry;

  /**
   * Sets up initial files names (apple, berry) for comparison tests.
   */
  @BeforeEach
  public void setup() {
    try {
      apple = File.createTempFile("apple", ".md");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    try {
      berry = File.createTempFile("berry", ".md");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Tests that: when file 1 is named "A" and  file 2 is "Z", compare(f1, f2)
   * returns a negative int, and compare(f2,f1) returns a positive int,
   * and comparing with themselves returns 0.
   */
  @Test
  public void compareNameTest() {
    // modified f1, f2
    assertEquals(name.compare(f1, f2), -25);
    assertEquals(name.compare(f2, f1), 25);
    assertEquals(name.compare(f1, f3), 0);
  }

  /**
   * Tests that: when file 1 is named "apple" and  file 2 is "berry", compare(f1, f2)
   * returns a negative int, and compare(f2,f1) returns a positive int, and
   * comparing with themselves returns 0.
   */
  @Test
  public void compareAppleBerryTest() {
    // modified f1, f2
    assertEquals(name.compare(apple, berry), -1);
    assertEquals(name.compare(berry, apple), 1);
  }
}
