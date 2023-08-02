package cs3500.pa01;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Examples and tests to test the CreatedComparator class.
 */

public class CreatedComparatorTest {
  File temp1;
  File sameTimeTemp1;
  File temp2;
  CreatedComparator created;
  File invalid;

  /**
   * Sets up initial conditions before each test.
   */
  @BeforeEach
  public void setup() {
    this.created = new CreatedComparator();
    try {
      temp1 = File.createTempFile("createdFirst", ".md");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    try {
      sameTimeTemp1 = File.createTempFile("createdFirst1", ".md");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    try {
      temp2 = File.createTempFile("createdSecond", ".md");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    invalid = new File("invalidPath");
  }

  /**
   * Test that with temporary file 1, which was created earlier than
   * temporary file 2, compare(temp1, temp2) returns a negative, and
   * compare (temp2, temp1) returns a positive.
   */
  @Test
  public void testCreatedComparator() {
    assertEquals(created.compare(temp1, temp2), -1);
    assertEquals(created.compare(temp2, temp1), 1);
    assertEquals(created.compare(sameTimeTemp1, temp1), 0);
  }

  /**
   * Test that comparing files with invalid paths throws a
   * runtime exception.
   */
  @Test
  public void invalidCreatedComparator() {
    assertThrows(RuntimeException.class, () -> created.compare(invalid, temp1));
    assertThrows(RuntimeException.class, () -> created.compare(temp1, invalid));
  }
}



