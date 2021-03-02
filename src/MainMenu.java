import java.util.Scanner;

public class MainMenu {
    private String title;
    private String[] menuItems;
    private String leadText;
    private int exitNumber;


    public MainMenu(String title, String[] menuItems, String leadText) {
        this.title = title;
        this.menuItems = menuItems;
        this.leadText = leadText;

        exitNumber = menuItems.length + 1;
    }


    public void setMenuItems (String[] menuItems) {
        this.menuItems = menuItems;
    }

    public void setExitNumber (int exitNumber) {this.exitNumber = exitNumber;}


    public int getExitNumber() {
        return exitNumber;
    }

    public void displayMenu() {
        System.out.println(title);

        for (int i = 1; i <= menuItems.length; i++) {
            System.out.println(i + ". " + menuItems[i - 1]);
        }

        System.out.println(exitNumber + ". " + "Exit");

        System.out.print("\n" + leadText + ": ");

    }


    public int readMenuChoice() {
        int userChoice;

        Scanner in = new Scanner(System.in);

        while (!in.hasNextInt()) {

            System.out.print("Please enter integer: ");
            in.next();
        }

        userChoice = in.nextInt();

        while (userChoice > menuItems.length + 1 || userChoice < 1) {

            System.out.print("Please enter valid Menu number: ");

            if (in.hasNextInt())
                userChoice = in.nextInt();
            else
                in.next();
        }

        return userChoice;


    }
}
