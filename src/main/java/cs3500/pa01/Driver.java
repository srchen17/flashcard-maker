package cs3500.pa01;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * This is the main driver of the study guide creator project.
 */
public class Driver {

  /**
   * The main method writes a study guide file to the given output path
   * with all headings and important info in the given file input path,
   * ordered by the given flag.
   *
   * @param args 3 arguments: input path, ordering flag, output path.
   * @throws IllegalArgumentException if not given 3 inputs.
   * @throws IllegalArgumentException if walkFileTree receives an invalid path.
   */
  public static void main(String[] args) {
    String input;
    String flag;
    String output;
    if (args.length == 3) {
      input = args[0];
      flag = args[1];
      output = args[2];
      MdFileVisitor visitor = new MdFileVisitor();
      try {
        Files.walkFileTree(Path.of(input), visitor);
      } catch (IOException e) {
        System.err.println("Invalid file path.");
        throw new IllegalArgumentException(e);
      }
      MdFileSorter sorter = new MdFileSorter(flag, visitor.getMdFiles());
      ArrayList<File> sortedFiles = sorter.sortByFlag();
      new MdFileWriter(new MdFileReader(sortedFiles).readContent(), output)
          .writeSgAndSrFiles();
    } else {
      if (args.length == 0) {
        Readable userInput = new InputStreamReader(System.in);
        new StudySessionControl(userInput, System.out).run();
      } else {
        throw new IllegalArgumentException("Must input 3 or 0 arguments.");
      }
    }
  }
}