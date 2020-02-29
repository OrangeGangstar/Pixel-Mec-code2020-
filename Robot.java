package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;                      //All these imports are needed so the code can 
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;  //work with motors, joysticks, drive station, etc.
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PWMVictorSPX;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.Timer; 
import edu.wpi.first.cameraserver.*;
import edu.wpi.first.wpilibj.util.Color;

import com.revrobotics.ColorSensorV3;		//these import will not work without the rev robotics libraries that needed to be installed
import com.revrobotics.ColorMatchResult;	//to vscode and can be found on rev robotics color sensor page: http://www.revrobotics.com/rev-31-1557/
import com.revrobotics.ColorMatch;

public class Robot extends TimedRobot {
  private static final String Blue_1 = "Blue_1";
  private static final String Red_1 = "Red_1";
  private String AutoSelected;
  private final SendableChooser<String> AutoChooser = new SendableChooser<>();

  private static final int legTopLeft = 2;   //setting up wheel motors to their PMW ports on the RobotRIO
  private static final int legBottomLeft = 3;
  private static final int legTopRight = 1;
  private static final int legBottomRight = 0;
  private static final int SUCC = 4; //other motors for other robot task
  private static final int EXHALE = 5;
  private static final int mom = 6;

  private final I2C.Port i2cPort = I2C.Port.kOnboard;

  private static final int gamer = 0; //sets up joystick to connect to usb port 1 on the laptop/computer

  private static final double USonHoldDist = 12.0; //ultrasonic sensor limited before it stops the robot(in inches)
  private static final double MathValToDist = 0.125; //set value used to convert sensor values to inches
  private static final int UsonPort = 0; //ultrasonic analog port (aka Analog In on the RobotRIO)

  private final AnalogInput AUsonIn = new AnalogInput(UsonPort); //gives the ultrasonic sensor a name
  private MecanumDrive MecPixel; //gives the drive train a name
  private Joystick gStick;  //gives the joystick a name

  PWMVictorSPX TopL = new PWMVictorSPX(legTopLeft);       //sets PMW motor ports to a respective name 
  PWMVictorSPX BottomL = new PWMVictorSPX(legBottomLeft); //for the wheels
  PWMVictorSPX TopR = new PWMVictorSPX(legTopRight);
  PWMVictorSPX BottomR = new PWMVictorSPX(legBottomRight);
  PWMVictorSPX DysonMotor = new PWMVictorSPX(SUCC);
  PWMVictorSPX craftsmanBLOW = new PWMVictorSPX(EXHALE);
  PWMVictorSPX FRICK = new PWMVictorSPX(mom);

  private final ColorSensorV3 chop = new ColorSensorV3(i2cPort);
  private final ColorMatch reeves = new ColorMatch();

  private final Color BlueBoi = ColorMatch.makeColor(0.143, 0.427, 0.429);
  private final Color GreenBoi = ColorMatch.makeColor(0.197, 0.561, 0.240);
  private final Color RedBoi = ColorMatch.makeColor(0.561, 0.232, 0.114);
  private final Color YellowBoi = ColorMatch.makeColor(0.361, 0.524, 0.113);
  
  Timer clock = new Timer();

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    AutoChooser.setDefaultOption("Blue_1", Blue_1);
    AutoChooser.addOption("Red_1", Red_1);
    SmartDashboard.putData("Auto choices", AutoChooser);
    
    TopL.setInverted(false); //flips the left side of motors for wheels
    BottomL.setInverted(false);//false cause... not needed this year

    MecPixel = new MecanumDrive(TopL, BottomL, TopR, BottomR); //hooks up the drive train with the PMW motors
                                                               //that are linked to the wheels
    gStick = new Joystick(gamer); //hooks up joysick to the usb port that is connected to the joystick

    CameraServer.getInstance().startAutomaticCapture();
    CameraServer.getInstance().startAutomaticCapture();

    reeves.addColorMatch(BlueBoi);
    reeves.addColorMatch(GreenBoi);
    reeves.addColorMatch(RedBoi);
    reeves.addColorMatch(YellowBoi);
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
    double currentDist = AUsonIn.getValue() ;
    System.out.println(currentDist);
    //System.out.println("asfsdfsdfsd");
    //System.out.println(xylophone.xJoy());
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
    AutoSelected = AutoChooser.getSelected();
    AutoSelected = SmartDashboard.getString("Auto Selector", Blue_1);
    System.out.println("Auto selected: " + AutoSelected);

