package edu.umn.ecology.populus.core;

import java.util.*;
import java.io.*;
import edu.umn.ecology.populus.constants.*;

//Title:        Populus
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Lars Roe
//Company:      University of Minnesota
//Description:  Populus


public final class PopPreferences {
   ///////////////////////
   // CONSTANTS DEFINED //
   ///////////////////////
   //Button Type Constants
   /** Button Type */
   public static final int IMAGES = 1;
   /** Button Type */
   public static final int TEXT = 2;
   /** Button Type */
   public static final int INVALID = -1;
   /** Button Type */
   public static final int DEFAULT_BUTTON_TYPE = IMAGES + TEXT;
   //BORDER CONSTANT
   public static final int DEFAULT_BORDER_THICKNESS = 5;
   public static final long DEFAULT_DELAY = 20;
   /** TERMINUS_TYPE */
   public static final int kARROWTERMINI = 1;
   /** TERMINUS_TYPE */
   public static final int kCIRCLETERMINI = 2;
   public static final int kDEFAULTTERMINI = kCIRCLETERMINI;
   //Trigger Type Constants
   /** Trigger Type */
   public static final int NO_TRIGGER = 210;
   /** Trigger Type */
   public static final int DEFAULT_TRIGGER = 211;
   /** Trigger Type */
   public static final int ALL_TRIGGER = 212;
   //TODO SAFE private static final String DEFAULT_DIRECTORY = System.getProperty( "user.home", "." );
   private static final String DEFAULT_DIRECTORY = "";
   private static final Integer BUTTON_TYPE        = new Integer( 100 );
   private static final Integer DIRECTORY          = new Integer( 101 );
   private static final Integer DELAY_TIME         = new Integer( 102 );
   private static final Integer TRIGGER_TYPE       = new Integer( 103 );
   private static final Integer USE_AWT_FILEDIALOG = new Integer( 104 );
   private static final Integer COLOR_SAVER        = new Integer( 105 );
   private static final Integer VALUE_SAVER        = new Integer( 106 );
   private static final Integer BORDER_THICKNESS   = new Integer( 107 );
   private static final Integer TERMINUS_TYPE      = new Integer( 108 );

   public static final Integer TOP_PACKETS = new Integer( 7 );
   public static final Integer SINGLE_PACKETS = new Integer( 8 );
   public static final Integer MULTI_PACKETS = new Integer( 9 );
   public static final Integer DISCRETE_PACKETS = new Integer( 10 );
   public static final Integer MENDEL_PACKETS = new Integer( 11 );
   public static final Integer SELECTION_PACKETS = new Integer( 12 );
   public static final Integer SPATIAL_PACKETS = new Integer( 13 );
   public static final Integer HOST_PACKETS = new Integer( 14 );
   public static final Integer RC_PACKETS = new Integer( 15 );
   public static final Integer QUANT_PACKETS = new Integer( 16 );
   public static final Integer AIDS_PACKETS = new Integer( 17 );

   //Reference to singleton
   private static PopPreferences singleton = null;

   //Instance data
   private Vector buttons;
   private Hashtable packetTable, table;

   private PopPreferences() {
      buttons = new Vector();
      initializeMenuPackets();
      subLoad(true);
   }

   public static synchronized void removeButton( PopulusToolButton b ) {
      getSingleton().buttons.removeElement( b );
   }

   public synchronized static PopPreferences getSingleton() {
      if (singleton == null)
         singleton = new PopPreferences();
      return singleton;
   }

   /** Lets PopulusToolButtons get notified when needed */
   public static void addButton( PopulusToolButton b ) {
      getSingleton().buttons.addElement(b);
   }

   // GETTERS
   private int safeLookup(Integer key, int defaultValue) {
      Integer i = (Integer) table.get(key);
      return (i == null) ? defaultValue : i.intValue();
   }
   private long safeLookup(Integer key, long defaultValue) {
      Long l = (Long) table.get(key);
      return (l == null) ? defaultValue : l.longValue();
   }
   private String safeLookup(Integer key, String defaultValue) {
      String s = (String) table.get(key);
      return (s == null) ? defaultValue : s;
   }

   public static int getButtonType() {
      return getSingleton().safeLookup(BUTTON_TYPE, DEFAULT_BUTTON_TYPE);
   }
   public static int getTriggerType() {
      return getSingleton().safeLookup(TRIGGER_TYPE, DEFAULT_TRIGGER);
   }

