// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.UsbCamera;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();
  private final WPI_VictorSPX driveLeftRear = new WPI_VictorSPX(Constants.IDs.Victor.driveLeftrearId);
  private final WPI_VictorSPX driveLeftFront = new WPI_VictorSPX(Constants.IDs.Victor.driveLeftfrontId);
  private final WPI_TalonSRX driveTopLeft = new WPI_TalonSRX(Constants.IDs.TalonSRX.driveTopLeftId);
  private final WPI_TalonSRX driveTopRight = new WPI_TalonSRX(Constants.IDs.TalonSRX.driveTopRightId);
  private final WPI_VictorSPX driveRightRear = new WPI_VictorSPX(Constants.IDs.Victor.driveRightrearId);
  private final WPI_VictorSPX driveRightFront = new WPI_VictorSPX(Constants.IDs.Victor.driveRightfrontId);
  private final Joystick joy = new Joystick(Constants.OI.Joy);
  private final AHRS m_gyro = new AHRS(SPI.Port.kMXP);
  private final DifferentialDrive drive = new DifferentialDrive(driveTopLeft, driveTopRight);
  private final DoubleSolenoid m_gearShift = new DoubleSolenoid(PneumaticsModuleType.REVPH, Constants.IDs.Air.driveShiftHighId, Constants.IDs.Air.driveShiftLowId);
  private final DoubleSolenoid m_gripper = new DoubleSolenoid(PneumaticsModuleType.REVPH, Constants.IDs.Air.gripperOpenSolenoidId, Constants.IDs.Air.gripperClosedSolenoidId);
  private final WPI_TalonFX m_arm1 = new WPI_TalonFX(Constants.IDs.TalonFX.armControllerId1);
  private final WPI_TalonFX m_arm2 = new WPI_TalonFX(Constants.IDs.TalonFX.armControllerId2);
  private final WPI_TalonSRX m_extend = new WPI_TalonSRX(Constants.IDs.TalonSRX.armExtendControllerId);
  private final Timer m_timer = new Timer();
  private final Timer m_armTimer = new Timer();
  private final Timer m_armDownTimer = new Timer();
  private final Timer m_extendTimer = new Timer();
  private double m_extendTarget = 0.0;
  private double m_extendDelay = 0.0;
  private double m_armDownTarget = 0.0;
  private double m_armDownDelay = 1.0;
  
  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    //Setup the Talons every robot init
    LiveWindow.disableAllTelemetry();
    UsbCamera camera = CameraServer.startAutomaticCapture();
    camera.setResolution(320, 240);

    //Left Master
    driveTopLeft.configFactoryDefault();
    driveTopLeft.setInverted(Constants.DriveTrain.Left.isInverted);
    //Left Followers
    driveLeftFront.configFactoryDefault();
    driveLeftFront.follow(driveTopLeft);
    driveLeftFront.setInverted(InvertType.FollowMaster);
    driveLeftRear.configFactoryDefault();
    driveLeftRear.follow(driveTopLeft);
    driveLeftRear.setInverted(InvertType.FollowMaster);

    //Right Master
    driveTopRight.configFactoryDefault();
    driveTopRight.setInverted(Constants.DriveTrain.Right.isInverted);
    //Right Followers
    driveRightFront.configFactoryDefault();
    driveRightFront.follow(driveTopRight);
    driveRightFront.setInverted(InvertType.FollowMaster);
    driveRightRear.configFactoryDefault();
    driveRightRear.follow(driveTopRight);
    driveRightRear.setInverted(InvertType.FollowMaster);

    //Arm Master
    m_arm1.setSelectedSensorPosition(0);
    m_arm1.configFactoryDefault();
    m_arm1.set(ControlMode.PercentOutput, 0.0);
    m_arm1.setNeutralMode(NeutralMode.Brake);
    m_arm1.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, 0, 30);
    m_arm1.configNeutralDeadband(Constants.Arm.Motor1.kNeutralDeadband);
    m_arm1.setSensorPhase(Constants.Arm.Motor1.sensorPhase);
    m_arm1.setInverted(Constants.Arm.Motor1.isInverted);
    m_arm1.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, 30);
    m_arm1.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, 30);
    m_arm1.configNominalOutputForward(0, 30);
    m_arm1.configNominalOutputReverse(0, 30);
    m_arm1.configPeakOutputForward(Constants.Arm.Motor1.kPeakOutput, 30);
    m_arm1.configPeakOutputReverse(-Constants.Arm.Motor1.kPeakOutput, 30);
    m_arm1.selectProfileSlot(0, 0); 
    m_arm1.config_kF(0, Constants.Arm.Motor1.kF, 30);
    m_arm1.config_kP(0, Constants.Arm.Motor1.kP, 30);
    m_arm1.config_kI(0, Constants.Arm.Motor1.kI, 30);
    m_arm1.config_kD(0, Constants.Arm.Motor1.kD, 30);
    m_arm1.configMotionCruiseVelocity(Constants.Arm.Motor1.kCruise, 30);
    m_arm1.configMotionAcceleration(Constants.Arm.Motor1.kAccel, 30);
    //Arm Follower
    m_arm2.configFactoryDefault();
    m_arm2.follow(m_arm1);
    m_arm2.setInverted((Constants.Arm.Motor2.isInverted)?InvertType.OpposeMaster:InvertType.FollowMaster);
    //Arm Extension 
    m_extend.configFactoryDefault();
    m_extend.configForwardSoftLimitEnable(true);
    m_extend.configForwardSoftLimitThreshold(Constants.Arm.Extend.Positions.SoftFwdLimit);
    m_extend.setSelectedSensorPosition(0);
    m_extend.set(ControlMode.PercentOutput, 0.0);
    m_extend.setNeutralMode(NeutralMode.Brake);
    m_extend.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, 0, 30);
    m_extend.configNeutralDeadband(Constants.Arm.Extend.kNeutralDeadband);
    m_extend.setSensorPhase(Constants.Arm.Extend.sensorPhase);
    m_extend.setInverted(Constants.Arm.Extend.isInverted);
    m_extend.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, 30);
    m_extend.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, 30);
    m_extend.configNominalOutputForward(0, 30);
    m_extend.configNominalOutputReverse(0, 30);
    m_extend.configPeakOutputForward(Constants.Arm.Extend.kPeakOutput, 30);
    m_extend.configPeakOutputReverse(-Constants.Arm.Extend.kPeakOutput, 30);
    m_extend.selectProfileSlot(0, 0); 
    m_extend.config_kF(0, Constants.Arm.Extend.kF, 30);
    m_extend.config_kP(0, Constants.Arm.Extend.kP, 30);
    m_extend.config_kI(0, Constants.Arm.Extend.kI, 30);
    m_extend.config_kD(0, Constants.Arm.Extend.kD, 30);
    m_extend.configMotionCruiseVelocity(Constants.Arm.Extend.kCruise, 30);
    m_extend.configMotionAcceleration(Constants.Arm.Extend.kAccel, 30);
    m_extend.setSelectedSensorPosition(0);

    m_gearShift.set(DoubleSolenoid.Value.kForward);

    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);
  }

  /**
   * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    SmartDashboard.putNumber("Gyro",m_gyro.getFusedHeading());
    // SmartDashboard.putBoolean("High Gear",m_gearShift.getFwdChannel());
    SmartDashboard.putNumber("Arm Position",m_arm1.getSensorCollection().getIntegratedSensorPosition());
    SmartDashboard.putNumber("Extend Position",m_extend.getSelectedSensorPosition());
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_timer.reset();
    m_timer.start();
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        if (Math.abs(m_gyro.getPitch())>=3.5) {
          double speed = Math.copySign(0.2,m_gyro.getPitch());
          drive.tankDrive(speed,speed);
        }
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {
    m_arm1.setSelectedSensorPosition(0); //TODO: DISABLE THIS FOR COMP!!! TESTING ONLY
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() { 
    drive.arcadeDrive(
      OI.deadband(joy.getRawAxis(Constants.Xbox.RightJoystick)) * Constants.DriveTrain.kSpeedMult,
      OI.deadband(joy.getRawAxis(Constants.Xbox.LeftJoyStick)) * Constants.DriveTrain.kTurnMult 
    );
    
    
    

    //Handle shifting the drivetrain
    if(joy.getRawButtonPressed(Constants.Controls.driveShiftHigh)) {
      System.out.println("teleopPeriodic: Shifting gear to High -- HIT IT CHEWWWY");
      m_gearShift.set(DoubleSolenoid.Value.kForward);
    } else if (joy.getRawButtonPressed(Constants.Controls.driveShiftLow)) {
      System.out.println("teleopPeriodic: Shifting gear to Low -- I am STRONK");
      m_gearShift.set(DoubleSolenoid.Value.kReverse);
    }

    //Handle opening/closing the gripper
    if(joy.getRawButtonPressed(Constants.Controls.gripperActuate)) {
      System.out.println("teleopPeriodic: Open Gripper");
      m_gripper.set(DoubleSolenoid.Value.kForward);
    } else if (joy.getRawButtonReleased(Constants.Controls.gripperActuate)) {
      System.out.println("teleopPeriodic: Close Gripper");
      m_gripper.set(DoubleSolenoid.Value.kReverse);
    }

    //Handle moving the arm
    if(m_armTimer.get()>=1.5) {
      m_arm1.set(ControlMode.Disabled, 0.0);
      m_armTimer.stop();
      m_armTimer.reset();
    }
    if(m_armDownTimer.get()>=m_armDownDelay) {
      m_arm1.set(ControlMode.MotionMagic, m_armDownTarget);
      m_armTimer.reset();
      m_armTimer.start();
      m_armDownTimer.stop();
      m_armDownTimer.reset();
    }
    if(m_extendTimer.get()>=m_extendDelay) {
      m_extend.set(ControlMode.MotionMagic, m_extendTarget);
      m_extendTimer.stop();
      m_extendTimer.reset();
    }
    if(joy.getRawButtonPressed(Constants.Controls.armDown)) {
      System.out.println("teleopPeriodic: Arm Down");
      // m_arm1.set(ControlMode.MotionMagic, Constants.Arm.Positions.Home);
      m_extend.set(ControlMode.MotionMagic, Constants.Arm.Extend.Positions.Home);
      // m_arm1.set(ControlMode.MotionMagic, Constants.Arm.Positions.Floor);
      m_armDownTarget = Constants.Arm.Extend.Positions.Home;
      m_armDownDelay = 1.0;
      m_armDownTimer.reset();
      m_armDownTimer.start();
    } else if (joy.getRawButtonPressed(Constants.Controls.armScoreHigh)) {
      System.out.println("teleopPeriodic: Scoring High");
      m_arm1.set(ControlMode.MotionMagic, Constants.Arm.Positions.ScoreHigh);
      m_extendTarget = Constants.Arm.Extend.Positions.ScoreHigh;
      m_extendDelay = 1.0;
      m_extendTimer.reset();
      m_extendTimer.start();
      // m_extend.set(ControlMode.MotionMagic, Constants.Arm.Extend.Positions.ScoreHigh);
    } else if(joy.getRawButtonPressed(Constants.Controls.armScoreLow)) {
      System.out.println("teleopPeriodic: Scoring Low");
      m_arm1.set(ControlMode.MotionMagic, Constants.Arm.Positions.ScoreLow);
      m_extendTarget = Constants.Arm.Extend.Positions.ScoreLow;
      m_extendDelay = 1.0;
      m_extendTimer.reset();
      m_extendTimer.start();
      // m_extend.set(ControlMode.MotionMagic, Constants.Arm.Extend.Positions.ScoreLow);
    } else if(joy.getRawButtonPressed(Constants.Controls.armFloor)) {
      System.out.println("teleopPeriodic: Arm To The Floor");
      m_arm1.set(ControlMode.MotionMagic, Constants.Arm.Positions.Floor);
      m_extendTarget = Constants.Arm.Extend.Positions.Floor;
      m_extendDelay = 1.0;
      m_extendTimer.reset();
      m_extendTimer.start();
      // m_extend.set(ControlMode.MotionMagic, Constants.Arm.Extend.Positions.Floor);
    } else if (joy.getRawButtonPressed(Constants.Controls.armHPShelf)) {
      System.out.println("teleopPeriodic: FEED ME HUMAN");
      m_arm1.set(ControlMode.MotionMagic, Constants.Arm.Positions.HPShelf);
      m_extendTarget = Constants.Arm.Extend.Positions.HPShelf;
      m_extendDelay = 1.0;
      m_extendTimer.reset();
      m_extendTimer.start();
      // m_extend.set(ControlMode.MotionMagic, Constants.Arm.Extend.Positions.HPShelf);
    }
   
    
  }
  
  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {
    m_gearShift.set(DoubleSolenoid.Value.kForward);
    m_gripper.set(DoubleSolenoid.Value.kReverse);
    m_arm1.set(ControlMode.MotionMagic, Constants.Arm.Positions.Home);
    m_extend.set(ControlMode.MotionMagic, Constants.Arm.Extend.Positions.Home);
  }

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {}

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {}

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {}
}
