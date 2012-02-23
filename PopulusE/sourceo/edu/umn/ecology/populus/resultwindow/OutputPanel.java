package edu.umn.ecology.populus.resultwindow;
import edu.umn.ecology.populus.plot.*;
import java.awt.print.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;

/**
  *  ModelPanel serves as a root for all other models in the 'edwin' directory
  *  Changes to this will affect all descendents.
  *
  *
  */

public class OutputPanel extends JPanel {
   protected transient Vector listeners = new Vector( 1, 1 );
   int type = 0;

   /** @see edu.umn.ecology.populus.constants.OutputTypes
    *
    * Lars - this doesn't seem like a good idea for clean software.  Will look into if/when I have time.
    */
   public int getType() {
      return type;
   }

   /** @see edu.umn.ecology.populus.constants.OutputTypes */
   public void setType( int nType ) {
      type = nType;
   }

   public OutputPanel() {
      super();
   }

   public void showOptions( int option ) {


   //Do nothing if not a plot model.
   }

   public synchronized void addOutputPanelListener( OutputPanelListener listener ) {
      listeners.addElement( listener );
   }

   public synchronized void removeOutputPanelListener( OutputPanelListener listener ) {
      listeners.removeElement( listener );
   }

   public void destroy() {

      //remove listeners
      listeners.removeAllElements();

   //give panel a chance to clean up

   //Make sure all paramfields have lost their connections to any frames

   //so that they can be lost, too
   }

   //////////

   // EVENT

   // METHODS

   //////////

   /**
     * Call this to easily notify an event
     */

   protected void fireOutputPanelEvent( int type ) {
      OutputPanelEvent event = new OutputPanelEvent( this, type );
      Enumeration e = listeners.elements();
      while( e.hasMoreElements() ) {
         ( (OutputPanelListener)e.nextElement() ).outputChangeRequested( event );
      }
   }

/*
public void saveGraphics() {
try {
java.awt.Dimension area = this.getSize();
java.io.FileOutputStream out = new java.io.FileOutputStream("modelout.jpg");
java.awt.image.BufferedImage bi = null;
bi = (java.awt.image.BufferedImage) this.createImage(area.width, area.height);
java.awt.Graphics g = bi.getGraphics();
this.paintAll(g);
com.sun.image.codec.jpeg.JPEGImageEncoder encoder
= com.sun.image.codec.jpeg.JPEGCodec.createJPEGEncoder(out);
encoder.encode(bi);
out.flush();
out.close();
edu.umn.ecology.populus.fileio.Logging.log("Succesfully created file");
}
catch (java.io.IOException ioe) { }
catch (java.awt.image.RasterFormatException except) { }
catch (Exception e) {
edu.umn.ecology.populus.fileio.Logging.log("Can't print because of");
e.printStackTrace();
}
}
*/
}
