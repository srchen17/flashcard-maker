package cs3500.pa01;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Comparator;


/**
 *  Has method that compares two files based on their creation times.
 */
public class CreatedComparator implements Comparator<File> {
  /**
   * Instantiates a CreatedComparator, which compares files based on creation time.
   */
  public CreatedComparator() {}

  /**
   * Compares two given files based on their creation times: returns a negative int if
   * the first was created before the second, zero if equal, and positive if created after.
   *
   * @param f1 the first object to be compared.
   * @param f2 the second object to be compared.
   * @return int negative if f1 was created before f2, 0 if the same, and positive if after.
   */
  @Override
  public int compare(File f1, File f2) {
    FileTime time1;
    FileTime time2;

    try {
      time1 = Files.readAttributes(Path.of(f1.getAbsolutePath()),
          BasicFileAttributes.class).creationTime();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    try {
      time2 = Files.readAttributes(Path.of(f2.getAbsolutePath()),
          BasicFileAttributes.class).creationTime();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return time1.compareTo(time2);
  }
}