    clock.reset();
		clock.start();
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    switch (AutoSelected) {
      case Blue_1:
        // Put custom auto code here
        break;
      case Red_1:
      default:
          if (clock.get() < 10.0) {
            MecPixel.driveCartesian(0.0, 0.7, 0.0,0); // drive forwards half speed
            }
          else {
            MecPixel.driveCartesian(0.0, 0.0, 0.0, 0.0);
          }
        break;

    }
  }

  // This function is called periodically during operator control.
  @Override
  public void teleopPeriodic() {

    yValue yylophone = new yValue(gStick.getY());
    xValue xylophone = new xValue(gStick.getX());
    zValue zylophone = new zValue(gStick.getZ());

    Fricker Shaquille = new Fricker(gStick.getThrottle());

    Color pewach = chop.getColor();
    String colorString;
    ColorMatchResult match = reeves.matchClosestColor(pewach);

    if(match.color == BlueBoi){
      colorString = "BLUE";
    }
    else if(match.color == RedBoi){
      colorString = "RED";
    }
    else if(match.color == GreenBoi){
      colorString = "GREEN";
    }
    else if(match.color == YellowBoi){
      colorString = "YELLOW";
    }
    else{
      colorString = "UNKNOWN";
    }

    SmartDashboard.putNumber("Red", pewach.red);
    SmartDashboard.putNumber("Green", pewach.green);
    SmartDashboard.putNumber("Blue", pewach.blue);
    SmartDashboard.putNumber("Confidence", match.confidence);
    SmartDashboard.putString("Detected Color", colorString);

    MecPixel.driveCartesian(xylophone.xJoy(), yylophone.yJoy(), zylophone.zJoy(), 0.0); //sets driving to run using 
                                                                                        //joystick controls
    
    FRICK.set(Shaquille.fThot());


    if(gStick.getRawButton(2) == true){
        DysonMotor.set(0.80);
    }
    else{
      DysonMotor.set(0.0);
    }

    if(gStick.getRawButton(1) == true){
        craftsmanBLOW.set(0.7);
    }
    else if(gStick.getRawButton(3) == true){
        craftsmanBLOW.set(0.2);
    }
    else{
        craftsmanBLOW.set(0.0);
    }

    if(gStick.getRawButton(9) == true){
      while(match.color != BlueBoi){
        pewach = chop.getColor();
        match = reeves.matchClosestColor(pewach);
        FRICK.set(0.5);   
        if(match.color == BlueBoi){
          colorString = "BLUE";
        }
        else if(match.color == RedBoi){
          colorString = "RED";
        }
        else if(match.color == GreenBoi){
          colorString = "GREEN";
        }
        else if(match.color == YellowBoi){
          colorString = "YELLOW";
        }
        else{
          colorString = "UNKNOWN";
        }
        SmartDashboard.putString("Detected Color", colorString);
      }
    }
    else{
      FRICK.set(0.0);
    }

    Timer.delay(0.005);    //timer sets up the code to have a 1 millisecond delay to avoid overworking and 
  }                       //over heating the RobotRIO

   // This function is called periodically during test mode.
  @Override
  public void testPeriodic() {
  }
}

class yValue{
  public yValue(double y){
    yCal = y;
  }
  public double yJoy(){
    if((yCal <= 0.2) && (yCal >= 0.0)){
      return 0.0;
    }
    else if(yCal > 0.2){
      return -yCal;
    }
    else if((yCal >= -0.2) && (yCal <= 0.0)){
      return 0.0;
    }
    else if(yCal < -0.2){
      return -yCal;
    }
    else{
      return 0.0;
    }
  }
  public double yCal;
}

class xValue{
  public xValue(double x){
    xCal = x;
  }
public double xJoy(){
  if((xCal <= 0.2) && (xCal >= 0.0)){
    return 0.0;
  }
  else if(xCal > 0.2){
    return xCal;
  }
  else if((xCal >= -0.2) && (xCal <= 0.0)){
    return 0.0;
  }
  else if(xCal < -0.2){
    return xCal;
  }
  else{
    return 0.0;
  }
}
public double xCal;
}

class zValue{
  public zValue(double z){
    zCal = z;
  }
public double zJoy(){
  if((zCal <= 0.3) && (zCal >= 0.0)){
    return 0.0;
  }
  else if(zCal > 0.3){
    return zCal;
  }
  else if((zCal >= -0.3) && (zCal <= 0.0)){
    return 0.0;
  }
  else if(zCal < -0.3){
    return zCal;
  }
  else{
    return 0.0;
  }
}
public double zCal;
}

class Fricker{
  public Fricker(double f){
    fCal = f;
  }
  public double fThot(){
    if((fCal <= 0.4)&& (fCal >= 0.0)){
      return 0.0;
    }
    else if (fCal > 0.4){
      return -fCal;
    }
    else if((fCal >= -0.4) && (fCal <= 0.0)){
      return 0.0;
    }
    else if(fCal < -0.4){
      return -fCal;
    }
    else{
      return 0.0;
    }
  }
public double fCal;
}
