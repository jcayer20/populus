
//Title:        Populus

//Version:      

//Copyright:    Copyright (c) 1999

//Author:       Lars Roe, under Don Alstad

//Company:      University of Minnesota

//Description:  First Attempt at using Java 1.2

//with Populus
package edu.umn.ecology.populus.visual;
import java.beans.*;

public class RunningTimePanelBeanInfo extends SimpleBeanInfo {
   String iconColor16x16Filename;
   String iconColor32x32Filename = "runningTimePanel32c.jpg";
   String iconMono16x16Filename;
   String iconMono32x32Filename;
   Class beanClass = RunningTimePanel.class;
   
   public PropertyDescriptor[] getPropertyDescriptors() {
      try {
         PropertyDescriptor _time = new PropertyDescriptor( "time", beanClass, "getTime", null );
         PropertyDescriptor _maxTime = new PropertyDescriptor( "maxTime", beanClass, "getMaxTime", "setMaxTime" );
         _maxTime.setDisplayName( "maxTime" );
         _maxTime.setShortDescription( "maxTime" );
         PropertyDescriptor _defaultTime = new PropertyDescriptor( "defaultTime", beanClass, "getDefaultTime", "setDefaultTime" );
         _defaultTime.setDisplayName( "defaultTime" );
         _defaultTime.setShortDescription( "defaultTime" );
         PropertyDescriptor _incrementTime = new PropertyDescriptor( "incrementTime", beanClass, "getIncrementTime", "setIncrementTime" );
         _incrementTime.setDisplayName( "incrementTime" );
         _incrementTime.setShortDescription( "incrementTime" );
         PropertyDescriptor[] pds = new PropertyDescriptor[] {
            _time, _maxTime, _defaultTime, _incrementTime,
         };
         return pds;
      }
      catch( IntrospectionException ex ) {
         ex.printStackTrace();
         return null;
      }
   }
   
   public java.awt.Image getIcon( int iconKind ) {
      switch( iconKind ) {
         case BeanInfo.ICON_COLOR_16x16:
             return iconColor16x16Filename != null ? loadImage( iconColor16x16Filename ) : null;
         
         case BeanInfo.ICON_COLOR_32x32:
             return iconColor32x32Filename != null ? loadImage( iconColor32x32Filename ) : null;
         
         case BeanInfo.ICON_MONO_16x16:
             return iconMono16x16Filename != null ? loadImage( iconMono16x16Filename ) : null;
         
         case BeanInfo.ICON_MONO_32x32:
             return iconMono32x32Filename != null ? loadImage( iconMono32x32Filename ) : null;
      }
      return null;
   }
   
   public RunningTimePanelBeanInfo() {
      
   }
}
