import rxtxrobot.*;
import java.util.Scanner;
import java.lang.String;

public class Sprint4 {

    private static RXTXRobot robot;
    private static int speedL;
    private static int speedR;
    private static int pingFrontPin;
    private static int pingSidePin;
    private static int bumpPin;
    private static int tempPin;
    private static int windPin;
    private static int armPin;
    private static int dumpPin;

    //calibration variables
    private static double tempSlope;
    private static double tempIntercept;
    private static double windSlope;
    private static double windIntercept;
    private static double conductivitySlope;
    private static double conductivityIntercept;

    //other
    private static double conductivity;

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
        robot.attachServo(RXTXRobot.SERVO3, armPin);

        //calibration
        tempSlope = -13.664;
        tempIntercept = 991.71;
        windSlope = 5.2013;
        windIntercept = 40.023;
        conductivitySlope = -2159.3;
        conductivityIntercept = 1030.9;

        //other
        int yesWater = 1;

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

        //robot.runMotor(RXTXRobot.MOTOR3, 100, 4500);  //partially raise boom
        //robot.sleep(2000);

        //run through the course
        
        //leave start

        //robot.runMotor(RXTXRobot.MOTOR1,speedL,RXTXRobot.MOTOR2,-speedR,1900);
        moveTillSense(40);
        turn(right); //turn into track
        robot.sleep(1000);
        robot.refreshDigitalPins();
        moveTillSense(30);  //move till arbitrary barrier
        //move(2);
        
        int barrier;
        robot.refreshDigitalPins();
        barrier = robot.getPing(pingFrontPin);
        System.out.println( "line 109 " + barrier);
        barrier = robot.getPing(pingFrontPin);
        System.out.println( "line 111 " + barrier);
        barrier = robot.getPing(pingFrontPin);
        System.out.println( "line 113 " + barrier);
        robot.sleep(1000);

         while(barrier <= 60)    //wait for barrier to be removed
         {
            robot.refreshDigitalPins();
            barrier = robot.getPing(pingFrontPin);
            System.out.println("Barrier" + barrier);
            barrier = robot.getPing(pingFrontPin);
            System.out.println("Barrier" + barrier);
            barrier = robot.getPing(pingFrontPin);
            System.out.println("Barrier" + barrier);
            robot.sleep(1000);
         }

        System.out.println("barrier gone " + barrier);

         //move up ramp
         speedL = fast;
         speedR = fast;
         robot.sleep(1000);
         move(2.9);
         robot.sleep(1000);

         //raiseBoom();        //raise boom
         //takeTemp();         //take temp

         //robot.sleep(10000);
         //getWindSpeed();     //get wind speed

         //lowerBoom();        //lower boom
         turn(left);    //turn into the track


        speedL = 300;
        speedR = slowR;

        move(1.8);          //move down ramp
        //robot.runMotor(RXTXRobot.MOTOR1,150,RXTXRobot.MOTOR2,-150,2500);
         
         robot.sleep(1000);

        speedL = slowL;
        speedR = slowR;

        senseGap();             //move till there's a gap to whatever side we need

        //move(1);
        turn(left);            //turn into gap
        //robot.runMotor(RXTXRobot.MOTOR1, 500, RXTXRobot.MOTOR2, -10, 1500);


         move(2);    //move through gap
         turn(left);         //turn towards the back wall

         moveTillSense(100);     //move till we're close enough to back wall

         turn(right);     //turn towards bridge

         moveTillSense(40);  //line up with bridge

         turn(right);     //turn in to bridge
         //robot.runMotor(RXTXRobot.MOTOR1, 10, RXTXRobot.MOTOR2, -500, 1630);


/*
         speedL=fast;
         speedR=fast;
         move(2);           //go up ramp to bridge
         speedL=slowL;
         speedR=slowR;
         move(3);        //move across the bridge
         //move(0.2);        //go down ramp on other side of the bridge
        robot.runMotor(RXTXRobot.MOTOR1,speedL,RXTXRobot.MOTOR2,-speedR,500);
         
         turn(right);             //turn towards soil
        //robot.runMotor(RXTXRobot.MOTOR1, 10, RXTXRobot.MOTOR2, -500, 1650);
         
         
         speedL = slowL;
         speedR = slowR;
         runTillBump();          //run into the soil container
         lowerArm();             //drop conductivity probe into soil
         lowerArm();
         getConductivity();     //take conductivity

         if (conductivity >= yesWater)
         {
            deployBeacon();
         }
         raiseArm();         //raise conductivity probe
//*/

