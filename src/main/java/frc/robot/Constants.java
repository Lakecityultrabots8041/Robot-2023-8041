// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

public class Constants {;
  
  public class IDs {
    //Ids of Victors 
    public class Victor {
      public static final int driveLeftrear = 1;
      public static final int driveLeftfront = 2;
      public static final int driveRightrear = 3;
      public static final int driveRightfront = 4;


    
    }
// Ids of SparkMax
  public class SparkMax  {
     public static final int driveTopLeft = 5;
     public static final int driveTopRight = 6; 
  }
 
    } //DriveTrain 
    public class DriveTrain {
      public static final double speedmult = 0.75;// slow down math boy 
      public class Left{
        public static final boolean isInverted = true; //invert left side
      } 
      public class Right {
        public static final boolean isInverted = true ;
      }
    
    //Operator Interface
   
  }
  public class OI{
    public static final int Joy = 0;
    public static final double maxband = 0.90;
    public static final double minband = 0.10;
  }}
  
  
    
  

  


