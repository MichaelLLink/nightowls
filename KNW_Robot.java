import RTXTRobot.*;

public class KNW_Robot {

            //attach motors
    //attachMotor(int motor, int pin)   //example: robot.attachMotor(RXTXRobot.MOTOR3, 7) --motor is DC, pin is digital
            //attach servos
    //attachServo(int servo, int pin)   //example: robot.attachServo(RXTXRobot.SERVO1, 9) --pin is digital

    //NOTE: We don't have to call attach() for ping sensors BUT if something is attached to the pin we call for the sensor, we'll get an error value (-1)

    //variables for probes
    //variables for probe data


    public static void main()
    {
                //create robot object
        RXTXRobot robot = new ArduinoUno();
        //robot.setPort("...");     //gotta put an actual port in here ¯\_(ツ)_/¯
        //robot.connect();
                //check robot connection

                //implement robot
        //sequential function calls for whatever course
            //move till we hit barracade (ping)
            //move till we level out --gyroscope (there's a function for that)
            //raiseLift
            //take temp (idk if there's a function for that)
            //lowerLift
            //turn
            //move until gap is sensed (ping sensor)
            //turn
            //move through gap
            //turn
            //move across bridge, stop when we hit the wall (I guess) then back up a lil so we can turn
            //turn
            //move until we hit water/sand bucket
            //drop arm
            //if there's enough water (call conductivity function)
                //releaseBeacon
            //raise arm

                //close robot
        //robot.close();
    }

    //void move --move until obstacle is hit
    public static void move()
    {

    }

    //void move(float)  --move for specified distance
    public static void move(float distance)
    {
        //calc ticks
        //while distance isn't reached
            //interface with motor controllers to check distanceTraveled
            //if distanceTraveled isn't equal to distance
                //check forward sensor for obstical
                //if there is NO OBSTICAL
                    //continue rotating wheels
                //if there is an OBSTICAL
                    //wait
    }

    //void turn(int)    --Left:  1, Right: 2
    public static void turn(int direction)
    {
        //if direction is 1
            //runEncodedMotor(robot.MOTOR1, -500, [whatever ticks it needs for 90 degrees], robot.MOTOR2, 500, [same ticks])
        //if direction is 2
            //runEncodedMotor(robot.MOTOR1, 500, [same ticks], robot.MOTOR2, -500, [same ticks])

    }

    //senseBarrier      --not sure exactly what we want these to do, like do they handle the barrier or just sense if there is one?
                        //--there's a ping sensor function we can use (int getPing(int pin)) example: int distance = robot.getPing(6);
    //void senseGap     --again, obstical handling??? Does this turn for us?
                        //do we need functions for these or can we just interface with the probes directly in main?
    //float senseTemp   --probably gonna use something like refreshAnalogPins()
                        /* EXAMPLE
                            robot.refreshAnalogPins();
                            int reading = robot.getAnalogPin(3).getValue();
                            System.out.println("The analog reading on pin 3 was: " + reading);
                         */
    //float senseWind
    //float senseWater  --there's actually a function for that in the rxtx package (int getConductivity()) so we probably don't need this function (woot woot)
                        //NOTE: The conductivity sensor requires pins on digital pins 12 and 13, and pins on analog pins 4 and 5.

    //void raiseLift
    public static void raiseLift()
    {

    }

    //void lowerLift
    public static void lowerLift()
    {

    }

    //void dropArm
    public static void dropArm()
    {

    }

    //void releaseBeacon
    public static void releaseBeacon()
    {

    }

    //void outputData   --read out to terminal, I guess
    public static void outputData()
    {

    }

    //void storeData    --would store to external file
    public static void storeData()
    {

    }
}
