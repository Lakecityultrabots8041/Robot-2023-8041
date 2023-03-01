// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

//import frc.robot.utils.PIDGains;
//import frc.robot.utils.TalonConstants;

public class Constants {;
  
  public class IDs {
    //Ids of Victors 
    public class Victor {
      public static final int driveLeftrearId = 1;
      public static final int driveLeftfrontId = 2;
      public static final int driveRightrearId = 3;
      public static final int driveRightfrontId = 4;
      public static final int armControllerId2 = 8;
    }
  
    // Ids of TalonSRX
    public class TalonSRX  {
      public static final int driveTopLeftId = 5;
      public static final int driveTopRightId = 6; 
      public static final int armControllerId1 = 7;
      public static final int armControllerId2 = 8;
      public static final int armExtendControllerId = 9;
    }

    //Solenoids
    public class Air {
      public static final int driveShiftHighId = 0;
      public static final int driveShiftLowId = 1;
      public static final int gripperSolenoidId = 2;
    }
 
    } 
    
    //DriveTrain 
    public class DriveTrain {
      public static final double kSpeedMult = 1.0; // slow down math boy 
      public static final double kTurnMult = 1.0; //reduce rate of turn
      public class Left{
        public static final boolean isInverted = true; //invert left side
      } 
      public class Right {
        public static final boolean isInverted = !Left.isInverted;
      }
  }

  //Gripper Constants
  public class Gripper {
    public static final boolean kOpenState = true; //state of the 
  }

  public class Arm {
    public class Positions { 
      public static final double ScoreHigh = 5000;
      public static final double ScoreLow = 1000;
      public static final double HPShelf = 3000;
      public static final double Floor = 1000;
      public static final double Home = 0;
    }
    public class Motor1 {
      //Talon Tuning
      public static final int motorId = IDs.TalonSRX.armControllerId1;
      public static final boolean isInverted = true;
      public static final boolean sensorPhase = false;
      // public final TalonConstants constants = new TalonConstants(motorId, sensorPhase, isInverted);
      //PID Tuning
      public static final double kP = 0.2;
      public static final double kI = 0.0;
      public static final double kD = 0.0;
      public static final double kF = 0.2;
      public static final int kIZone = 0;
      public static final double kNeutralDeadband = 0.001;
      public static final double kPeakOutput = 1.0;
      public static final double kCruise = 1000;
      public static final double kAccel = 2000;
      // public final PIDGains gains = new PIDGains(kP, kI, kD, kF, kIZone, kNeutralDeadband, kPeakOutput, kCruise, kAccel);
    }      
    public class Motor2 {
      //Talon Tuning
      public static final int motorId = IDs.TalonSRX.armControllerId2;
      public static final boolean isInverted = true;
      public static final boolean sensorPhase = false;
      // public final TalonConstants constants = new TalonConstants(motorId, sensorPhase, isInverted);
      //PID Tuning
      public static final double kP = 0.2;
      public static final double kI = 0.0;
      public static final double kD = 0.0;
      public static final double kF = 0.2;
      public static final int kIZone = 0;
      public static final double kNeutralDeadband = 0.001;
      public static final double kPeakOutput = 1.0;
      public static final double kCruise = 1000;
      public static final double kAccel = 2000;
      // public final PIDGains gains = new PIDGains(kP, kI, kD, kF, kIZone, kNeutralDeadband, kPeakOutput, kCruise, kAccel);
    }   
    public class Extend {
      public static final int motorId = IDs.TalonSRX.armExtendControllerId;
      public static final boolean isInverted = true;
      public static final boolean sensorPhase = false;
      // public final TalonConstants constants = new TalonConstants(motorId, sensorPhase, isInverted);
      //PID Tuning
      public static final double kP = 0.2;
      public static final double kI = 0.0;
      public static final double kD = 0.0;
      public static final double kF = 0.2;
      public static final int kIZone = 0;
      public static final double kNeutralDeadband = 0.001;
      public static final double kPeakOutput = 1.0;
      public static final double kCruise = 1000;
      public static final double kAccel = 2000;
      // public final PIDGains gains = new PIDGains(kP, kI, kD, kF, kIZone, kNeutralDeadband, kPeakOutput, kCruise, kAccel);
      public class Positions { 
        public static final double ScoreHigh = 5000;
        public static final double ScoreLow = 1000;
        public static final double HPShelf = 3000;
        public static final double Floor = 1000;
        public static final double Home = 0;
      }
    }         
  }

  //Operator Interface
  public class OI {
    public static final int Joy = 0;
    public static final double maxband = 0.90;
    public static final double minband = 0.0;
  } 

  //Logitech Extreme 3d pro
  public class Joy {
    public static final int axis_horizontal = 0;
    public static final int axis_vertical = 1;
    public static final int axis_rotate = 2;
    public static final int axis_slider = 3;
    public static final int trigger_button = 1;
    public static final int side_button = 2;
    public static final int thumb3 = 3; //bottom-left
    public static final int thumb4= 4; //bottom-right
    public static final int thumb5 = 5; //top-left
    public static final int thumb6 = 6; //top-right
    public static final int lefthand7 = 7; //top-left base buttons
    public static final int lefthand8 = 8; //top-right base buttons
  } 

  public class Controls {
    public static final int driveShiftHigh = Joy.lefthand7;
    public static final int driveShiftLow = Joy.lefthand8;
    public static final int gripperActuate = Joy.trigger_button;
    public static final int armDown = Joy.side_button;
    public static final int armScoreHigh = Joy.thumb5;
    public static final int armScoreLow = Joy.thumb3;
    public static final int armHPShelf = Joy.thumb6;
    public static final int armFloor = Joy.thumb4;
  }

}

  
  
    
  

  


