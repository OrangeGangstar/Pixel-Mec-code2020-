package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;                      //All these imports are needed so the code can 
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;  //work with motors, joysticks, drive station, etc.
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PWMVictorSPX;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Timer;

public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  private static final int legTopLeft = 2;   //setting up wheel motors to their PMW ports on the RobotRIO
  private static final int legBottomLeft = 3;
  private static final int legTopRight = 1;
  private static final int legBottomRight = 0;

  private static final int gamer = 0; //sets up joystick to connect to usb port 1 on the laptop/computer

  private static final double USonHoldDist = 12.0; //ultrasonic sensor limited before it stops the robot(in inches)
  private static final double MathValToDist = 0.125; //set value used to convert sensor values to inches
  private static final double speedConst = 0.05; //set constant speed value for robot using ultrasonic
  private static final int UsonPort = 0; //ultrasonic analog port (aka Analog In on the RobotRIO)

  private final AnalogInput AUsonIn = new AnalogInput(UsonPort); //gives the ultrasonic sensor a name
  private MecanumDrive MecPixel; //gives the drive train a name
  private Joystick gStick;  //gives the joystick a name

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    PWMVictorSPX TopL = new PWMVictorSPX(legTopLeft);       //sets PMW motor ports to a respective name 
    PWMVictorSPX BottomL = new PWMVictorSPX(legBottomLeft); //for the wheels
    PWMVictorSPX TopR = new PWMVictorSPX(legTopRight);
    PWMVictorSPX BottomR = new PWMVictorSPX(legBottomRight);

    TopL.setInverted(true); //flips the left side of motors for wheels
    BottomL.setInverted(true);

    MecPixel = new MecanumDrive(TopL, BottomL, TopR, BottomR); //hooks up the drive train with the PMW motors
                                                               //that are linked to the wheels
    gStick = new Joystick(gamer); //hooks up joysick to the usb port that is connected to the joystick
  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to
   * the switch structure below with additional strings. If using the
   * SendableChooser make sure to add them to the chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }


  // This function is called periodically during operator control.
  @Override
  public void teleopPeriodic() {

    double currentDist = AUsonIn.getValue() *MathValToDist;       //ultrasonic sensor converting values to inches
    double UsonSpeed = (USonHoldDist - currentDist) * speedConst; //converting inches to a speed

    MecPixel.driveCartesian(gStick.getY() , gStick.getX(), gStick.getZ(), 0.0); //sets driving to run using 
                                                                                //joystick controls
    System.out.println(currentDist);

    Timer.delay(0.01);    //timer sets up the code to have a 1 millisecond delay to avoid overworking and 
  }                       //over heating the RobotRIO

   // This function is called periodically during test mode.
  @Override
  public void testPeriodic() {
  }
}