        System.out.println("End reached. Goodnight");
        robot.close();
        
    }
    
    private static void move(double distance)
    {
        int time = (int)distance*1000;
        
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
        
        robot.runMotor(RXTXRobot.MOTOR1, speederL, RXTXRobot.MOTOR2, -speederR, time);
    }
    
    private static void turn(int direction)
    {
        robot.resetEncodedMotorPosition(RXTXRobot.MOTOR1);
        robot.resetEncodedMotorPosition(RXTXRobot.MOTOR2);
        robot.resetEncodedMotorPosition(RXTXRobot.MOTOR1);
        robot.resetEncodedMotorPosition(RXTXRobot.MOTOR2);
        System.out.println("Motor 1: " + robot.getEncodedMotorPosition(RXTXRobot.MOTOR1));
        System.out.println("Motor 2: " + robot.getEncodedMotorPosition(RXTXRobot.MOTOR2));
        boolean turned = false;
        int ticks = 0;

/*
        if(direction == 1) //left
        {
            //robot.runEncodedMotor(RXTXRobot.MOTOR1, -speed, RXTXRobot.MOTOR2, -speed, [man idk]);
            robot.runMotor(RXTXRobot.MOTOR1, 10, RXTXRobot.MOTOR2, -500, 1450);
            System.out.println("Motor 1: " + robot.getEncodedMotorPosition(RXTXRobot.MOTOR1));
            System.out.println("Motor 2: " + robot.getEncodedMotorPosition(RXTXRobot.MOTOR2));
        }
        else if(direction == 2) //right
        {
            //robot.runEncodedMotor(RXTXRobot.MOTOR1, speed, RXTXRobot.MOTOR2, speed, [man idk]);
            robot.runMotor(RXTXRobot.MOTOR1, 500, RXTXRobot.MOTOR2, -10, 1250);
            System.out.println("Motor 1: " + robot.getEncodedMotorPosition(RXTXRobot.MOTOR1));
            System.out.println("Motor 2: " + robot.getEncodedMotorPosition(RXTXRobot.MOTOR2));
        }

        */
        if(direction == 1) //left
        {
            while(!turned)
            {
                if(ticks >= -40) {
                    robot.runMotor(RXTXRobot.MOTOR1, -500, RXTXRobot.MOTOR2, -500, 150);
                }
                if(ticks < -40 && ticks > -90)
                {
                    robot.runMotor(RXTXRobot.MOTOR1, -500,RXTXRobot.MOTOR2, -500, 100);
                }
                if(ticks <= -90)
                {
                    robot.runMotor(RXTXRobot.MOTOR1, -500,RXTXRobot.MOTOR2, -500, 45);
                }

                ticks = robot.getEncodedMotorPosition(RXTXRobot.MOTOR1);

                if(ticks <= -170)
                {
                    turned = true;
                    System.out.println("Motor 1: " + robot.getEncodedMotorPosition(RXTXRobot.MOTOR1));
                    System.out.println("Motor 2: " + robot.getEncodedMotorPosition(RXTXRobot.MOTOR2));
                }
            }
        }
        else if(direction == 2) //right
        {
            while(!turned)
            {
                if(ticks <= 50) {
                    robot.runMotor(RXTXRobot.MOTOR1, 500, RXTXRobot.MOTOR2, 500, 150);
                }
                if(ticks > 50 && ticks < 80)
                {
                    robot.runMotor(RXTXRobot.MOTOR1, 500,RXTXRobot.MOTOR2, 500, 100);
                }
                if(ticks >= 80)
                {
                    robot.runMotor(RXTXRobot.MOTOR1, 500,RXTXRobot.MOTOR2, 500, 40);
                }

                ticks = robot.getEncodedMotorPosition(RXTXRobot.MOTOR1);

                if(ticks >= 245)
                {
                    turned = true;
                    System.out.println("Motor 1: " + robot.getEncodedMotorPosition(RXTXRobot.MOTOR1));
                    System.out.println("Motor 2: " + robot.getEncodedMotorPosition(RXTXRobot.MOTOR2));
                }
            }
        }

    }
    
    private static void raiseBoom()
    {
        //robot.runMotor(RXTXRobot.MOTOR3,100,15000);
        //robot.moveServo(RXTXRobot.SERVO2, 110);
    }
    
    private static void lowerBoom()
    {
        //robot.runMotor(RXTXRobot.MOTOR3,-100,3000);
        //robot.moveServo(RXTXRobot.SERVO2, 45);
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
    private static void moveTillSense(int space) {
        boolean tooClose = false;
        int distance;

        robot.resetEncodedMotorPosition(RXTXRobot.MOTOR1);

        while (!tooClose) {

            robot.runMotor(RXTXRobot.MOTOR1, speedL, RXTXRobot.MOTOR2, -speedR, 500);

            robot.refreshDigitalPins();
            distance = robot.getPing(pingFrontPin);
            System.out.println("move till sense not too close distance: " + distance);
            distance = robot.getPing(pingFrontPin);
            System.out.println("move till sense not too close distance: " + distance);
            distance = robot.getPing(pingFrontPin);
            System.out.println("move till sense not too close distance: " + distance);

            if (distance <= space) {
                tooClose = true;
                robot.runMotor(RXTXRobot.MOTOR1, 0, RXTXRobot.MOTOR2, 0, 0);
                System.out.println("barrier reached " + distance);
            }
        }
    }
    private static void senseGap()
    {
        boolean gap = false;
        int distance;
        
        robot.resetEncodedMotorPosition(RXTXRobot.MOTOR1);
        
        //robot.runMotor(RXTXRobot.MOTOR1, speedL, RXTXRobot.MOTOR2, -speedR, 0);

        
        while (!gap)
        {
            robot.refreshDigitalPins();
            distance = robot.getPing(pingSidePin);

            robot.runMotor(RXTXRobot.MOTOR1, speedL, RXTXRobot.MOTOR2, -speedR, 100);
            
            if(distance > 65)
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
        double temp = (thermistorReading - tempIntercept)/tempSlope;
        
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
        
        double windSpeed = ((anemometerReading-thermistorReading)-windIntercept)/windSlope;
        
        System.out.println("The wind speed is: " + windSpeed + "m/s");
        
    }
    
    private static void getConductivity()
    {
        conductivity = robot.getConductivity();
        System.out.println("ADC code: " + conductivity);
        conductivity = 100*(conductivity - conductivityIntercept)/conductivitySlope;
        System.out.println("Water content: " + conductivity + "%");
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
        //System.out.println("SENSORS READ:");
        //System.out.println("The TEMPERATURE is: " + temp + " celsius");
        //System.out.println("The WIND SPEED is: " + temp + "Whatever the wind units are");
        //System.out.println("The soil CONDUCTIVITY is: " + conductivity);
    }
    
}
