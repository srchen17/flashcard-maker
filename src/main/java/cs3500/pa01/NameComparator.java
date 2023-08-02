package cs3500.pa01;

import java.io.File;
import java.util.Comparator;

/**
 * Holds method to compare two file names alphabetically.
 */
public class NameComparator  implements Comparator<File> {
  /**
   * Instantiates a NameComparator, which compares by name.
   */
  public NameComparator() {}

  /**
   * Compares two given files by their names: if the first file's name comes alphabetically before
   * the second, returns a negative int; if they are the same, returns zero; if after,
   * returns a positive int.
   *
   * @param f1 the first object to be compared.
   * @param f2 the second object to be compared.
   * @return int  (-) if f1's name comes alphabetically before f2, 0 if the same, and (+) if after.
   */
  @Override
  public int compare(File f1, File f2) {
    String name1 = f1.getName();
    String name2 = f2.getName();
    return name1.compareTo(name2);
  }
}

