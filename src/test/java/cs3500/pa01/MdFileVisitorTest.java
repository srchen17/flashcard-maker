package cs3500.pa01;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests and examples for the class MdFileVisitor
 */
public class MdFileVisitorTest {
  MdFileVisitor visitor;
  File fileA;
  File fileD;
  File pdf;
  Path pathA;
  Path pathB;

  /**
   * Sets up initial conditions before each test.
   */
  @BeforeEach
  public void setup() {
    visitor = new MdFileVisitor();
    fileA = new File("src/test/dataExamples/ExamplesForFileVisitor/A.md");
    fileD = new File("src/test/dataExamples/ExamplesForFileVisitor/D.md");
    pathA = fileA.toPath();
    pathB = fileD.toPath();
    pdf = new File("src/test/dataExamples/ExamplesForFileVisitor/d.pdf");
  }


  /**
   * Tests the method postVisitDirectory - tests that it returns continue.
   */
  @Test
  public void postVisitDirectoryTest() {
    assertEquals(visitor.postVisitDirectory(pathA, new IOException()), FileVisitResult.CONTINUE);
  }

  /**
   * Tests the method preVisitDirectory - tests that it returns continue
   */
  @Test
  public void preVisitDirectoryTest() {
    BasicFileAttributes attr1;

    try {
      attr1 = Files.readAttributes(fileA.toPath(), BasicFileAttributes.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    assertEquals(visitor.preVisitDirectory(pathA, attr1), FileVisitResult.CONTINUE);
  }

  /**
   * Tests the method visitFIle - tests that it returns continue,
   * and that it appropriately adds all markdown files to its array.
   */
  @Test
  public void visitFile() {
    BasicFileAttributes attr1;
    try {
      attr1 = Files.readAttributes(fileA.toPath(), BasicFileAttributes.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    // ends with md
    assertEquals(visitor.visitFile(pathA, attr1), FileVisitResult.CONTINUE);
    assertTrue(visitor.getMdFiles().contains(fileA));
    // does not end with md
    assertEquals(visitor.visitFile(pdf.toPath(), attr1), FileVisitResult.CONTINUE);
    assertFalse(visitor.getMdFiles().contains(pdf));
  }

  /**
   * Tests the method visitFileFailed, and that it will return contnue.
   */
  @Test
  public void visitFileFailed() {
    assertEquals(visitor.visitFileFailed(pathA, new IOException()), FileVisitResult.CONTINUE);
  }

  /**
   * Tests the method getMdFiles and that it returns the correct array of markdown
   * files. Tests that if getMdFiles is called before callback methods, thorws
   * an illegal state exception.
   */
  @Test
  public void getMdFiles() {
    // callBack false
    assertThrows(IllegalStateException.class, () -> visitor.getMdFiles(),
        "No callback methods have been called yet.");

    visitor.postVisitDirectory(pathA, new IOException());
    // callBack true
    assertEquals(visitor.getMdFiles(), visitor.getMdFiles());
  }
}


