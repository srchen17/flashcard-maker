package cs3500.pa01;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Examples and tests to test the ModifiedComparator class.
 */
public class ModifiedComparatorTest {
  File f1;
  File f2;
  File f3;
  ModifiedComparator modified;

  /**
   * Sets up files before each test
   */
  @BeforeEach
  public void setup() {
    f1 = new File("src/test/dataExamples/ExamplesForFileVisitor/A.md");
    f2 = new File("src/test/dataExamples/ExamplesForFileVisitor/D.md");
    modified = new ModifiedComparator();
    f3 = f1;
  }

  /**
   * Tests that: when file 1 is modified before file 2, compare(f1, f2) returns a negative int,
   * and compare(f2,f1) returns a positive int. When compared to themselves, returns 0.
   */
  @Test
  public void compareModifiedTest() {
    FileTime ft1 = FileTime.fromMillis(5);
    FileTime ft2 = FileTime.fromMillis(10);

    try {
      Files.setLastModifiedTime(f1.toPath(), ft1);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    try {
      Files.setLastModifiedTime(f2.toPath(), ft2);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    // modified f1, f2
    assertEquals(modified.compare(f1, f2), -1);
    assertEquals(modified.compare(f2, f1), 1);
    assertEquals(modified.compare(f1, f3), 0);
  }

  /**
   * Test that invalid file names will throw runtime exceptions, for
   * both the first file and second file parameters.
   */
  @Test
  public void compareModifiedThrowsTest() {
    f1 = new File("invalid");
    f2 = new File("invalid");

    // modified f1, f2
    assertThrows(RuntimeException.class, () -> modified.compare(f1, f2));

    f1 = new File("src/test/dataExamples/folder1/A.md");
    assertThrows(RuntimeException.class, () -> modified.compare(f1, f2));
  }
}
