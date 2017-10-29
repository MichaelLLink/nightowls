import rxtxrobot.*;

import java.util.Scanner;
import java.lang.String;

public class Sprint3 {

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
        dumpPin = 9;     //digital
        boomPin = 8;   //digital
        dumpPin = 10;   //digital
        armPin = 6; //digital
        //NOTE: conductivity pins //Digital: D12, D13     Analog: A4, A5

        speedL = 500;
        speedR = 150;
        speed = 500;
        int yesWater = 0; //change this to whatever the conductivity needed to release the beacon is

        //calibrations
        tempSlope = -13.664;
        tempIntercept = 991.71;
        windSlope = 5.2013;
        windIntercept = 40.023;
        feetToTicks = 11;
        feetToTime = 2666;



        //set up motors and sensors
        robot.attachMotor(RXTXRobot.MOTOR1, 5);
        robot.attachMotor(RXTXRobot.MOTOR2, 6);
        //robot.attachServo(RXTXRobot.SERVO1, dumpPin);
        //robot.attachServo(RXTXRobot.SERVO2,boomPin);
        //robot.attachServo(RXTXRobot.SERVO3,armPin);

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
        move(1);    //move out of the starting box
        turn(left);    //turn left
        moveTillSense();    //move till barrier
        move(3);           //move up ramp
        output();
        turn(right);    //turn into the track
        move(3);          //move down ramp
        senseGap();    //move till there's a gap to whatever side we need (adjust wiring for this
        turn(right);    //turn to the right
        //move till we're where we need to be for the bridge
        move(4);
        turn(left);
        runTillBump();
        move(-1);
        turn(right);
        runTillBump();
        move(-1);//line up with bridge
        turn(right);

        //PAUSE TO NOT RUN OFF THE EDGE AND BREAK THE ROBOT
        System.out.println("Are we lined up? Y/N ");
        String in;
        in = input.next();
        if(in == "n")
        {
            while(in != "e" && in != "y")
            {
                System.out.println("How about now? Y/N/E");
                in = input.next();
            }
        }
        if(in.equals("y")) {
            move(2);           //go up ramp to bridge
            move(3);    //move across the bridge
            move(2);          //go down ramp on other side of the bridge
            moveTillSense();    //only works if the playing field is bounded
            turn(left);    //turn left
            runTillBump();      //run into the soil container
            output(); //if needed, rn it'll just display to screen
            robot.close();
        }
        else robot.close();
    }

    private static void move(int distance)
    {
        //boolean reverse  = false;
        int speederL;
        int speederR;
        //int speeder = 0;

        if(distance < 0)
        {
            //reverse = true;
            speederL = -speedL;
            speederR = -speedR;
            //speeder = -speed;
        }
        else
        {
            speederL = speedL;
            speederR = speedR;
            //speeder = speed;
        }

        int ticks = distance*feetToTicks;
        int time = distance*feetToTime;
        int moved = 0;
        int space;

        robot.resetEncodedMotorPosition(RXTXRobot.MOTOR1);

        //robot.runEncodedMotor(RXTXRobot.MOTOR1, speeder, ticks, RXTXRobot.MOTOR2, -speeder, ticks);
        robot.runMotor(RXTXRobot.MOTOR1, speederL, RXTXRobot.MOTOR2, -speederR, time);

        while(moved != ticks)
        {
            space = senseDistance(pingFrontPin);

            if(space <= 10)
            {
                robot.runMotor(RXTXRobot.MOTOR1, 0, RXTXRobot.MOTOR2, 0, 0);
                System.out.println("idling");
            }

            if(space > 10)
            {
                //robot.runEncodedMotor(RXTXRobot.MOTOR1, speeder, ticks, RXTXRobot.MOTOR2, -speeder, ticks);
                robot.runMotor(RXTXRobot.MOTOR1, speederL, RXTXRobot.MOTOR2, -speederR, time);
            }

            moved = robot.getEncodedMotorPosition(RXTXRobot.MOTOR1);
        }

    }

    private static void turn(int direction)
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

    private static void raiseBoom()
    {
        robot.moveServo(RXTXRobot.SERVO2, 180);
    }

    private static void lowerBoom()
    {
        robot.moveServo(RXTXRobot.SERVO2, 0);
    }

    private static void lowerArm()
    {
        robot.moveServo(RXTXRobot.SERVO3, 90);
    }

    private static void raiseArm()
    {
        robot.moveServo(RXTXRobot.SERVO3, 0);
    }

    private static void deployBeacon()
    {
        robot.moveServo(RXTXRobot.SERVO1, 145);
        //add a bit of buffer here
        //robot.moveServo(RXTXRobot.SERVO1, 0);
    }

    private static void takeConductivity()
    {
        conductivity = robot.getConductivity();
    }

    private static void runTillBump()
    {
        boolean bumpTriggered = false;

        robot.runMotor(RXTXRobot.MOTOR1, speed, RXTXRobot.MOTOR2, -speed, 0);

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

    private static void moveTillSense()
    {
        boolean tooClose = false;
        int distance;

        robot.resetEncodedMotorPosition(RXTXRobot.MOTOR1);

        robot.runMotor(RXTXRobot.MOTOR1, speed, RXTXRobot.MOTOR2, -speed, 0);

        while (!tooClose)
        {
            distance = senseDistance(pingFrontPin);

            if(distance <= 10)
            {
                tooClose = true;
                robot.runMotor(RXTXRobot.MOTOR1, 0, RXTXRobot.MOTOR2, 0, 0);
            }
        }
    }

    private static void senseGap()
    {
        boolean gap = false;
        int distance;
        //int pin = 0;

/* this is done based on wiring now
        if(direction == 1) //left
        {
            //pin = pingLeftPin;
        }
        else if(direction == 2) //right
        {
            pin = pingRightPin;
        }
*/
        robot.resetEncodedMotorPosition(RXTXRobot.MOTOR1);

        robot.runMotor(RXTXRobot.MOTOR1, speedL, RXTXRobot.MOTOR2, -speedR, 0);

        while (!gap)
        {
            distance = senseDistance(pingSidePin);

            if(distance <= 10)
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

        //System.out.println("The probe read the value: " + thermistorReading);
        //System.out.println("In volts: " + (thermistorReading * (5.0/1023.0)));

        temp = 0;

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

    //UNCALIBRATED
    private static void getWindSpeed()
    {
        double anemometerReading = getAnemometerReading();
        double thermistorReading = getThermistorReading();

        windSpeed = ((anemometerReading-thermistorReading)-windIntercept)/windSlope;

        System.out.println("The shielded thermistor read the value: " + anemometerReading);
        //System.out.println("In volts: " + (anemometerReading * (5.0/1023.0)));

        System.out.println("The exposed thermistor read the value: " + thermistorReading);
        //System.out.println("In volts: " + (thermistorReading * (5.0/1023.0)));

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
        System.out.println("The TEMPERATURE is: " + temp + " celsius");
        System.out.println("The WIND SPEED is: " + temp + "Whatever the wind units are");
        System.out.println("The soil CONDUCTIVITY is: " + conductivity);
    }

}
