package cs3500.pa01;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.Comparator;


/**
 * Has method that compares two files based on their modified times.
 */
public class ModifiedComparator  implements Comparator<File> {

  /**
   * Instantiates a ModifiedComparator, which compares fles based on last modified time.
   */
  public ModifiedComparator() {}

  /**
   * Returns an integer based on whether the first given file was modified before the
   * second given file (negative if before, zero if same, positive if modified after).
   *
   * @param f1 the first object to be compared.
   * @param f2 the second object to be compared.
   * @return int  negative if f1 was modified before f2, 0 if the same, and positive if after.
   */
  @Override
  public int compare(File f1, File f2) {
    FileTime mod1;
    FileTime mod2;

    try {
      mod1 = Files.getLastModifiedTime(Path.of(f1.getAbsolutePath()));
      mod2 = Files.getLastModifiedTime(Path.of(f2.getAbsolutePath()));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return mod1.compareTo(mod2);
  }
}