   public static String getDirectory() {
      return getSingleton().safeLookup(DIRECTORY, DEFAULT_DIRECTORY);
   }

   public static long getDelayTime() {
      return getSingleton().safeLookup(DELAY_TIME, DEFAULT_DELAY);
   }

   public static int getOwnershipBorderThickness() {
      return getSingleton().safeLookup(BORDER_THICKNESS, DEFAULT_BORDER_THICKNESS);
   }

   public static int getTerminusType() {
      return getSingleton().safeLookup(TERMINUS_TYPE, kDEFAULTTERMINI);
   }

   //SETTERS

   public static void setButtonType( int newType ) {
      getSingleton().table.put( BUTTON_TYPE, new Integer( newType ) );

      //tell buttons to update look.
      getSingleton().notifyButtons();
   }

   public static void setDelayTime( long newTime ) {
      getSingleton().table.put( DELAY_TIME, new Long( newTime ) );
   }

   public static void setUseAWTFileDialog( boolean newVal ) {
      getSingleton().table.put( USE_AWT_FILEDIALOG, new Boolean( newVal ) );
   }

   public static void setDirectory(String newFile) {
       if (new File(newFile).isDirectory())
           getSingleton().table.put(DIRECTORY, newFile);
   }

   public static void setOwnershipBorderThickness(int newThickness) {
       getSingleton().table.put(BORDER_THICKNESS, new Integer(newThickness));
   }

   public static void setTriggerType( int newVal ) {
      getSingleton().table.put( TRIGGER_TYPE, new Integer( newVal ) );
   }

   public static void setTerminusType(int newVal) {
      getSingleton().table.put(TERMINUS_TYPE, new Integer(newVal));
   }

   public static ModelPacket[] getModelPackets(Integer i) {
      return ( (ModelPacket[])getSingleton().packetTable.get( i ) );
   }

   public static boolean isUseAWTFileDialog() {
      return ( (Boolean)getSingleton().table.get( USE_AWT_FILEDIALOG ) ).booleanValue();
   }

   public static String getPreferencesFile() {
      String s = "";
      try {
    	  s = System.getProperty("user.home") + System.getProperty("file.separator");
      } catch (Exception e) {
    	  //TODO: log
      }
      s += "userpref.po";
      return s;
   }

