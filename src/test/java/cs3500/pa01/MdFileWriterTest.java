package cs3500.pa01;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests and examples for the class MdFileWriter.
 */
public class MdFileWriterTest {
  HeadingContent heading1;
  HeadingContent heading2;
  ImportantContent important1;
  ImportantContent important2;
  ArrayList<SgContent> sgArray;
  MdFileWriter validWriter;
  String directoryPath;

  /**
   * Sets up initial conditions before each test.
   */
  @BeforeEach
  public void setup() {
    heading1 = new HeadingContent("# This is heading 1");
    heading2 = new HeadingContent("# This is heading 2");
    important1 = new ImportantContent("[[ important content! ]]");
    important2 = new ImportantContent("[[ important content 2! ]]");
    directoryPath = "src/test/dataExamples/mdTest1";

    sgArray = new ArrayList<>(Arrays.asList(
        heading1, important1, heading2, important2));
    validWriter = new MdFileWriter(sgArray,
        "src/test/dataExamples/fileWriterData/writeHere");
  }

  /**
   * Tests that if given an invalid file path, writeStudyFile throws a runtime exception.
   */
  @Test
  public void fileWriterExceptionTest() {
    MdFileWriter invalid = new MdFileWriter(sgArray,  "/src/totallyInvalid");
    assertThrows(RuntimeException.class, invalid::writeSgAndSrFiles);
    try {
      Files.deleteIfExists(Path.of("/src/totallyInvalid.sr"));
      Files.deleteIfExists(Path.of("/src/totallyInvalid.md"));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Tests that if writeStudyFile is called to write from notes with no content,
   * will not write anything in the study guide.
   */

  @Test
  public void writeNoContentTest() {
    sgArray = new ArrayList<>();
    File noContentSg;
    File emptyFile;
    byte[] emptyFileArray;
    byte[] noContentSgArray;

    try {
      noContentSg = File.createTempFile("temporary", ".md");
      emptyFile = File.createTempFile("nothingHere", ".md");
      emptyFileArray = Files.readAllBytes(emptyFile.toPath());
      noContentSgArray = Files.readAllBytes(noContentSg.toPath());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    MdFileWriter emptyWriter = new MdFileWriter(sgArray,
        noContentSg.getAbsolutePath());
    emptyWriter.writeSgAndSrFiles();
    assertArrayEquals(emptyFileArray, noContentSgArray);
  }

  /**
   * Tests that with a valid file writer, writeStudyFile will write all given
   * content in order, with headings and important info formatted correctly.
   */

  @Test
  public void validFileWriterTest() {
    sgArray = new ArrayList<>(Arrays.asList(heading1, important1, heading2, important2));
    // creates new file
    File newSgFile;
    File resultFile;
    byte[] resultArray;
    byte[] newSgContent;

    try {
      resultFile = File.createTempFile("result", ".md");
      MdFileWriter newSgWriter = new MdFileWriter(sgArray,
          "src/test/dataExamples/ExamplesForFileWriter/new.md");
      newSgWriter.writeSgAndSrFiles();
      newSgFile = new File("src/test/dataExamples/ExamplesForFileWriter/new.md");
      resultArray = Files.readAllBytes(resultFile.toPath());
      newSgContent = Files.readAllBytes(newSgFile.toPath());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    assertFalse(Arrays.equals(resultArray, newSgContent));

    try {
      FileWriter resultWriter = new FileWriter(resultFile);
      resultWriter.write("# This is heading 1" + "\n");
      resultWriter.write("-  important content! " + "\n");
      resultWriter.write("\n# This is heading 2\n");
      resultWriter.write("-  important content 2! " + "\n");
      resultWriter.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    try {
      resultArray = Files.readAllBytes(resultFile.toPath());
      newSgContent = Files.readAllBytes(newSgFile.toPath());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    assertArrayEquals(resultArray, newSgContent);

    try {
      Files.delete(new File("src/test/dataExamples/ExamplesForFileWriter/new.md").toPath());
      Files.deleteIfExists(resultFile.toPath());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    // writes to existing file
    File tempEmpty;
    File writeToEmpty;
    byte[] emptyArray;
    byte[] writeToEmptyArray;

    try {
      tempEmpty = File.createTempFile("empty", ".md");
      writeToEmpty = File.createTempFile("empty2", ".md");
      emptyArray = Files.readAllBytes(tempEmpty.toPath());
      writeToEmptyArray = Files.readAllBytes(writeToEmpty.toPath());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    assertArrayEquals(emptyArray, writeToEmptyArray);
    new MdFileWriter(sgArray, writeToEmpty.getAbsolutePath())
        .writeSgAndSrFiles();
  }

  /**
   * Test that writeStudyFile creates appropriate .sr files
   */
  @Test
  public void testWriteStudyFileSr() {
    QnaContent hardAddition = new QnaContent("[[2 + 2 ::: 4]]");
    QnaContent easyAddition = new QnaContent("[[3 + 3 ::: 6]]", QuestionDifficulty.EASY);
    QnaContent mathWithPunctuation = new QnaContent("[[ Is (5+2) the same as [5 + 2]? ::: Yes! ]]");

    QnaContent longStringWithSpaces = new QnaContent(
        "[[  Why does Kierkegaard say Abraham experiences distress? "
            + "       :::       "
            + "He says, Abraham is silent, but cannot speak, "
            + "therein lies the distress and anguish.\" "
            + "(Because he cannot make himself understood.) ]]", QuestionDifficulty.HARD);
    ArrayList<SgContent> questionArray = new ArrayList<>(
        Arrays.asList(hardAddition, easyAddition, mathWithPunctuation, longStringWithSpaces));
    MdFileWriter mathQuestions = new MdFileWriter(questionArray,
        "src/test/dataExamples/ExamplesForWriteStudyFile/mathQuestionsHere.md");
    mathQuestions.writeSgAndSrFiles();

    File mathQuestionsFile = new File(
        "src/test/dataExamples/ExamplesForWriteStudyFile/mathQuestionsHere.sr");
    File mathQuestionsExpected = new File(
        "src/test/dataExamples/ExamplesForWriteStudyFile/mathQuestionsExpected");
    byte[] mathContent;
    byte[] mathExpectedContent;

    try {
      mathContent = Files.readAllBytes(mathQuestionsFile.toPath());
      mathExpectedContent = Files.readAllBytes(mathQuestionsExpected.toPath());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    assertArrayEquals(mathContent, mathExpectedContent);
  }
}
