package cs3500.pa01;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Examples and tests for the class MdFileReader.
 */
public class MdFileReaderTest {
  File fileA;

  ArrayList<File> sortedFiles;

  MdFileReader reader;
  Scanner fileAscanner;

  /**
   * Sets up examples before each test.
   */
  @BeforeEach
  public void setup() {
    fileA = new File("src/test/dataExamples/fileReaderData/A.md");
    sortedFiles = new ArrayList<>(List.of(fileA));
    reader = new MdFileReader(sortedFiles);
    fileAscanner = new Scanner(fileA.getAbsolutePath());
  }

  /**
   * Tests edge cases for what a heading may look like: seven #, or
   * a # in the middle of a line.
   */
  @Test
  public void containsHeadingTest() {
    File headings = new File("src/test/dataExamples/HeadingEdgeCases/headings.md");
    ArrayList<File> sortedFiles1 = new ArrayList<>(List.of(headings));
    ArrayList<SgContent> headingResults =
        new ArrayList<>(Arrays.asList(
            new HeadingContent("# "),
            new HeadingContent("# h1"),
            new HeadingContent("## h1"),
            new HeadingContent("### h1"),
            new HeadingContent("#### h1"),
            new HeadingContent("##### h1"),
            new HeadingContent("###### h1")));


    MdFileReader readHeadings = new MdFileReader(sortedFiles1);
    assertEquals(readHeadings.readContent().get(0).content,
        headingResults.get(0).content);
    assertEquals(readHeadings.readContent().get(1).content,
        headingResults.get(1).content);
    assertEquals(readHeadings.readContent().get(2).content,
        headingResults.get(2).content);
    assertEquals(readHeadings.readContent().get(4).content,
        headingResults.get(4).content);
    assertEquals(readHeadings.readContent().get(5).content,
        headingResults.get(5).content);

  }


  /**
   * Tests the method getRestImportantInfo. Tests that it can retrieve
   * all important information, even if it starts after bracketed info
   * ends.
   */
  @Test
  public void getRestImportantInfoTest() {
    ArrayList<SgContent> results =
        new ArrayList<>(Arrays.asList(
            new ImportantContent("[[unfinished line here!]]"),
            new ImportantContent("[[hello! hello!]]"),
            new ImportantContent("[[hi!]]"),
            new ImportantContent("[[hey!]]"),
            new ImportantContent("[[yes hello there]]")
        ));

    ArrayList<File> sortedFiles = new ArrayList<>(
        List.of(
            new File("src/test/dataExamples/UnfinishedBracketsExample/unfinishedBrackets.MD")));

    MdFileReader notesReader = new MdFileReader(sortedFiles);

    assertEquals(notesReader.readContent().get(0).content,
        results.get(0).content);
    assertEquals(notesReader.readContent().get(1).content,
        results.get(1).content);
    assertEquals(notesReader.readContent().get(2).content,
        results.get(2).content);

  }

  /**
   * Tests that readContent correctly
   * reads headers and important info combined in one doc.
   * Test edge cases for headings.
   */
  @Test
  public void readContentTest() {

    ArrayList<SgContent> results =
        new ArrayList<>(Arrays.asList(
            new HeadingContent("# Java Arrays"),
            new ImportantContent("[[An **array** is a collection of variables of the same type]]"),
            new HeadingContent("# Vectors"),
            new ImportantContent("[[Vectors act like resizable arrays]]")
        ));

    ArrayList<File> sortedFiles = new ArrayList<>(
        Arrays.asList(
          new File("src/test/dataExamples/ArrayVectorNotes2/Arrays.md"),
          new File("src/test/dataExamples/ArrayVectorNotes2/Vectors.md")));

    MdFileReader notesReader = new MdFileReader(sortedFiles);
    assertEquals(notesReader.readContent().get(0).content,
        results.get(0).content);
    assertEquals(notesReader.readContent().get(1).content,
        results.get(1).content);
    assertEquals(notesReader.readContent().get(2).content,
        results.get(2).content);
    assertEquals(notesReader.readContent().get(3).content,
        results.get(3).content);

    results = new ArrayList<>(List.of());
    sortedFiles = new ArrayList<>(List.of(
        new File("src/test/dataExamples/nothingHere.md")));

    notesReader = new MdFileReader(sortedFiles);
    assertEquals(results, notesReader.readContent());
  }

  /**
   * Tests that when given an invalid path, the method readContent
   * throws an IllegalArgumentException.
   */
  @Test
  public void readContentExceptionTest() {

    ArrayList<File> sortedFiles = new ArrayList<>(
        Arrays.asList(
            new File("src/test/dataExamples/pa1Example/DoesntExist.md"),
            new File("src/test/dataExamples/pa1Example/Vectors.md")));
    MdFileReader invalidReader = new MdFileReader(sortedFiles);
    assertThrows(IllegalArgumentException.class,
        invalidReader:: readContent, "Scanner cold not be created.");
  }

  /**
   * Test that the file reader can extract q and info from a file with readContent
   */
  @Test
  public void getQnaInfoTest() {
    File aristotle = new File("src/test/dataExamples/QnaSampleQuestions/aristotle.md");
    File confucius = new File("src/test/dataExamples/QnaSampleQuestions/confucius.md");

    MdFileReader readQuestions = new MdFileReader(new ArrayList<>(
        Arrays.asList(aristotle, confucius)));
    HeadingContent aristotleHeading = new HeadingContent("## All About Aristotle");
    ImportantContent aristotleImp1 = new ImportantContent("[[He was a Greek philosopher]]");
    QnaContent aristotleQ1 = new QnaContent("[[What was his idea of happiness? ::: Eudaimonia]]");
    QnaContent aristotleQ2 = new QnaContent(
        "[[What is one of Aristotle's most famous works? ::: Nichomachean Ethics]]");
    HeadingContent confuciusHeading = new HeadingContent("## All About Confucius");
    ImportantContent confuciusImp1 = new ImportantContent(
        "[[ Confucius thought filial piety was very important.]]");
    QnaContent confuciusQ1 = new QnaContent("[[ What are some Confucian virtues?"
        + " ::: Filial piety, wisdom, following the rites.]]");
    QnaContent confuciusQ2 = new QnaContent(
        "[[ What does Confucius tell Upright Gong? "
            + "::: That he should not have turned in his father.]]");

    ArrayList<SgContent> expected = new ArrayList<>(Arrays.asList(
        aristotleHeading, aristotleImp1, aristotleQ1, aristotleQ2,
        confuciusHeading, confuciusImp1, confuciusQ1, confuciusQ2));

    for (SgContent s : readQuestions.readContent()) {
      System.out.println(s.content);
    }

    for (SgContent s : expected) {
      System.out.println(s.content);
    }


    assertEquals(readQuestions.readContent().get(0).format(false),
        expected.get(0).format(false));
    assertEquals(readQuestions.readContent().get(1).format(false),
        expected.get(1).format(false));
    assertEquals(readQuestions.readContent().get(2).format(false),
        expected.get(2).format(false));
    assertEquals(readQuestions.readContent().get(3).format(false),
        expected.get(3).format(false));
    assertEquals(readQuestions.readContent().get(4).format(false),
        expected.get(4).format(false));
    assertEquals(readQuestions.readContent().get(5).format(false),
        expected.get(5).format(false));
    assertEquals(readQuestions.readContent().get(6).format(false),
        expected.get(6).format(false));
    assertEquals(readQuestions.readContent().get(7).format(false),
        expected.get(7).format(false));
  }
}


