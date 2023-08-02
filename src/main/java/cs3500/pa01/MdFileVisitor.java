package cs3500.pa01;

import static java.nio.file.FileVisitResult.CONTINUE;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

/**
 * A visitor for a given file path that retrieves all
 * markdown files in that directory.
 */
public class MdFileVisitor implements FileVisitor<Path> {
  private final ArrayList<File> array = new ArrayList<>();
  private boolean callBack = false;

  /**
   * Instantiates a MdFileVisitor, to traverse a directory extracting markdown files.
   */
  public MdFileVisitor() {}

  /**
   * Handles what occurs after an entire directory is visited.
   *
   * @param dir the directory given by the file input path to extract the sg info from.
   *          a reference to the directory
   * @param exc
   *          {@code null} if the iteration of the directory completes without
   *          an error; otherwise the I/O exception that caused the iteration
   *          of the directory to complete prematurely
   *
   * @return  FileVisitResult, continue, which continues walking the directory.
   */
  @Override
  public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
    System.out.format("Completed Visiting Directory: %s%n", dir);
    this.callBack = true;
    return CONTINUE;
  }

  /**
   * Handles what occurs before an entire directory is visited.
   *
   * @param dir  the directory given by the file input path to extract the sg info from.
   *          a reference to the directory
   * @param attrs  the attributes of the directory containing notes.
   *          the directory's basic attributes
   *
   * @return  FileVisitResult, continue, which continues walking the directory.
   */
  @Override
  public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
    System.out.format("Starting to Visit Directory: %s%n", dir);
    this.callBack = true;
    return CONTINUE;
  }

  /**
   * Handles what occurs when a notes file is visited: adds it to an
   * array if it is a markdown file.
   *
   * @param file  the current notes file that is being visited
   *          a reference to the file
   * @param attrs  the notes file's attributes
   *          the file's basic attributes
   *
   * @return FileVisitResult, continue, which continues walking the directory.
   */
  @Override
  public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
    if (file.getFileName().toString().endsWith(".md")) {
      this.array.add(file.toFile());
    }
    this.callBack = true;
    return CONTINUE;
  }

  /**
   * Handles what occurs if a visit to a file fails.
   *
   * @param file  the current notes file that attempting to be visited
   *          a reference to the file
   * @param exc
   *          the I/O exception that prevented the file from being visited
   *
   * @return FileVisitResult, continue, which continues walking the directory.
   */
  @Override
  public FileVisitResult visitFileFailed(Path file, IOException exc) {
    exc.printStackTrace();
    System.err.println("Visit file failed.");
    this.callBack = true;
    return CONTINUE;
  }

  /**
   * Returns an array of markdown files extracted from the given input path directory.
   *
   * @return ArrayList of File  The array of markdown files extracted from the notes directory.
   */
  public ArrayList<File> getMdFiles() {
    if (this.callBack) {
      return this.array;
    } else {
      throw new IllegalStateException("No callback methods have been called yet.");
    }
  }
}
