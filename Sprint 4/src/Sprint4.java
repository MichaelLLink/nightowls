import rxtxrobot.*;
import java.util.Scanner;
import java.lang.String;

public class Sprint4 {

    public static RXTXRobot robot;
    public static int speedL;
    public static int speedR;
    public static int pingFrontPin;
    public static int pingSidePin;
    public static int bumpPin;
    public static int tempPin;
    public static int windPin;
    public static int armPin;
    public static int dumpPin;

    //calibration variables
    public static double tempSlope;
    public static double tempIntercept;
    public static double windSlope;
    public static double windIntercept;
    public static double conductivitySlope;
    public static double conductivityIntercept;
    //public static int feetToTime;

    public static void main(String[] args)
    {
        //set up robot
        robot = new ArduinoUno();
        robot.setPort("/dev/tty.usbmodem1411"); //make sure to update port before running
        robot.connect();

        //set pins
        pingFrontPin = 7;       //digital
        pingSidePin = 11;       //digital (which side is based on wiring, for route 1, left, and route 2, right)
        bumpPin = 3;            //analog
        tempPin = 0;            //analog
        windPin = 1;            //analog
        dumpPin = 8;            //digital
        armPin = 10;            //digital

        //motion
        speedL = 250;
        speedR = 250;
        int fast = 500;
        int slowL = 250;
        int slowR = 250;

        //motors and servos
        robot.attachMotor(RXTXRobot.MOTOR1, 5);
        robot.attachMotor(RXTXRobot.MOTOR2, 6);
        robot.attachMotor(RXTXRobot.MOTOR3, 4);
        robot.attachServo(RXTXRobot.SERVO1, dumpPin);
        robot.attachServo(RXTXRobot.SERVO3,armPin);

        //calibration
        tempSlope = -13.664;
        tempIntercept = 991.71;
        windSlope = 5.2013;
        windIntercept = 40.023;
        conductivitySlope = -2159.3;
        conductivityIntercept = 1030.9;

        //other
        int yesWater = 0;

        //get starting position
        boolean trackPicked = false;
        Scanner input = new Scanner(System.in);
        int position;
        int left = 0;
        int right = 0;
        System.out.println("What is your starting position? ");
        position = input.nextInt();
        while(!trackPicked)
        {
            if (position == 1)
            {
                left = 1;
                right = 2;
                trackPicked = true;
            } else if (position == 2)
            {
                left = 2;
                right = 1;
                trackPicked = true;
            } else {
                System.out.println("Invalid position, pick again");
                //take input
                position = input.nextInt();
            }
        }

        //run through the course


    }

}
