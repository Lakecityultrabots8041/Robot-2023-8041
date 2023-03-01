package frc.robot.utils;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class Talon {
    private WPI_TalonSRX m_motor;
    private String m_moduleName;

    /** 
     * This class builds a new TalonSRX motor controller and resets it from factory default
     * @return TalonSRX object
     */
    public Talon(String name, TalonConstants moduleConstants, PIDGains moduleGains) {
        m_motor = new WPI_TalonSRX(moduleConstants.MotorID);
        m_moduleName = name;

        m_motor.configFactoryDefault();
        m_motor.set(ControlMode.PercentOutput, 0.0);
        m_motor.setNeutralMode(NeutralMode.Brake);
        m_motor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, 0, 30);
        m_motor.configNeutralDeadband(moduleGains.kNeutralDeadband);
        m_motor.setSensorPhase(moduleConstants.SensorPhase);
        m_motor.setInverted(moduleConstants.IsInverted);
        m_motor.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, 30);
        m_motor.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, 30);
        m_motor.configNominalOutputForward(0, 30);
        m_motor.configNominalOutputReverse(0, 30);
        m_motor.configPeakOutputForward(moduleGains.kPeakOutput, 30);
        m_motor.configPeakOutputReverse(-moduleGains.kPeakOutput, 30);
        m_motor.selectProfileSlot(0, 0); 
        m_motor.config_kF(0, moduleGains.kF, 30);
        m_motor.config_kP(0, moduleGains.kP, 30);
        m_motor.config_kI(0, moduleGains.kI, 30);
        m_motor.config_kD(0, moduleGains.kD, 30);
        m_motor.configMotionCruiseVelocity(moduleGains.kCruise, 30);
        m_motor.configMotionAcceleration(moduleGains.kAccel, 30);
        
        // m_motor.setSelectedSensorPosition(0); //reset position
    }

    /**
     * This function sets the desired percent output speed
     * @param speed (double) Percent output speed
     */
    public void setSpeed(double speed) {
        m_motor.set(ControlMode.PercentOutput, speed);
    }

    /**
     * This function sets the desired motionMagic target position
     * @param target (double) Target encoder position
     */
    public void setTarget(double target) {
        m_motor.set(ControlMode.MotionMagic, target);
    }

    public double getPosition() {
        return m_motor.getSelectedSensorPosition(0);
    }
   
    public String getModuleName() {
        return m_moduleName;
    }
}