   /**
    * Initializes the menus, or packetTable
    */
   private void initializeMenuPackets() {
      ModelPacket[] topModels, singleModels, multiModels, rcModels,
      discreteModels, hostModels, aidsModels, mendelianModels, selectionPopModels,
      quantitativeModels, spatialModels;

      topModels = new ModelPacket[] {
         new ModelPacket( edu.umn.ecology.populus.model.ie.IEModel.class ),
      };

      singleModels = new ModelPacket[] {
         new ModelPacket( edu.umn.ecology.populus.model.dig.DIGModel.class ),
         new ModelPacket( edu.umn.ecology.populus.model.lpg.LPGModel.class ),
         new ModelPacket( edu.umn.ecology.populus.model.aspg.ASPGModel.class ),
         new ModelPacket( edu.umn.ecology.populus.model.sspg.SSPGModel.class ),
      };

      multiModels = new ModelPacket[] {
         new ModelPacket( edu.umn.ecology.populus.model.lvc.LVCModel.class ),
         new ModelPacket( edu.umn.ecology.populus.model.lvpptl.LVPPTLModel.class ),
      };

      rcModels = new ModelPacket[] {
         new ModelPacket( edu.umn.ecology.populus.model.rc.RCModel.class ),
         new ModelPacket( edu.umn.ecology.populus.model.rct.RCTModel.class ),
      };

      discreteModels = new ModelPacket[] {
         new ModelPacket( edu.umn.ecology.populus.model.appdnb.APPDNBModel.class ),
         new ModelPacket( edu.umn.ecology.populus.model.nbss.NBSSModel.class ),
         new ModelPacket( edu.umn.ecology.populus.model.appdfr.APPDFRModel.class ),
         new ModelPacket( edu.umn.ecology.populus.model.appdnrs.APPDNRSModel.class ),
         new ModelPacket( edu.umn.ecology.populus.model.appdi.APPDIModel.class ),
         new ModelPacket( edu.umn.ecology.populus.model.appdtpr.APPDTPRModel.class ),
         new ModelPacket( edu.umn.ecology.populus.model.ihpi.IHPIModel.class ),
         new ModelPacket( edu.umn.ecology.populus.model.pp.PPModel.class ),
         new ModelPacket( edu.umn.ecology.populus.model.cp.CPModel.class ),
         new ModelPacket( edu.umn.ecology.populus.model.hph.HPHModel.class ),

      };

      hostModels = new ModelPacket[] {
         new ModelPacket( edu.umn.ecology.populus.model.imd.IMDModel.class ),
         new ModelPacket( edu.umn.ecology.populus.model.md.MDModel.class ),
         new ModelPacket( edu.umn.ecology.populus.model.eov.EOVModel.class ),
         new ModelPacket( edu.umn.ecology.populus.model.bp.BPModel.class ),
         new ModelPacket( edu.umn.ecology.populus.model.tp.TPModel.class ),
         new ModelPacket( edu.umn.ecology.populus.model.hhap.HHAPModel.class ),
      };
      aidsModels = new ModelPacket[] {
         new ModelPacket( edu.umn.ecology.populus.model.aidsbasic.AIDSBASICModel.class),
         new ModelPacket( edu.umn.ecology.populus.model.aids.AIDSModel.class ),
         new ModelPacket( edu.umn.ecology.populus.model.aidst.AIDSTModel.class ),
      };
      mendelianModels = new ModelPacket[] {
         new ModelPacket( edu.umn.ecology.populus.model.gdamcm.GDModel.class ),
         new ModelPacket( edu.umn.ecology.populus.model.inbreeding.InbreedingModel.class ),
         new ModelPacket( edu.umn.ecology.populus.model.ps.PSModel.class ),
         new ModelPacket( edu.umn.ecology.populus.model.das.DASModel.class )
      };

      selectionPopModels = new ModelPacket[] {
         new ModelPacket( edu.umn.ecology.populus.model.woozle.WoozleModel.class ),
         new ModelPacket( edu.umn.ecology.populus.model.sdal.SDALModel.class ),
         new ModelPacket( edu.umn.ecology.populus.model.soamal.SOAMALModel.class ),
         new ModelPacket( edu.umn.ecology.populus.model.sotl.SOTLModel.class ),
         new ModelPacket( edu.umn.ecology.populus.model.soasll.SOASLLModel.class ),
         new ModelPacket( edu.umn.ecology.populus.model.sam.SAMModel.class ),
         new ModelPacket( edu.umn.ecology.populus.model.interdgs.INTERDGSModel.class ),
         new ModelPacket( edu.umn.ecology.populus.model.intradgs.INTRADGSModel.class ),
         new ModelPacket( edu.umn.ecology.populus.model.kham.KHAMModel.class ),
         new ModelPacket( edu.umn.ecology.populus.model.hmss.HMSSModel.class ),
         new ModelPacket( edu.umn.ecology.populus.model.fdsdm.FDSDMModel.class ),
         new ModelPacket( edu.umn.ecology.populus.model.fdsess.FDSESSModel.class ),
         new ModelPacket( edu.umn.ecology.populus.model.ddsgv.DDSGVModel.class ),
      };

      quantitativeModels = new ModelPacket[] {
         new ModelPacket( edu.umn.ecology.populus.model.paqg.PAQGModel.class ),
         new ModelPacket( edu.umn.ecology.populus.model.herit.HERITModel.class ),
         new ModelPacket( edu.umn.ecology.populus.model.dsoqc.DSOQCModel.class )
      };

      spatialModels = new ModelPacket[] {
         new ModelPacket( edu.umn.ecology.populus.model.prm.PRMModel.class ),
         new ModelPacket( edu.umn.ecology.populus.model.sgfac.SGFACModel.class ),
         new ModelPacket( edu.umn.ecology.populus.model.mnp.MNPModel.class ),
         new ModelPacket( edu.umn.ecology.populus.model.sd.SDModel.class ),
      };

      packetTable = new Hashtable();
      packetTable.put( TOP_PACKETS, topModels );
      packetTable.put( SINGLE_PACKETS, singleModels );
      packetTable.put( MULTI_PACKETS, multiModels );
      packetTable.put( DISCRETE_PACKETS, discreteModels );
      packetTable.put( MENDEL_PACKETS, mendelianModels );
      packetTable.put( SELECTION_PACKETS, selectionPopModels );
      packetTable.put( SPATIAL_PACKETS, spatialModels );
      packetTable.put( QUANT_PACKETS, quantitativeModels );
      packetTable.put( HOST_PACKETS, hostModels );
      packetTable.put( RC_PACKETS, rcModels );
      packetTable.put( AIDS_PACKETS, aidsModels );

   }

