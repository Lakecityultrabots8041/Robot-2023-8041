package frc.robot.utils;

public class TalonConstants {
    //control
    public final int MotorID;
    public final boolean SensorPhase;
    public final boolean IsInverted;

    /**
     * Creates a new constants object for initializing a TalonSRX Motor Controller
     * @param MotorID - (int) CAN ID of the Motor (TalonSRX)
     * @param SensorPhase - (bool) true to invert the sensor phase
     * @param IsInverted - (bool) true to invert the motor control
     */
    public TalonConstants(int MotorID, boolean SensorPhase, boolean IsInverted) {
        this.MotorID = MotorID;
        this.SensorPhase = SensorPhase;
        this.IsInverted = IsInverted;
    }
}