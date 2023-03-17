// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;


public class OI {

  public static double deadband(double value) { 
     double direction = Math.signum(value);
    value = (Math.abs(value) > Constants.OI.maxband) ? direction * 1.0 : value; //over max 
    value = (Math.abs(value) < Constants.OI.minband) ? direction * 0.35 : value; // less than less 
       return value;
}
  
  

}


  
