package cs3500.pa01;

import java.io.File;
import java.util.ArrayList;

/**
 * Sorts an array of markdown files according to a certain ordering flag.
 */
public class MdFileSorter {
  private final String flag;
  private final ArrayList<File> mdFiles;

  /**
   * Enumeration for the possible ordering flags that might order the study guide content.
   */
  public enum OrderingFlag {
    filename,
    created,
    modified
  }

  /**
   * Instantiates an MdFileSorter, which will sort an array of markdown files by a flag.
   *
   * @param flag  The given ordering flag, as a String
   * @param mdFiles The array of markdown files to be sorted
   */
  public MdFileSorter(String flag, ArrayList<File> mdFiles) {
    this.flag = flag;
    this.mdFiles = mdFiles;
  }

  /**
   * Sorts an array of markdown files by an ordering flag.
   *
   * @return  ArrayList of File, the sorted list of markdown files.
   */
  public ArrayList<File> sortByFlag() {
    if (flag.equals(OrderingFlag.filename.toString())) {
      this.mdFiles.sort(new NameComparator());
    } else {
      if (flag.equals(OrderingFlag.modified.toString())) {
        this.mdFiles.sort(new ModifiedComparator());
      } else {
        if (flag.equals(OrderingFlag.created.toString())) {
          this.mdFiles.sort(new CreatedComparator());
        } else {
          throw new IllegalArgumentException("2nd arg must be filename, created, or modified");
        }
      }
    }
    return this.mdFiles;
  }
}
