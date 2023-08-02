package cs3500.pa01;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Writes a study guide file from an array of study guide content
 * to the given output path.
 */
public class MdFileWriter {
  private final ArrayList<SgContent> sgContentToWrite;
  private String output;

  /**
   * Instantiates an MdFileWriter.
   *
   * @param sgContent  Study guide content extracted from a notes directory,
   *                  to be written in the study guide.
   * @param output  The output path for where the study guide file should be written.
   */
  public MdFileWriter(ArrayList<SgContent> sgContent, String output) {
    this.sgContentToWrite = sgContent;
    this.output = output;
  }

  /**
   * Writes a study guide file and a .sr file with study content
   */
  public void writeSgAndSrFiles() {
    ArrayList<SgContent> qnaContent = new ArrayList<>();
    ArrayList<SgContent> studyGuideContent = new ArrayList<>();
    for (SgContent s : this.sgContentToWrite) {
      if (s.format(false).contains(":::")) {
        qnaContent.add(s);
      } else {
        studyGuideContent.add(s);
      }
    }
    new MdFileWriter(qnaContent, this.output).writeStudyFile(".sr");
    new MdFileWriter(studyGuideContent, this.output).writeStudyFile(".md");
  }

  /**
   * Writes/creates a study files to an output path, with the file containing the formatted,
   * organized study guide content.
   */
  private void writeStudyFile(String suffix) {
    FileWriter writer;
    try {
      if (!output.endsWith(".md")) {
        output = output + ".md";
      }
      if (!(suffix.equals(".md"))) {
        writer = new FileWriter(output.substring(0, output.length() - 3) + suffix);
      } else {
        writer = new FileWriter(output);
      }
      if (!sgContentToWrite.isEmpty()) {
        writer.write(this.sgContentToWrite.get(0).format(true) + "\n");
        this.sgContentToWrite.remove(0);
      }
      for (SgContent c : this.sgContentToWrite) {
        writer.write(c.format(false) + "\n");
      }
      writer.close();
    } catch (IOException e) {
      System.err.println("Unable to write to study file. Path may be invalid.");
      throw new RuntimeException(e);
    }
  }
}