   /**
     * Saves the Hashtable to file.
     */

   public synchronized void save() {
      table.put( COLOR_SAVER, new ColorSaver() );
      table.put( VALUE_SAVER, new ValuesToSave());
      try {
         ObjectOutputStream oos;
         oos = new ObjectOutputStream( new FileOutputStream(getPreferencesFile()) );
         oos.writeObject( table );
      }
      catch( FileNotFoundException e ) {
         e.printStackTrace();
      }
      catch( IOException e ) {
         e.printStackTrace();
      }
   }

   /**
     * Loads the Hashtable from file
     */
   public synchronized void load() {
      subLoad(false);
   }
   /**
     * Loads the Hashtable from file, called by load().
     * @param isInit When true, we don't compare with the existing data to see if we need to update
     *  anything, e.g. the button look.
     */
   private void subLoad(boolean isInit) {
      try {
         int oldButtonType = INVALID, newButtonType = INVALID;
         ObjectInputStream ois;
         if (!isInit) {
            oldButtonType = ( table != null ) ? getButtonType() : INVALID;
         }
         ois = new ObjectInputStream( new FileInputStream(getPreferencesFile()) );
         table = (Hashtable)ois.readObject();

         if (!isInit) {
            //check if button type changed (if so, tell buttons)
            newButtonType = getButtonType();
            if( ( oldButtonType != newButtonType ) && ( oldButtonType != INVALID ) ) {
               notifyButtons();
            }
         }
         ColorSaver cs = (ColorSaver)table.get( COLOR_SAVER );
         if( cs != null ) {
            cs.setColors();
         }
         ValuesToSave vs = (ValuesToSave)table.get( VALUE_SAVER );
         if( vs != null ) {
            vs.setValues();
         }
      }
      catch( Exception e ) {
    	  //TODO: Log exception
         reset(isInit);
      }
   }

   /**
     * Initiates the Hashtable and adds the standard set of values
     * @param isInit When true, we don't compare with the existing data to see if we need to update
     *  anything, e.g. the button look.
     */
    public void reset() { reset(false); }
    protected synchronized void reset(boolean isInit) {
      Hashtable tbl = table;
      int oldButtonType, newButtonType;
      oldButtonType = ( table != null && !isInit ) ? getButtonType() : INVALID;
      table = new Hashtable( );
      table.put( BUTTON_TYPE, new Integer( DEFAULT_BUTTON_TYPE ) );
      table.put( DIRECTORY, DEFAULT_DIRECTORY );
      table.put( DELAY_TIME, new Long( DEFAULT_DELAY ) );
      table.put( TRIGGER_TYPE, new Integer( DEFAULT_TRIGGER ) );
      table.put( USE_AWT_FILEDIALOG, new Boolean( false ) );
      ValuesToSave.setDefaults();
      ColorSaver.setDefaults();
      table.put( COLOR_SAVER, new ColorSaver() );
      table.put( VALUE_SAVER, new ValuesToSave() );
      table.put( BORDER_THICKNESS , new Integer(5));

      //check if button type changed (if so, tell buttons)
      if (!isInit) {
         newButtonType = getButtonType();
         if( ( oldButtonType != newButtonType ) && ( oldButtonType != INVALID ) ) {
            notifyButtons();
         }
      }
   }

   /**
    * Tells buttons that the look has changed.
    * Lars - This has a memory leak, since we always
    * keep a reference of all the buttons -- maybe even when their model is long gone.
    */
   private void notifyButtons() {
      PopulusToolButton button;
      Enumeration e = buttons.elements();
      //edu.umn.ecology.populus.fileio.Logging.log("There are " + buttons.size() + " buttons."); //Lars
      while( e.hasMoreElements() ) {
         button = (PopulusToolButton)e.nextElement();
         button.setLook();
      }
   }
}
