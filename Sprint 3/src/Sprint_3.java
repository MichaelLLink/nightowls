import rxtxrobot.*;

import java.util.Scanner;

public class Sprint_3 {

    public static RXTXRobot robot;
    public static int speed;
    //public static int pingFrontPin;
    //public static int pingLeftPin;
    public static int pingRightPin;
    public static int bumpPin;
    public static int tempPin;
    public static int windPin;
    //public static int conductivityPin;
    //public static int armPin;
    //public static int gyroscopePin
    //public static int boomPin;
    public static int dumpPin;

        //data
    public static double windSpeed;
    public static double temp;
    public static float conductivity;

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
        pingRightPin = 7;    //digital pin
        bumpPin = 3;   //analog pin
        tempPin = 0;    //analog
        windPin = 1;  //analog
        //conductivityPin = ;   //Digital: D12, D13     Analog: A4, A5
        dumpPin = 9;     //digital
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
        robot.attachServo(RXTXRobot.SERVO1, dumpPin);
        //robot.attachServo(RXTXRobot.SERVO2,boomPin);
        //robot.attachServo(RXTXRobot.SERVO3,armPin);


            //run through the course


        robot.close();
    }

    //public static void moveTillSense(){}

    public static void move(int distance)
    {
        int ticks = distance*11;

        robot.runEncodedMotor(RXTXRobot.MOTOR1, speed, ticks, RXTXRobot.MOTOR2, -speed, ticks);

    }

    public static void turn(int direction)
    {
        if(direction == 1) //left
        {
            //robot.runEncodedMotor(RXTXRobot.MOTOR1, -speed, RXTXRobot.MOTOR2, -speed, [man idk]);
            robot.runMotor(RXTXRobot.MOTOR1, -speed, RXTXRobot.MOTOR2, -speed, 2000);
        }
        else if(direction == 2) //right
        {
            //robot.runEncodedMotor(RXTXRobot.MOTOR1, speed, RXTXRobot.MOTOR2, speed, [man idk]);
            robot.runMotor(RXTXRobot.MOTOR1, speed, RXTXRobot.MOTOR2, speed, 2000);
        }
    }

    public static void raiseBoom()
    {
        robot.moveServo(RXTXRobot.SERVO2, 180);
    }

    public static void lowerBoom()
    {
        robot.moveServo(RXTXRobot.SERVO2, 0);
    }

    public static void lowerArm()
    {
        robot.moveServo(RXTXRobot.SERVO3, 0);
    }

    public static void raiseArm()
    {
        robot.moveServo(RXTXRobot.SERVO3, 180);
    }

    public static void deployBeacon()
    {
        robot.moveServo(RXTXRobot.SERVO1, 145);
    }

    //public static void takeConductivity(){}

    public static void changeAngle(int angle) {
        //move servo
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

    public static void senseDistance(int pin)
    {
        robot.refreshDigitalPins();
        int distance = robot.getPing(pin); //remember to check pin

        //read out distance
        System.out.println("Distance: " + distance + "cm");
    }

    public static void takeTemp()
    {
        double thermistorReading = getThermistorReading();

        //System.out.println("The probe read the value: " + thermistorReading);
        //System.out.println("In volts: " + (thermistorReading * (5.0/1023.0)));

        double temp = 0;

        temp = (thermistorReading - exposedIntercept)/exposedSlope;

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

    public static void getWindSpeed()
    {
        double anemometerReading = getAnemometerReading();
        double thermistorReading = getThermistorReading();

        double windSpeed = ((thermistorReading-anemometerReading)-windIntercept)/windSlope;

        //not entirely sure what we need here
        System.out.println("The shielded thermistor read the value: " + anemometerReading);
        //System.out.println("In volts: " + (anemometerReading * (5.0/1023.0)));

        System.out.println("The exposed thermistor read the value: " + thermistorReading);
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