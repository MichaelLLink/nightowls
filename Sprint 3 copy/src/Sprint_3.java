import rxtxrobot.*;

import java.util.Scanner;
import java.lang.String;

public class Sprint_3 {

    private static RXTXRobot robot;
    private static int speedL;
    private static int speedR;
    private static int speed;
    private static int pingFrontPin;
    private static int pingSidePin;
    private static int bumpPin;
    private static int tempPin;
    private static int windPin;
    private static int armPin;
    private static int boomPin;
    //private static int boomDCPin;
    private static int dumpPin;

    //data
    private static double windSpeed;
    private static double temp;
    private static float conductivity;

    //calibrations
    private static double tempSlope;
    private static double tempIntercept;
    private static double windSlope;
    private static double windIntercept;
    private static int feetToTicks;
    private static int feetToTime;

    public static void main(String[] args) {
        //set up robot
        robot = new ArduinoUno();
        robot.setPort("/dev/tty.usbmodem1411"); //make sure to update port before running
        robot.connect();

        //set pin and other static variables
        pingFrontPin = 7;      //digital pin
        pingSidePin = 11;    //digital pin (which side is based on wiring, for route 1, left, and route 2, right)
        bumpPin = 3;   //analog pin
        tempPin = 0;    //analog
        windPin = 1;  //analog
        boomPin = 4;   //digital
        //boomDCPin = 4; //digital
        dumpPin = 8;   //digital
        armPin = 10; //digital
        //NOTE: conductivity pins //Digital: D12, D13     Analog: A4, A5

        speedL = 250;
        speedR = 250;
        int fast = 500;
        int slowL = 250;
        int slowR = 250;
        int yesWater = 0; //change this to whatever the conductivity needed to release the beacon is

        //calibrations
        tempSlope = -13.664;
        tempIntercept = 991.71;
        windSlope = 5.2013;
        windIntercept = 40.023;
        feetToTicks = 11;
        feetToTime = 609;



        //set up motors and sensors
        robot.attachMotor(RXTXRobot.MOTOR1, 5);
        robot.attachMotor(RXTXRobot.MOTOR2, 6);
        //robot.attachMotor(RXTXRobot.MOTOR3, boomDCPin);
        robot.attachServo(RXTXRobot.SERVO1, dumpPin);
        robot.attachServo(RXTXRobot.SERVO3,armPin);
        robot.attachServo(RXTXRobot.SERVO2,boomPin);

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
        /*
        move(1.5);  //leave start
        turn(left); //turn into track
        moveTillSense(30);  //move till arbitrary barrier
        while(robot.getPing(pingFrontPin) <= 50)    //wait for barrier to be removed
        {
            robot.refreshDigitalPins();
            System.out.println("Barrier");
        }

        //move up ramp
        speedL = fast;
        speedR = fast;
        move(2.8);
        raiseBoom();        //raise boom
        takeTemp();         //take temp
        if(temp == 0)       //check that temp was actually taken
        {
            takeTemp();
        }
        robot.sleep(10000);
        getWindSpeed();     //get wind speed
        if(windSpeed == 0)  //check that wind speed was taken
        {
            getWindSpeed();
        }
        output();
        lowerBoom();        //lower boom
        turn(right);    //turn into the track
        */
        speedL = slowL;
        speedR = slowR;

        move(2);          //move down ramp
        senseGap();             //move till there's a gap to whatever side we need
        turn(right);            //turn into gap
        move(3);    //move through gap
        turn(right);         //turn towards the back wall
        moveTillSense(100);     //move till we're close enough to back wall
        turn(left);     //turn towards bridge
        moveTillSense(30);  //line up with bridge
        turn(left);     //turn in to bridge

        speedL=fast;
        speedR=fast;
        move(2);           //go up ramp to bridge
        speedL=slowL;
        speedR=slowR;
        move(3);        //move across the bridge
        move(0.2);        //go down ramp on other side of the bridge

        turn(left);             //turn towards soil
        runTillBump();          //run into the soil container
        lowerArm();             //drop conductivity probe into soil
        lowerArm();
        takeConductivity();     //take conductivity
        if (conductivity == 0)   //make sure conductivity was taken
        {
            takeConductivity();
        }
        if (conductivity >= yesWater) {
            deployBeacon();
            //check that beacon was deployed
        }
        raiseArm();         //raise conductivity probe

        output();
        robot.close();

    }

    private static void move(double distance)
    {
        int time = (int)distance*1000;
        int ticks = (int)distance*feetToTicks;

        //robot.resetEncodedMotorPosition(RXTXRobot.MOTOR1);

        //int moved = 0;

        int speederL;
        int speederR;


        if(distance < 0)
        {
            //reverse = true;
            speederL = -speedL;
            speederR = -speedR;
        }
        else
        {
            speederL = speedL;
            speederR = speedR;
        }

        robot.runMotor(RXTXRobot.MOTOR1, speedL, RXTXRobot.MOTOR2, -speedR, time);
    }

