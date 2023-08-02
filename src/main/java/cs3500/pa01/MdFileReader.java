package cs3500.pa01;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A class which reads an array of sorted markdown files and converts
 * its relevant information into an array of study guide content.
 */
public class MdFileReader {
  private final ArrayList<File> sortedMdFiles;

  /**
   * Instantiates a markdown file reader and interpreter.
   *
   * @param mdFiles a sorted array of markdown files.
   */
  public MdFileReader(ArrayList<File> mdFiles) {
    this.sortedMdFiles = mdFiles;
  }

  /**
   * Is the given String a heading?
   *
   * @param line  the line in a markdown file to be read.
   * @return  boolean  whether the line is a heading
   */
  private boolean containsHeading(String line) {
    Pattern hashtags = Pattern.compile("^#{1,6} ");
    Matcher hashtagMatcher = hashtags.matcher(line);
    return hashtagMatcher.find() && !(line.startsWith("####### "));
  }

  /**
   * Retrieves all important (bracketed) info and qna info starting in the
   * given line and returns it as an array list.
   *
   * @param line String representing a line of the file being read.
   * @param scanner  Scanner of the file being read.
   * @return  ArrayList of SgContent, containing important and qna info starting in the given line.
   */
  private ArrayList<SgContent> getStudyContent(String line, Scanner scanner) {
    ArrayList<SgContent> importantInfo = new ArrayList<>();
    Pattern brackets = Pattern.compile("\\[\\[.*?]]");
    Matcher bracketMatcher = brackets.matcher(line);
    String leftoverLine = line;
    while (bracketMatcher.find()) {
      importantInfo.add(this.extractStudyContent(bracketMatcher.group()));
      if (bracketMatcher.end() < leftoverLine.length()) {
        leftoverLine = line.substring(bracketMatcher.end() + 1);
      }
    }
    if (leftoverLine.contains("[[") && !leftoverLine.contains("]]")) {
      importantInfo.addAll(this.getRestStudyContent(leftoverLine, scanner));
    }
    return importantInfo;
  }

  /**
   * Based on a given string, return the string converted into a
   * form of study guide content (either important or qna)
   */
  private SgContent extractStudyContent(String string) {
    if (string.contains(":::")) {
      return new QnaContent(string);
    } else {
      return new ImportantContent(string);
    }
  }

  /**
   * Given a fragment of important info ( [[...), searches for the
   * rest of it and retrieves the rest of the important info from the file.
   *
   * @param leftoverLine  Fragment of unfinished important info
   * @param scanner  Scanner of the file of the fragment
   * @return  Array list of SgContent, important info retrieved from rest of file
   *
   */
  private ArrayList<SgContent> getRestStudyContent(String leftoverLine, Scanner scanner) {
    int bracketLength = 2;
    ArrayList<SgContent> importantInfo = new ArrayList<>();
    boolean foundEnd = false;
    String important = leftoverLine.substring(leftoverLine.indexOf("[["));
    String leftover = "";

    while (!foundEnd && scanner.hasNextLine()) {
      String next = scanner.nextLine();
      if (next.contains("]]")) {
        //System.out.println(next);
        important = important + next.substring(0, next.indexOf("]]") + bracketLength);
        importantInfo.add(extractStudyContent(important));
        foundEnd = true;
        if (next.length() > (next.indexOf("]]") + bracketLength)) {
          leftover = next.substring(next.indexOf("]]") + bracketLength);
        }
      } else {
        important = important + next;
      }
    }
    importantInfo.addAll(this.getStudyContent(leftover, scanner));
    return importantInfo;
  }

  /**
   * Retrieves all headings and important info in the array of files sortedMdFiles
   * and returns it as an array of StudyGuideContent.
   *
   * @return ArrayList of SgContent, an array list of all content to be written into the sg.
   *
   */
  public ArrayList<SgContent> readContent() {
    ArrayList<SgContent> studyGuide = new ArrayList<>();
    for (File file : this.sortedMdFiles) {
      Scanner scanner;
      try {
        scanner = new Scanner(file.toPath());
      } catch (IOException e) {
        System.err.println("Scanner could not be created.");
        throw new IllegalArgumentException(e);
      }
      while (scanner.hasNextLine()) {
        String next = scanner.nextLine();
        if (containsHeading(next)) {
          studyGuide.add(new HeadingContent(next));
        } else {
          studyGuide.addAll(this.getStudyContent(next, scanner));
        }
      }
    }
    return studyGuide;
  }
}
