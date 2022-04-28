package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive; 
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import edu.wpi.first.wpilibj.motorcontrol.PWMVictorSPX;
import edu.wpi.first.wpilibj.motorcontrol.PWMTalonSRX;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.I2C;

import java.time.Clock;

import edu.wpi.first.cameraserver.*;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DigitalInput;

public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  //decalring intigers (PWM ports) for motor controllers
  private static final int legTopLeft = 2;   //setting up wheel motors to their PMW ports on the RobotRIO
  private static final int legBottomLeft = 3;
  private static final int legTopRight = 1;
  private static final int legBottomRight = 0;
  private static final int SUCC = 6; //other motors for other robot task
  private static final int EXHALE = 7;
  private static final int LIFT = 4;
  private static final int DROP = 5;

  //declaring integers (DIO ports) for sensors
  private static final int HALT = 0;
  private static final int CEASE = 1;

  int replay = 0;
  double topSpin = 0;
  double output;
  private double startTime;

  private final I2C.Port i2cPort = I2C.Port.kOnboard;

  private static final int gamer = 0; //sets up joystick to connect to usb port 1 on the laptop/computer

  private static final double USonHoldDist = 12.0; //ultrasonic sensor limited before it stops the robot(in inches)
  private static final double MathValToDist = 0.125; //set value used to convert sensor values to inches
  private static final int UsonPort = 0; //ultrasonic analog port (aka Analog In on the RobotRIO)

  private final AnalogInput AUsonIn = new AnalogInput(UsonPort); //gives the ultrasonic sensor a name
  private DifferentialDrive DifOrange; //gives the drive train a name
  private Joystick gStick;  //gives the joystick a name

  MotorController m_frontLeft = new PWMVictorSPX(legTopLeft);//2
  MotorController m_backLeft = new PWMTalonSRX(legBottomLeft);//3
  MotorControllerGroup m_left = new MotorControllerGroup(m_frontLeft, m_backLeft);
  MotorController m_frontRight = new PWMVictorSPX(legTopRight);//1
  MotorController m_backRight = new PWMTalonSRX(legBottomRight);//0
  MotorControllerGroup m_right = new MotorControllerGroup(m_frontRight, m_backRight);
  PWMSparkMax  DysonMotor = new PWMSparkMax(SUCC); //6
  PWMTalonSRX craftsmanBLOW = new PWMTalonSRX(EXHALE); //5
  PWMSparkMax  DojaCat = new PWMSparkMax(LIFT);//4
  PWMVictorSPX MeganTheeStallion = new PWMVictorSPX(DROP);//7

   /*
  private final ColorSensorV3 chop = new ColorSensorV3(i2cPort);
  private final ColorMatch reeves = new ColorMatch();
  
  private fi89al Color BlueBoi = ColorMatch.makeColor(0.143, 0.427, 0.429);
  private final Color GreenBoi = ColorMatch.makeColor(0.197, 0.561, 0.240);
  private final Color RedBoi = ColorMatch.makeColor(0.561, 0.232, 0.114);
  private final Color YellowBoi = ColorMatch.makeColor(0.361, 0.524, 0.113);
  */
  DigitalInput DomGoth = new DigitalInput(HALT);
  DigitalInput SubFemboy= new DigitalInput(CEASE);

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Forward only", kDefaultAuto);
    m_chooser.addOption("Back shooter", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);
    
    CameraServer.startAutomaticCapture().setResolution(240,180);

    m_frontRight.setInverted(true);
    m_backRight.setInverted(true);
    m_frontLeft.setInverted(false); //flips the left side of motors for wheels
    m_backLeft.setInverted(false);//false cause... not needed this year
 
    DifOrange = new DifferentialDrive(m_left, m_right); //hooks up the drive train with the PMW motors
                                                 
    //that are linked to the wheels
      DifOrange.setExpiration(0.1);                           
    gStick = new Joystick(gamer); //hooks up joysick to the usb port that is connected to the joystick
    /*
    reeves.addColorMatch(BlueBoi);
    reeves.addColorMatch(GreenBoi);
    reeves.addColorMatch(RedBoi);
    reeves.addColorMatch(YellowBoi);
    /*
  
  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  }
  @Override
  public void robotPeriodic(){
    DifOrange.setSafetyEnabled(true);}

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString line to get the  auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to
   * the switch structure below with additional strings. If using the
   * SendableChooser make sure to add them to the chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    startTime = Timer.getFPGATimestamp();

     
  }

  /*
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    double time = Timer.getFPGATimestamp();
    
             if(time - startTime < 1){
              m_frontLeft.set(0.3);
              m_frontRight.set(0.3);
              m_backLeft.set(0.3);
              m_backRight.set(0.3);}
            else if((time - startTime >= 1  && time - startTime < 3)){
              craftsmanBLOW.set(-1.0);}         
            else if(time - startTime >= 3  && time - startTime < 5){
              DysonMotor.set(1.0);}
            else if(time - startTime >= 5  && time - startTime < 8){
              m_frontLeft.set(-0.6);
              m_frontRight.set(-0.6);
              m_backLeft.set(-0.6);
              m_backRight.set(-0.6);
             }
             else if(time - startTime >= 8  && time - startTime < 9){
              m_frontLeft.set(0.6);
              m_frontRight.set(0.6);
              m_backLeft.set(0.6);
              m_backRight.set(0.6);
             }
             else if(time - startTime >= 9){
              m_frontLeft.set(-0.6);
              m_frontRight.set(0.6);
              m_backLeft.set(-0.6);
              m_backRight.set(0.6);
             }
           
            else{
              m_frontLeft.set(0);
              m_frontRight.set(0);
              m_backLeft.set(0);
              m_backRight.set(0);
              craftsmanBLOW.set(0);
              DysonMotor.set(0);
            }
            }
  // This function is called periodically during operator control.
  @Override
  public void teleopPeriodic() {
    yValue yylophone = new yValue(gStick.getY());
    zValue zylophone = new zValue(gStick.getZ());
    wValue wylophone = new wValue(gStick.getRawAxis(3));
    Fricker Shaquille = new Fricker(gStick.getThrottle());

    /*
    Color pewach = chop.getColor(1);
    String colorString = "c";
    ColorMatchResult match = reeves.matchClosestColor(pewach);
    
    //ink colorPrint = new ink(pewach, colorString, match, BlueBoi, RedBoi, GreenBoi, YellowBoi);

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
    SmartDashboard.putNumber("# of Spins", topSpin);
*/
    DifOrange.arcadeDrive(yylophone.yJoy(),zylophone.zJoy(), false); //sets driving to run using  //joystick controls
                                                                                        //joystick controls


    if(gStick.getRawButton(6) == true){
        DysonMotor.set(-0.35);
    }
    else if(gStick.getRawButton(2) == true){
        DysonMotor.set(0.5);
        craftsmanBLOW.set(1.0);
    }
    else{
      DysonMotor.set(0.0);
    }

    if(gStick.getRawButton(1) == true){
        craftsmanBLOW.set(-1.0);
        DysonMotor.set(1.0);
      }
    else if(gStick.getRawButton(5) == true){
        craftsmanBLOW.set(1.0);
        DysonMotor.set(-1.0);
    }
    else if(gStick.getRawButton(3) == true){
      craftsmanBLOW.set(-1.0);
    }
    else if((gStick.getRawButton(3) && (gStick.getRawButton(1) == true)
    )){
      craftsmanBLOW.set(-1.0);
      DysonMotor.set(1.0);
    }
    else{
        craftsmanBLOW.set(0.0);

    if(gStick.getRawButton(8) == true){
       output = -wylophone.wSlide();
    }
    else if(gStick.getRawButton(7) == true){
      output = wylophone.wSlide();
    }
    else{
      output = 0.0;
    }
    

    if (DomGoth.get()==false){
      output = Math.min(output, 0);
    }
    else if (SubFemboy.get()==false){
      output = Math.max(output, 0);}
    }
    MeganTheeStallion.set((-output));
    DojaCat.set(output/2);



