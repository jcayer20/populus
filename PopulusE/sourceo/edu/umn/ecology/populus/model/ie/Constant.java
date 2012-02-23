package edu.umn.ecology.populus.model.ie;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import edu.umn.ecology.populus.visual.ppfield.*;
import com.borland.jbcl.layout.*;
import edu.umn.ecology.populus.visual.HTMLLabel;

public class Constant extends JPanel {
   JButton closeButton = new JButton();
   String name;
   PopulusParameterField constantPPF = new PopulusParameterField();
   
   public Constant() {
      try {
         jbInit();
      }
      catch( Exception e ) {
         e.printStackTrace();
      }
   }
   
   public String getName() {
      return name;
   }
   
   public double getValue() {
      return constantPPF.getDouble();
   }
   
   public Constant( String name, double value ) {
      this();
      this.name = name;
      constantPPF.setParameterName( "<i>" + name + "</i>" );
      constantPPF.setCurrentValue( value );
   }
   
   void closeButton_actionPerformed( ActionEvent e ) {
      Container c = getParent();
      c.remove( this );
      c.validate();
      c.repaint();
   }
   
   private void jbInit() throws Exception {
      constantPPF.setIncrementAmount( 0.1 );
      constantPPF.setMaxValue( 1000.0 );
      constantPPF.setMinValue( -1000.0 );
      constantPPF.setParameterName( "a" );
      closeButton.setFont( new java.awt.Font( "Dialog", 1, 12 ) );
      closeButton.setPreferredSize( new Dimension( 14, 15 ) );
      closeButton.setToolTipText( "Remove this constant" );
      closeButton.setMargin( new Insets( 0, 0, 0, 0 ) );
      closeButton.setText( "X" );
      closeButton.addActionListener( new java.awt.event.ActionListener()  {
         
         public void actionPerformed( ActionEvent e ) {
            closeButton_actionPerformed( e );
         }
      } );
      this.add( constantPPF, null );
      this.add( closeButton, null );
   }
}
