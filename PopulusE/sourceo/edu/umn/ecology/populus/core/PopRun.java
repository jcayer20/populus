package edu.umn.ecology.populus.core;
import edu.umn.ecology.populus.core.DesktopWindow;
import java.awt.*;
import java.awt.event.*;
import javax.swing.UIManager;
import java.io.IOException;
import java.util.*;

public class PopRun {
   ResourceBundle res = ResourceBundle.getBundle( "edu.umn.ecology.populus.core.Res" );
   boolean packFrame = false;
   private DesktopWindow dw;

   public static void main( String argv[] ) {
      new PopRun();
   }

   protected void finalize() throws Throwable {
      edu.umn.ecology.populus.fileio.Logging.log( res.getString( "Populus_Started" ) );
   }

   PopRun() {
      edu.umn.ecology.populus.fileio.Logging.log( res.getString( "Populus_Starting_" ) +" on "+System.getProperty("os.name"));
      String buildTime = "<Unable to read time file>";
      try {
         //Read timestamp.txt from current directory.
         String timeFile = "timestamp.txt";
         java.io.BufferedReader ri = new java.io.BufferedReader(new java.io.FileReader(timeFile));
         buildTime = ri.readLine();
      }
      catch (java.io.FileNotFoundException whateverHappensInMinnesotaStaysInMinnesota) { }
      catch (java.io.IOException whateverHappensInMinnesotaStaysInMinnesota) { }
      edu.umn.ecology.populus.fileio.Logging.log( "Populus built " + buildTime + "\n");
      try {
         UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
         Object[] localUIDefaults1 = new String[] {
            "StyledRadioButtonUI", "edu.umn.ecology.populus.visual.StyledRadioButtonUI"
         };
         UIManager.getLookAndFeelDefaults().putDefaults( localUIDefaults1 );
         Object[] localUIDefaults2 = new String[] {
            "BracketUI", "edu.umn.ecology.populus.visual.BracketUI"
         };
         System.out.print( "." );
         UIManager.getLookAndFeelDefaults().putDefaults( localUIDefaults2 );
         dw = new DesktopWindow();
      }
      catch( Exception e ) {
         e.printStackTrace();
         //Try to log to file
         try {
            String fileName = System.getProperty("user.home", ".") + System.getProperty("file.separator")
                  + "PopulusErrorLog" + System.currentTimeMillis() + ".txt";
            java.io.PrintWriter w = new java.io.PrintWriter(new java.io.FileOutputStream(fileName));
            w.write("!Fatal error in Populus!\n");
            w.write("Populus built " + buildTime + "\n");
            w.write("Date: " + new Date() + "\n\n");
            e.printStackTrace(w);
         } catch (java.io.FileNotFoundException fnfe) {
            edu.umn.ecology.populus.fileio.Logging.log("Couldn't write error to log file");
         }
      }
   }
}

