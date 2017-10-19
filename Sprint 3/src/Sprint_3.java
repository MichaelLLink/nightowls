import rxtxrobot.*;

public class Sprint_3 {

    public static RXTXRobot robot;
    public static int speed;
    public static int pingFrontPin;
    //public static int pingLeftPin;
    //public static int pingRightPin;
    public static int bumpPin;
    public static int tempPin;
    public static int windPin;
    //public static int conductivityPin;
    public static int armPin;
    //public static int gyroscopePin
    //public static int probePin;
    //public static int dumpPin;

    //calibrations
    public static double exposedSlope;
    public static double exposedIntercept;
    public static double windSlope;
    public static double windIntercept;

    public static void main(String[] args) {
        //set up robot
        robot = new ArduinoUno();
        robot.setPort("/dev/tty.usbmodem1411"); //make sure to update port before running
        robot.connect();

        //set pin and other static variables
        //pingPin = 7;    //digital pin
        bumpPin = 3;   //analog pin
        tempPin = 0;    //analog
        windPin = 1;  //analog
        //conductivityPin = ;   //Digital: D12, D13     Analog: A4, A5
        armPin = 9;     //digital
        //gyroscopePin
        //probePin
        //dumpPin


        //calibrations
        exposedSlope = -6.58594486;
        exposedIntercept = 687.9718646;
        windSlope = -15.595;
        windIntercept = 448.26;

        speed = 200;

        //set up motors and sensors
        robot.attachMotor(RXTXRobot.MOTOR1, 5);
        robot.attachMotor(RXTXRobot.MOTOR2, 6);
        robot.attachServo(RXTXRobot.SERVO1, armPin);
        //robot.attachServo(RXTXRobot.SERVO2,probePin);
        //robot.attachServo(RXTXRobot.SERVO3,dumpPin);


        //run through the course


        robot.close();
    }

    //public static void moveTillSense(){}

    //public static void moveTillBump(){}

    //public static void move(float distance){}

    //public static void turn(int direction){}

    //public static void raiseSensors(){}

    //public static void lowerSensors(){}

    //public static void lowerArm(){}

    //public static void raiseArm(){}

    //public static void deployBeacon(){}

    //public static [return] takeTemp(){}

    //public static [return] takeWindSpeed(){}

    //public static [return] takeConductivity(){}

    /*
    public static int getThermistorReading()
    {
        int sum = 0;
        int readingCount = 10;

        for(int i = 0; i < readingCount; i++)
        {
            robot.refreshAnalogPins();
            int reading = robot.getAnalogPin(tempPin).getValue();
            sum += reading;
        }

        return sum / readingCount;
    }

    public static int getAnemometerReading()
    {
        int sum = 0;
        int readingCount = 10;

        for(int i = 0; i < readingCount; i++)
        {
            robot.refreshAnalogPins();
            int reading = robot.getAnalogPin(windPin).getValue();
            sum += reading;
        }

        return sum / readingCount;
    }
    */
}