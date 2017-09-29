import rxtxrobot.*;

public class RunTillBump {

    public static RXTXRobot robot;

    public static void main(String[] args)
    {
        boolean bumpTriggered = false;

            //set up arduino
        robot = new ArduinoUno();
        robot.setPort("COM3"); //unsure about port
        robot.connect();

            //run motor
        robot.runMotor(RXTXRobot.MOTOR1, 100, RXTXRobot.MOTOR2, 100, 0);
        //robot.runEncodedMotor(robot.MOTOR1, 100, 600, robot.MOTOR2, 100, 600);

            //set control
        robot.refreshDigitalPins();
        int firstReading = robot.getDigitalPin(11).getValue();

        while (!bumpTriggered)
        {
                robot.refreshDigitalPins();
                int reading = robot.getDigitalPin(11).getValue(); //IDK what value signifies not pushed

                //add if statement to set bumpTriggered based on reading
                if(reading != firstReading) {
                    bumpTriggered = true;
                }

            if(bumpTriggered)
            {
                    //stop motor
                robot.runMotor(RXTXRobot.MOTOR1, 0, RXTXRobot.MOTOR2, 0, 0);
                //runEncodedMotor(robot.MOTOR1, 0, , robot.MOTOR2, 0, 0);  //if we can't call an encoded motor directly
            }

        }
        //close robot
        robot.close();


    }

}
