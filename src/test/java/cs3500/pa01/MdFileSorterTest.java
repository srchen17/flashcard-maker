package cs3500.pa01;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests and examples for the class SortedMdFiles.
 */
public class MdFileSorterTest {

  MdFileSorter name;
  MdFileSorter modified;
  MdFileSorter created;
  MdFileSorter invalid;

  File tempA;
  File tempB;
  File tempC;
  File tempD;


  /**
   * Sets up initial conditions before each test.
   */
  @BeforeEach
  public void setup() {
    try {
      tempA = File.createTempFile("Apple", ".md");
      Thread.sleep(1000);
      tempB = File.createTempFile("Bear", ".md");
      Thread.sleep(1000);
      tempC = File.createTempFile("Crocodile", ".md");
      Thread.sleep(1000);
      tempD = File.createTempFile("Dinosaur", ".md");
    } catch (IOException e) {
      System.err.println("Invalid file path");
      throw new RuntimeException(e);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }

    name = new MdFileSorter("filename",
        new ArrayList<>(
            Arrays.asList(tempB, tempA, tempC, tempD)));
    modified = new MdFileSorter("modified",
        new ArrayList<>(
            Arrays.asList(tempB, tempA, tempD, tempC)));
    created = new MdFileSorter("created",
        new ArrayList<>(
            Arrays.asList(tempC, tempD, tempA, tempB)));

    invalid = new MdFileSorter("invalidFlag",
        new ArrayList<>(Arrays.asList(tempB, tempA, tempC, tempD)));
  }


  /**
   * Tests the method sortFlag, that it can sort correctly
   * by modified, created, and filename.
   */
  @Test
  public void sortTest() {
    // initialized above with arrays in incorrect order.

    ArrayList<File> sortedArray = new ArrayList<>(
        Arrays.asList(tempA, tempB, tempC, tempD));

    assertEquals(name.sortByFlag(), sortedArray);
    assertEquals(modified.sortByFlag(), sortedArray);
    assertEquals(created.sortByFlag(), sortedArray);
    assertThrows(IllegalArgumentException.class, invalid::sortByFlag,
        "2nd arg must be filename, created, or modified");
  }

  /**
   * Tests that sortFlag throws an exception when given
   * a flag tht is not created, modified, or filename.
   */
  @Test
  public void sortEdgeCases() {
    MdFileSorter invalidName = new MdFileSorter("Filename",
        new ArrayList<>(
            Arrays.asList(tempB, tempA, tempC, tempD)));
    MdFileSorter invalidModified = new MdFileSorter("Modified",
        new ArrayList<>(
            Arrays.asList(tempB, tempA, tempD, tempC)));
    MdFileSorter invalidCreated = new MdFileSorter("Created",
        new ArrayList<>(
            Arrays.asList(tempC, tempD, tempA, tempB)));

    assertThrows(IllegalArgumentException.class, invalidName::sortByFlag,
        "2nd arg must be filename, created, or modified");
    assertThrows(IllegalArgumentException.class, invalidModified::sortByFlag,
        "2nd arg must be filename, created, or modified");
    assertThrows(IllegalArgumentException.class, invalidCreated::sortByFlag,
        "2nd arg must be filename, created, or modified");
  }

  /**
   * Tests that when given invalid flags will throw an exception, and tests
   * that when flag is null, throws a null pointer exception.
   */
  @Test
  public void sortInvalidPathTest() {
    assertThrows(IllegalArgumentException.class, invalid::sortByFlag,
        "2nd arg must be filename, created, or modified");
    MdFileSorter invalidSort = new MdFileSorter("euoadf", new ArrayList<>());
    assertThrows(IllegalArgumentException.class, invalidSort::sortByFlag,
        "2nd arg must be filename, created, or modified");

    MdFileSorter invalidNull = new MdFileSorter(null, new ArrayList<>());
    assertThrows(NullPointerException.class, invalidNull::sortByFlag);
  }
}
