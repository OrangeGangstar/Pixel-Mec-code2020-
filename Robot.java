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

import com.revrobotics.ColorSensorV3; //http://revrobotics.com/content/sw/color-sensor-v3/sdk/REVColorSensorV3.json
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorMatch;

public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Blue_1";
  private static final String kCustomAuto = "Red_1";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  private static final int legTopLeft = 2;   //setting up wheel motors to their PMW ports on the RobotRIO
  private static final int legBottomLeft = 3;
  private static final int legTopRight = 1;
  private static final int legBottomRight = 0;
  private static final int SUCC = 4; //other motors for other robot task
  private static final int EXHALE = 5;
  private static final int mom = 6;
  private static final int SUCC1 = 7;
  private static final int EXHALE1 = 8;

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
  PWMVictorSPX DysonMotor = new P
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
    m_chooser.setDefaultOption("Forward only", kDefaultAuto);
    m_chooser.addOption("Back only", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);
    
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
    m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);

    clock.reset();
		clock.start();
  }

  /*
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
          if (clock.get() < 7.0) {
            MecPixel.driveCartesian(0.0, -0.6, 0.0,0); // drive forwards
            }
          else {
            MecPixel.driveCartesian(0.0, 0.0, 0.0, 0.0);
          }
        break;
      case kDefaultAuto:
      default:
          if (clock.get() < 7.0) {
            MecPixel.driveCartesian(0.0, 0.6, 0.0,0); // drive forwards
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
    String colorString = "c";
    ColorMatchResult match = reeves.matchClosestColor(pewach);

    ink colorPrint = new ink(pewach, colorString, match, BlueBoi, RedBoi, GreenBoi, YellowBoi);

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

    MecPixel.driveCartesian(xylophone.xJoy(), yylophone.yJoy(), zylophone.zJoy(), 0.0); //sets driving to run using  //joystick controls
                                                                                        //joystick controls
    FRICK.set(Shaquille.fThot());

    if(gStick.getRawButton(2) == true){
        DysonMotor.set(0.80);
    }
    else{
      DysonMotor.set(0.0);
    }

    if(gStick.getRawButton(1) == true){
        craftsmanBLOW.set(-1.0);
    }
    else if(gStick.getRawButton(3) == true){
        craftsmanBLOW.set(-0.5);
    }
    else{
        craftsmanBLOW.set(0.0);
    }

    if(gStick.getRawButton(9) == true){
      while(match.color != BlueBoi){
        pewach = chop.getColor();
        match = reeves.matchClosestColor(pewach);
        FRICK.set(0.5);
        SmartDashboard.putNumber("Red", pewach.red);
        SmartDashboard.putString("Detected Color", colorPrint.colorSplash());
        if(gStick.getRawButton(8) == true)
          break;
      }
    }
    else if(gStick.getRawButton(10) == true){
      while(match.color != RedBoi){
        pewach = chop.getColor();
        match = reeves.matchClosestColor(pewach);
        FRICK.set(0.5);   
        //colorPrint.colorSplash();
        if(gStick.getRawButton(8) == true)
          break;
      }
    }
    else if(gStick.getRawButton(11)){
      while(match.color != GreenBoi){
        pewach = chop.getColor();
        match = reeves.matchClosestColor(pewach);
        FRICK.set(0.5);   
        //colorPrint.colorSplash();
        if(gStick.getRawButton(8) == true)
          break;
      }
    }
    else if(gStick.getRawButton(12)){
      while(match.color != YellowBoi){
        pewach = chop.getColor();
        match = reeves.matchClosestColor(pewach);
        FRICK.set(0.5);   
        //colorPrint.colorSplash();
        if(gStick.getRawButton(8) == true)
          break;
      }
    }
    else{
    }

    Timer.delay(0.001);    //timer sets up the code to have a 1 millisecond delay to avoid overworking and 
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
    return (zCal * 0.8);
  }
  else if((zCal >= -0.3) && (zCal <= 0.0)){
    return 0.0;
  }
  else if(zCal < -0.3){
    return (zCal * 0.8);
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

class ink{
  public ink(Color c, String s, ColorMatchResult r, Color b1, Color r1, Color g1, Color y1){
    pewach = c;
    colorStr = s;
    match = r;
    bBoi = b1;
    rBoi = r1;
    gBoi = g1;
    yBoi = y1;
  }
  public String colorSplash(){
    if(match.color == bBoi){
      colorStr = "BLUE";
    }
    else if(match.color == rBoi){
      colorStr = "RED";
    }
    else if(match.color == gBoi){
      colorStr = "GREEN";
    }
    else if(match.color == yBoi){
      colorStr = "YELLOW";
    }
    else{
      colorStr = "UNKNOWN";
    }
    return colorStr;
  }
  public Color pewach;
  public String colorStr;
  public ColorMatchResult match;
  public Color bBoi;
  public Color rBoi;
  public Color gBoi;
  public Color yBoi;
}
