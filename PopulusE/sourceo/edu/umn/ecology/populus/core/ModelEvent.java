package edu.umn.ecology.populus.core;

/**
  * This type was created in VisualAge.
  */

public class ModelEvent extends java.util.EventObject {
   public static final int ACTIVATED = 2;
   public static final int OUTPUT_UPDATE_BEGIN = 4;
   public static final int OUTPUT_UPDATE_END = 8;
   public static final int KILL = 1;
   private int type = 0;
   
   /**
     * ModelEvent constructor comment.
     * @param source java.lang.Object
     */
   
   public ModelEvent( Model source, int type ) {
      super( source );
      setType( type );
   }
   
   /**
     * @return edu.umn.ecology.populus.core.Model
     */
   
   public Model getModel() {
      return (Model)getSource();
   }
   
   /**
     * @return int
     */
   
   public int getType() {
      return type;
   }
   
   /**
     * ModelEvent constructor comment.
     * @param source java.lang.Object
     */
   
   public ModelEvent( Model source ) {
      super( source );
   }
   
   /**
     * @param newValue int
     */
   
   private void setType( int newValue ) {
      this.type = newValue;
   }
}
