import rxtxrobot.*;
import java.util.Scanner;

public class Sprint2 {

    public static RXTXRobot robot;

    public static void main(String[] args)
    {
            //set up robot
        robot = new ArduinoUno();
        robot.setPort("COM3"); //unsure about port
        robot.connect();

            //set up scanner and input
        Scanner input = new Scanner(System.in);
        int iChoice = 0;
        int iSelect = 0;

        do
        {
                //display menu
            displayMenu();

                //scan for input
            iChoice = input.nextInt();

                //begin switch loop for selections
            switch(iChoice)
            {
                case 1:

            }

        }while(iChoice != 0)

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
        //System.out.println("6 - Conductivity/Anemometer Test");
        System.out.println("0 - Exit");
    }

}
