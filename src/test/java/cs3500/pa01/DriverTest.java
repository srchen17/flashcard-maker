package cs3500.pa01;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import org.junit.jupiter.api.Test;

/**
 * Tests and examples for the Driver class
 */
class DriverTest {

  /**
   * Tests the main method: when given an incorrect number of arguments,
   * throws an exception. Writes correct lines of information into
   * when given a valid path.
   */
  @Test
  public void testMain() {
    Scanner scanSg1;
    assertThrows(IllegalArgumentException.class,
        () -> Driver.main(new String[]{"h"}));

    String[] valid = new String[]{
        "src/test/dataExamples/ArrayVectorNotes",
        "modified", "src/test/dataExamples/sg1.md"};

    Driver.main(valid);
    File sg1 = new File("src/test/dataExamples/sg1.md");

    try {
      scanSg1 = new Scanner(sg1);
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }

    assertEquals(scanSg1.nextLine(), "## Arrays");
    assertEquals(scanSg1.nextLine(), "- anything here");
    assertEquals(scanSg1.nextLine(), "");
    assertEquals(scanSg1.nextLine(), "# Vectors");
    assertEquals(scanSg1.nextLine(), "- important vector info");
    assertEquals(scanSg1.nextLine(), "- goes off the page");
  }


}