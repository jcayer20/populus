package edu.umn.ecology.populus.fileio;
import java.io.*;

/**
  * This type was created in VisualAge.
  * TODO: Is this used? Looks worthless...
  */

public class OutputFilter implements java.io.FilenameFilter {

   /**
     * accept method comment.
     */

   public boolean accept( File dir, String name ) {
      return true;
   }

   /**
     * OutputFilter constructor comment.
     */

   public OutputFilter() {
      super();
   }
}
