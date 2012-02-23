package edu.umn.ecology.populus.model.rct;
import edu.umn.ecology.populus.plot.*;
import edu.umn.ecology.populus.model.appd.APPD3DProtoParamInfo;
import edu.umn.ecology.populus.math.*;
import edu.umn.ecology.populus.constants.ColorScheme;
import java.awt.Color;
import java.util.*;


/** Possibly not needed.... */
public class RCT3DParamInfo extends APPD3DProtoParamInfo {
   public static final int NvsT = 1;
   public static final int NvsN = 2;
   protected double time;
   protected double n1, n2, n3, R1, R2, R3;
   protected int speciesNum = 3;
   protected int resourceNum;
   protected DiscreteProc discProc = null;
   protected String xCaption = null;
   protected String yCaption = null;
   protected int numVars;
   protected int plotType = 0;
   protected double[] initialConditions = null;
   protected String mainCaption = null;
   protected String zCaption = null;
   ResourceBundle res = ResourceBundle.getBundle( "edu.umn.ecology.populus.model.rct.Res" );
   Integrator ig = null;

   public BasicPlotInfo getBasicPlotInfo() {
      BasicPlotInfo bp;
      initialConditions = new double[3];
      initialConditions[0] = n1;
      initialConditions[1] = n2;
      initialConditions[2] = n3;
      int equations = initialConditions.length;
      double[][][] points = new double[1][3][];
      double[] xlist;
      double[][] ylists;
      int size, i;
      if( gens < 0 ) {
         ig.record.ss = true;
         ig.record.interval = false;
      }
      ig.integrate( initialConditions, 0.0, time );
      xlist = ig.getX();
      ylists = ig.getY();
      size = xlist.length;
      points[0][0] = ylists[0];
      points[0][1] = ylists[1];
      points[0][2] = ylists[2];
      bp = new BasicPlotInfo( points, mainCaption, xCaption, yCaption, zCaption );
      return bp;
   }

}