/*
    if(gStick.getRawButton(9) == true){
      while(match.color != BlueBoi){
        pewach = chop.getColor();
        match = reeves.matchClosestColor(pewach);
        FRICK.set(0.35);                            //goes to red
        if(gStick.getRawButton(8) == true)
          break;
      }
    }
    else if(gStick.getRawButton(10) == true){
      while(match.color != RedBoi){
        pewach = chop.getColor();
        match = reeves.matchClosestColor(pewach);
        FRICK.set(0.35);                            //goes to blue
        //colorPrint.colorSplash(); 
        if(gStick.getRawButton(8) == true)
          break;
      }
    }
    else if(gStick.getRawButton(11)){
      while(match.color != GreenBoi){
        pewach = chop.getColor();
        match = reeves.matchClosestColor(pewach);
        FRICK.set(0.35);                            //goes to yellow
        //colorPrint.colorSplash();
        if(gStick.getRawButton(8) == true)
          break;
      }
    }
    else if(gStick.getRawButton(12)){
      while(match.color != YellowBoi){
        pewach = chop.getColor();
        match = reeves.matchClosestColor(pewach);
        FRICK.set(0.35);                            //goes to green
        //colorPrint.colorSplash();
        if(gStick.getRawButton(8) == true)
          break;
      }
    }
    else{
    }
    if(gStick.getRawButton(7) == true){
      while(topSpin < 3.8){
      FRICK.set(0.35);
      pewach = chop.getColor();
      match = reeves.matchClosestColor(pewach);

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

      if(colorString == "GREEN")
          topSpin = topSpin + 0.5;
      
      SmartDashboard.putNumber("# of Spins", topSpin);
      if(gStick.getRawButton(8))
        break; 
      }
    }

    String gameData;
    gameData = DriverStation.getGameSpecificMessage();
    if(gameData.length() > 0){
      switch(gameData.charAt(0)){
        case 'B':
          System.out.println("B");
          break;
        case 'G':
          System.out.println("G");
          break;
        case 'R':
          System.out.println("R");
          break;
        case 'Y':
          System.out.println("Y");
          break;
        default:
          break;
      }
    }
    else{
    }
    */
    topSpin = 0;

    Timer.delay(0.001);
   }   //timer sets up the code to have a 1 millisecond delay to avoid overworking and 
          //over heating the RobotRIO

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
    return (zCal * 0.5);
  }
  else if((zCal >= -0.3) && (zCal <= 0.0)){
    return 0.0;
  }
  else if(zCal < -0.3){
    return (zCal * 0.5);
  }
  else{
    return 0.0;
  }
}
public double zCal;
}

class wValue{
  public wValue(double w){
    wCal = w;
  }
  public double wSlide(){
    return (-wCal+1)/2;

  }
  public double wCal;






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
/*
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
*/