    private static void turn(int direction)
    {
        if(direction == 1) //left
        {
            //robot.runEncodedMotor(RXTXRobot.MOTOR1, -speed, RXTXRobot.MOTOR2, -speed, [man idk]);
            robot.runMotor(RXTXRobot.MOTOR1, 10, RXTXRobot.MOTOR2, -500, 1700);
        }
        else if(direction == 2) //right
        {
            //robot.runEncodedMotor(RXTXRobot.MOTOR1, speed, RXTXRobot.MOTOR2, speed, [man idk]);
            robot.runMotor(RXTXRobot.MOTOR1, 500, RXTXRobot.MOTOR2, -10, 1700);
        }
    }

    private static void raiseBoom()
    {
        //robot.runMotor(RXTXRobot.MOTOR3,100,15000);
        robot.moveServo(RXTXRobot.SERVO2, 110);
    }

    private static void lowerBoom()
    {
        //robot.runMotor(RXTXRobot.MOTOR3,-100,3000);
        robot.moveServo(RXTXRobot.SERVO2, 45);
    }

    private static void lowerArm()
    {
        robot.moveServo(RXTXRobot.SERVO3, 20);
    }

    private static void raiseArm()
    {
        robot.moveServo(RXTXRobot.SERVO3, 90);
    }

    private static void deployBeacon()
    {
        robot.moveServo(RXTXRobot.SERVO1, 180);
    }

    private static void takeConductivity()
    {
        conductivity = robot.getConductivity();
        System.out.println("The SOIL CONDUCTIVITY is: " + conductivity);
    }

    private static void runTillBump()
    {
        boolean bumpTriggered = false;

        robot.runMotor(RXTXRobot.MOTOR1, speedR, RXTXRobot.MOTOR2, -speedL, 0);

        while (!bumpTriggered)
        {
            robot.refreshAnalogPins();
            int reading = robot.getAnalogPin(bumpPin).getValue();

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

    private static void moveTillSense(int space)
    {
        boolean tooClose = false;
        int distance;

        robot.resetEncodedMotorPosition(RXTXRobot.MOTOR1);

        robot.runMotor(RXTXRobot.MOTOR1, speed, RXTXRobot.MOTOR2, -speed, 0);

        while (!tooClose)
        {
            robot.refreshDigitalPins();
            distance = robot.getPing(pingFrontPin);

            if(distance <= space)
            {
                tooClose = true;
                robot.runMotor(RXTXRobot.MOTOR1, 0, RXTXRobot.MOTOR2, 0, 0);
                System.out.println("barrier reached");
            }
        }
    }

    private static void senseGap()
    {
        boolean gap = false;
        int distance;

        //robot.resetEncodedMotorPosition(RXTXRobot.MOTOR1);

        robot.runMotor(RXTXRobot.MOTOR1, speedL, RXTXRobot.MOTOR2, -speedR, 0);

        while (!gap)
        {
            distance = senseDistance(pingSidePin);

            if(distance <= 50)
            {
                gap = true;
                robot.runMotor(RXTXRobot.MOTOR1, 0, RXTXRobot.MOTOR2, 0, 0);
            }
        }
    }

    private static int senseDistance(int pin)
    {
        robot.refreshDigitalPins();
        //int distance = robot.getPing(pin); //remember to check pin
        return robot.getPing(pin);
    }

    private static void takeTemp()
    {
        double thermistorReading = getThermistorReading();
        temp = (thermistorReading - tempIntercept)/tempSlope;

        System.out.println("The temperature is: " + temp + " celsius");
    }

    private static int getThermistorReading()
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

    private static void getWindSpeed()
    {
        double anemometerReading = getAnemometerReading();
        double thermistorReading = getThermistorReading();

        windSpeed = ((anemometerReading-thermistorReading)-windIntercept)/windSlope;

        System.out.println("The wind speed is: " + windSpeed + "m/s");

    }

    private static int getAnemometerReading()
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

    //UNSURE IF THIS IS SOMETHING WE NEED BUT HERE IT IS. IF WE HAVE TO OUTPUT TO A FILE, WE CAN SET THAT UP LATER, I GUESS
    private static void output()
    {
        System.out.println("SENSORS READ:");
        System.out.println("The TEMPERATURE is: " + temp + " celsius");
        System.out.println("The WIND SPEED is: " + temp + "Whatever the wind units are");
        System.out.println("The soil CONDUCTIVITY is: " + conductivity);
    }

}