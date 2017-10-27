import rxtxrobot.*;
import java.util.Scanner;
import java.util.Timer;

public class Sprint2 {

    public static RXTXRobot robot;
    public static int speed;
    public static int pingPin;
    public static int bumpPin;
    public static int tempPin;
    public static int windPin;
    //public static int conductivityPin;
    public static int armPin;

        //calibrations
    public static double tempSlope;
    public static double tempIntercept;
    public static double windSlope;
    public static double windIntercept;

    public static void main(String[] args)
    {
            //set up robot
        robot = new ArduinoUno();
        robot.setPort("/dev/tty.usbmodem1411"); //make sure to update port before running
        robot.connect();

            //set pin and other static variables
        pingPin = 11;    //digital pin
        bumpPin = 3;   //analog pin
        tempPin = 0;    //analog
        windPin= 1;  //analog
        //conductivityPin = ;   //Digital: D12, D13     Analog: A4, A5
        armPin = 8;     //digital

        robot.attachMotor(RXTXRobot.MOTOR1,5);
        robot.attachMotor(RXTXRobot.MOTOR2,6);

            //calibrations
        tempSlope = -13.664;
        tempIntercept = 991.71;
        windSlope = 5.2013;
        windIntercept = 40.023;

        speed = 200;

            //set up motors and sensors
        robot.attachServo(RXTXRobot.SERVO1, armPin);
        robot.moveServo(RXTXRobot.SERVO1, 90);

            //set up scanner and input
        Scanner input = new Scanner(System.in);
        int iChoice = 0;


        do
        {
            //reset choice for each loop
            iChoice = 0;

                //display menu
            displayMenu();

                //scan for input
            iChoice = input.nextInt();

                //begin switch loop for selections
            switch(iChoice)
            {
                case 1:
                    move();
                    break;
                case 2:
                    changeAngle();
                    break;
                case 3:
                    runTillBump();
                    break;
                case 4:
                    senseDistance();
                    break;
                case 5:
                    takeTemp();
                    break;
                case 6:
                    //getConductivity();
                    getWindSpeed();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Please select an *actual* test");
                    break;
            }

        }while(iChoice != 0);

        System.out.println("Thank you for your time!");

            //close robot
        robot.close();

    }

    public static void displayMenu()
    {
        System.out.println("\n**WELCOME TO NIGHTOWLS SPRINT 2 PRESENTATION**");
        System.out.println("Which test would you like to preform?");
        System.out.println("1 - Move Test");
        System.out.println("2 - Servo Test");
        System.out.println("3 - Bumper Test");
        System.out.println("4 - Ping Test");
        System.out.println("5 - Temperature Test");
        System.out.println("6 - Anemometer Test");
        System.out.println("0 - Exit");
    }

    public static void move()
    {
        //distance in inches
        int distance = 3;

        //set ticks (11 being our ticks-per-inches ratio)
        int ticks = 110 * distance;

        //move 3 feet
        //robot.runEncodedMotor(RXTXRobot.MOTOR2, -speed, ticks, RXTXRobot.MOTOR1, speed, ticks);
        System.out.println(robot.getEncodedMotorPosition(RXTXRobot.MOTOR2));
        System.out.println(robot.getEncodedMotorPosition(RXTXRobot.MOTOR1));
        Scanner input = new Scanner(System.in);;
        int in = 1;

        robot.runMotor(RXTXRobot.MOTOR1, -150, RXTXRobot.MOTOR2, 500, 8000);
        //robot.runMotor(RXTXRobot.MOTOR1, 150, 8000);

        System.out.println(robot.getEncodedMotorPosition(RXTXRobot.MOTOR2));
        System.out.println(robot.getEncodedMotorPosition(RXTXRobot.MOTOR1));

        /*
        while(in != 0)
        {
            in = input.nextInt();
            if(in == 0)
            {
                robot.runMotor(RXTXRobot.MOTOR1, 0, RXTXRobot.MOTOR2, 0, 0);
            }
        }
        */
        //robot.runEncodedMotor();
        //robot.runMotor(RXTXRobot.MOTOR1, speed, RXTXRobot.MOTOR2, -speed, 5000);
    }

    public static void changeAngle()
    {
        Scanner input = new Scanner(System.in);
        int angle = 0;
        angle = input.nextInt();

            //move servo
        robot.moveServo(RXTXRobot.SERVO1, angle);

        angle = input.nextInt();
        //angle = 0;


            //reset servo
        robot.moveServo(RXTXRobot.SERVO1, angle);
    }

    public static void runTillBump()
    {
        boolean bumpTriggered = false;

        robot.runMotor(RXTXRobot.MOTOR1, speed, RXTXRobot.MOTOR2, -speed, 0);

        while (!bumpTriggered)
        {
            robot.refreshAnalogPins();
            int reading = robot.getAnalogPin(bumpPin).getValue(); //IDK what value signifies not pushed

            //add if statement to set bumpTriggered based on reading
            if(reading == 0) {
                bumpTriggered = true;
                System.out.println(reading);
                System.out.println("bump triggered");
            }

            if(bumpTriggered)
            {
                //stop motor
                robot.runMotor(RXTXRobot.MOTOR1, 0, RXTXRobot.MOTOR2, 0, 0);
            }

        }
    }

    public static void senseDistance()
    {
        robot.refreshDigitalPins();
        int distance = robot.getPing(pingPin); //remember to check pin

            //read out distance
        System.out.println("Distance: " + distance + "cm");
    }

    public static void takeTemp()
    {
        double thermistorReading = getThermistorReading();

        System.out.println("The probe read the value: " + thermistorReading);
        System.out.println("In volts: " + (thermistorReading * (5.0/1023.0)));

        double temp = 0;

        temp = (thermistorReading - tempIntercept)/tempSlope;

        System.out.println("The temperature is: " + temp + " celsius");
    }

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

    /*
    public static void getConductivity()
    {

    }
    */
    public static void getWindSpeed()
    {
        double anemometerReading = getAnemometerReading();
        double thermistorReading = getThermistorReading();

        double windSpeed = ((anemometerReading-thermistorReading)-windIntercept)/windSlope;

            //not entirely sure what we need here
        System.out.println("The shielded thermistor/temp read the value: " + thermistorReading);
        //System.out.println("In volts: " + (anemometerReading * (5.0/1023.0)));

        System.out.println("The exposed thermistor/wind read the value: " + anemometerReading);
        //System.out.println("In volts: " + (thermistorReading * (5.0/1023.0)));

        System.out.println("The wind speed is: " + windSpeed + "m/s");

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

}
