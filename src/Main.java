import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static JDBCWriter jdbcWriter = new JDBCWriter();
    private ArrayList<Car> cars;


    public void run() {
        String[] menuItems = new String[]{"Connect to DB", "Cars", "Customers", "Rentals"};
        String[] subMenuItems = new String[]{"Insert", "Delete", "Update", "View all Cars"};
        MainMenu menu = new MainMenu("Kailua CarRental", menuItems, "Enter menu number");
        String returnStr = "\nPress any key to return: ";
        JDBCWriter jdbcWriter = new JDBCWriter();
        int choice;
        int subMenuChoice;


        boolean run = true;

        boolean connectedToDB = false;

        while (run) {

            boolean subMenuRun = true;

            menu.displayMenu();

            if (connectedToDB) {
                choice = menu.readMenuChoice() + 1;
            } else
                choice = menu.readMenuChoice();

            switch (choice) {

                case 1: // Connect to db
                    boolean b = jdbcWriter.setConnection();
                    menu.setMenuItems(new String[]{"Cars", "Customers", "Rentals"});
                    menu.setExitNumber(menu.getExitNumber() - 1);
                    connectedToDB = true;
                    cars = jdbcWriter.getCarsFromDatabase();
                    if (b)
                        System.out.println("Connection made\n");
                    else
                        System.out.println("No connection made\n");
                    break;

                case 2: // Cars submenu
                    while (subMenuRun) {
                        MainMenu subMenuCars = new MainMenu("Cars -- Exit returns to main menu", subMenuItems,
                                "Enter menu number");
                        subMenuCars.displayMenu();
                        subMenuChoice = subMenuCars.readMenuChoice();


                        switch (subMenuChoice) {
                            case 1: // Insert car
                                scanner.nextLine();
                                Car newCar;
                                newCar = createCar();
                                jdbcWriter.writeLines(newCar);
                                break;
                            case 2: //Delete car
                                System.out.println("Please enter ID number of car you wish to delete: ");
                                int deleteChoice = readChoiceInt();
                                jdbcWriter.deleteCar(deleteChoice);
                                System.out.println("Successfully deleted the car with car_id: " +deleteChoice);
                                break;
                            case 3: //Update car
                                updateCar();
                                break;
                            case 4: //View all cars
                                displayList(cars);
                                break;
                            case 5:
                                subMenuRun = false;
                                break;

                        }
                        if (subMenuChoice != 5) {
                            System.out.print("\nEnter r to return: ");
                            scanner.next();
                        }
                    }
                    break;
                case 3: // Customers submenu
                    MainMenu subMenuCustomers = new MainMenu("Customers -- Exit returns to main menu",
                            subMenuItems,"Enter menu number");
                    subMenuCustomers.displayMenu();
                    subMenuChoice = subMenuCustomers.readMenuChoice();


                    switch (subMenuChoice) {
                        case 1: // Insert customer
                            Customer customer=new Customer();
                            customer = createCustomer();
                            jdbcWriter.insertCustomer(customer);
                            break;
                        case 2: //Delete customer
                            System.out.println("Please enter ID number of customer you wish to delete: ");
                            System.out.println("Enter first name of customer");
                            String fname = readChoiceString();
                            System.out.println("Enter last name of customer");
                            String lname = readChoiceString();
                            int id = jdbcWriter.getCustomerIdFromDB(fname,lname);
                            jdbcWriter.deleteCustomer(id);
                            System.out.println("Successfully deleted the customer with customer id: "+id+
                                    "\nand the name: "+fname+" "+lname);
                            break;
                        case 3: //Update customer
                            updateCustomer();
                            break;
                        case 4: //View all cars
                            displayList(cars);
                            break;
                        case 5:
                            subMenuRun = false;
                            break;

                    }
                    if (subMenuChoice != 5) {
                        System.out.print("\nEnter r to return: ");
                        scanner.next();
                    }

                    break;

                case 5:
                    System.out.println("Thank you for using kailua database management app");
                    run = false;
                    break;

            }

        }


    }


    public int readChoiceInt() {
        int userChoice;


        while (!scanner.hasNextInt()) {

            System.out.print("Please enter integer: ");
            scanner.next();
        }
        userChoice = scanner.nextInt();


        return userChoice;
    }

    public String readChoiceString() {
        String userChoice;


        while (!scanner.hasNextLine()) {

            System.out.print("Please enter string: ");
            scanner.next();
        }
        userChoice = scanner.nextLine();


        return userChoice;
    }

    public double readChoiceDouble() {
        double userChoice;


        while (!scanner.hasNextDouble()) {

            System.out.print("Please enter double: ");
            scanner.next();
        }
        userChoice = scanner.nextDouble();


        return userChoice;
    }


    public Car createCar() {
        Car car;
        String model_name;
        String registration_number;
        LocalDate first_registration;
        double odometer;
        int car_group_id;
        int brand_id;
        int fuelType_id;


        System.out.println("Enter Model name: ");
        model_name = readChoiceString();
        System.out.println("Enter Registration number: ");
        registration_number = readChoiceString();
        first_registration = LocalDate.now();
        System.out.println("Enter amount on Odometer: ");
        odometer = readChoiceDouble();
        System.out.println("Enter car_group_id (1. Luxury, 2. Family, 3. Sport): ");
        car_group_id = readChoiceInt();
        System.out.println("Enter brand_id (1. Tesla, 2. Ford, 3. BMW): ");
        brand_id = readChoiceInt();
        System.out.println("Enter fuelType_id (1. El, 2. Diesel, 3. Benzin)");
        fuelType_id = readChoiceInt();

        car = new Car(model_name, registration_number, first_registration,
                odometer, car_group_id, brand_id, fuelType_id);

        return car;
    }


    public void displayList(ArrayList<Car> cars) {

        cars = jdbcWriter.getCarsFromDatabase();

        for (Car car : cars) {
            System.out.println("Car id: " + "#" + car.getCar_id());
            System.out.println("Model name: " + car.getModel_name());
            System.out.println("Registration number: " + car.getRegistration_number());
            System.out.println("First registration date: " + car.getFirst_registration());
            System.out.println("Kilometers traveled: " + car.getOdometer());
            String carGrp;

            switch (car.getCar_group_id()) {
                case 1:
                    carGrp = "Luxury";
                    break;
                case 2:
                    carGrp = "Family";
                    break;
                case 3:
                    carGrp = "Sports";
                    break;
                default:
                    return;
            }

            System.out.println("Car group:" + "\n--ID:" + "#" + car.getCar_group_id() + "\n--Group: " + carGrp);
            System.out.println(("Brand: " + " \n--ID:" + "#" + car.getBrand_id() + "\n--Name: " +
                    jdbcWriter.getBrandNameFromDB(car.getBrand_id())));
            String fuelType;

            switch (car.getFuelType_id()) {
                case 1:
                    fuelType = "El";
                    break;
                case 2:
                    fuelType = "Diesel";
                    break;
                case 3:
                    fuelType = "Benzin";
                    break;
                default:
                    return;
            }

            System.out.println("Fuel Type:" + "\n--ID:" + "#" + car.getFuelType_id() + "\n--Fuel: " + fuelType);

        }
    }

/*
    public int getCarIndexById(int id) {
        int carId;
        int indexOfCar = -1;

        cars = jdbcWriter.getCarsFromDatabase();


        for (Car car : cars) {
            if (car.getCar_id() == id) {
                    indexOfCar = cars.indexOf(car);
                } else {
                    indexOfCar = -1;
                }
            }

        return indexOfCar;
    }

 */ // Gammel metode

    public Customer createCustomer() {
        Customer customer;
        String first_name;
        String last_name;
        String email;
        int address_id;
        String address_name;
        int zipcode_id;
        int city_id;
        String city_name;

        //Contact
        System.out.println("Enter first name: ");
        first_name = readChoiceString();
        System.out.println("Enter last name: ");
        last_name = readChoiceString();
        System.out.println("Enter email: ");
        email = readChoiceString();

        //Address
        System.out.println("Enter address_name: ");
        address_name = readChoiceString();
        System.out.println("Enter zipcode ");
        zipcode_id = readChoiceInt();
        System.out.println("Enter city name: ");
        scanner.nextLine();
        city_name = readChoiceString();



        jdbcWriter.insertCity(city_name);
        if (!jdbcWriter.zipCodeExists(zipcode_id)){
            jdbcWriter.insertZipCode(zipcode_id,city_name);
        }

        jdbcWriter.insertAddress(address_name,zipcode_id);
        address_id = jdbcWriter.getAddressIDFromDbByAddressName(address_name);
        city_id = jdbcWriter.getCityIDFromDbByCityName(city_name);
        customer = new Customer(first_name,last_name,email,address_id,address_name,zipcode_id,city_id,city_name);

        return customer;
    }

    public void updateCustomer() {
        System.out.println("Please enter ID of the customer you wish to update");
        int customer_id = readChoiceInt();
        Customer customer = jdbcWriter.getCustomerFromDBbyId(customer_id);


        String[] customerItems = new String[6];

        customerItems[0] = customer.getFirst_name() + ": current first name";
        customerItems[1] = customer.getLast_name() + ": current last name";
        customerItems[2] = customer.getEmail() + ": current email";
        customerItems[3] = customer.getAddress_name() + ": current address name";
        customerItems[4] = ""+customer.getZipcode() + ": current zipcode";
        customerItems[5] = customer.getCity_name() + ": current city name";


        MainMenu updateCustomerMenu = new MainMenu("Update Customer Menu", customerItems, "Enter menu number");

        boolean run = true;


        while (run) {

            updateCustomerMenu.displayMenu();
            int customerMenuChoice = readChoiceInt();
            scanner.nextLine();


            switch (customerMenuChoice) {
                case 1:
                    System.out.println("Enter new desired first name: ");
                    String newFirstName = readChoiceString();
                    customer.setFirst_name(newFirstName);
                    customerItems[0] = customer.getFirst_name() + ": current first name";
                    break;
                case 2:
                    System.out.println("Enter new last name: ");
                    String newLastName = readChoiceString();
                    customer.setLast_name(newLastName);
                    customerItems[1] = customer.getLast_name() + ": current last name";
                    break;
                case 3:
                    System.out.println("Enter new email: ");
                    String newEmail = readChoiceString();
                    customer.setEmail(newEmail);
                    customerItems[2] = customer.getEmail() + ": current email";
                    break;
                case 4:
                    System.out.println("Enter new street address: ");
                    String newAddress = readChoiceString();
                    customer.setAddress_name(newAddress);
                    customerItems[3] = customer.getAddress_name() + ": current address";
                    break;
                case 5:
                    System.out.println("Enter new zipcode: ");
                    int newZipCode = readChoiceInt();
                    customer.setZipcode(newZipCode);
                    customerItems[4] = customer.getZipcode() + ": current zipcode";
                    break;
                case 6:
                    System.out.println("Enter new city name: ");
                    String newCityName = readChoiceString();
                    customer.setCity_name(newCityName);
                    customerItems[5] = customer.getCity_name() + ": current brand_id";
                    break;
                default:
                    run = false;

                    //No break lol

            }
            System.out.println("SE MIG SE MIG"+customer_id);
            jdbcWriter.updateCustomer(customer_id,customer);
        }

    }

    public void updateCar() {
        System.out.println("Please enter ID of car you wish to update");
        int car_id = readChoiceInt();
        Car car = jdbcWriter.getCarFromDBbyIndex(car_id);

        String[] carItems = new String[7];

        carItems[0] = car.getModel_name() + ": current model_name";
        carItems[1] = car.getRegistration_number() + ": current reg_nr";
        carItems[2] = String.valueOf(car.getFirst_registration()) + ": current first_reg_date";
        carItems[3] = String.valueOf(car.getOdometer() + ": current odometer reading");
        carItems[4] = String.valueOf(car.getCar_group_id() + ": current car_grp_id");
        carItems[5] = String.valueOf(car.getBrand_id() + ": current brand_id");
        carItems[6] = String.valueOf(car.getFuelType_id() + ": current fuelType_id");

        MainMenu updateCarMenu = new MainMenu("Update Car Menu", carItems, "Enter menu number");

        boolean run = true;


        while (run) {

            updateCarMenu.displayMenu();
            int carMenuChoice = readChoiceInt();
            scanner.nextLine();


            switch (carMenuChoice) {
                case 1:
                    System.out.println("Enter new desired model name: ");
                    String newModelName = readChoiceString();
                    car.setModel_name(newModelName);
                    carItems[0] = car.getModel_name() + ": current model_name";
                    break;
                case 2:
                    System.out.println("Enter new registration number: ");
                    String newRegNr = readChoiceString();
                    car.setRegistration_number(newRegNr);
                    carItems[1] = car.getRegistration_number() + ": current reg_nr";
                    break;
                case 3:
                    System.out.println("Enter new first registration date: ");
                    LocalDate firstRegDate = LocalDate.parse(readChoiceString());
                    car.setFirst_registration(firstRegDate);
                    carItems[2] = String.valueOf(firstRegDate + ": current regDate");
                    break;
                case 4:
                    System.out.println("Enter new odometer reading: ");
                    double odometer = readChoiceDouble();
                    car.setOdometer(odometer);
                    carItems[3] = String.valueOf(car.getOdometer() + ": current odometer reading");
                    break;
                case 5:
                    System.out.println("Enter new car group: ");
                    int newCarGrpId = readChoiceInt();
                    car.setCar_group_id(newCarGrpId);
                    carItems[4] = String.valueOf(car.getCar_group_id() + ": current car_grp_id");
                    break;

                case 6:
                    System.out.println("Enter new brand id: ");
                    int newBrandId = readChoiceInt();
                    car.setBrand_id(newBrandId);
                    carItems[5] = String.valueOf(car.getBrand_id() + ": current brand_id");
                    break;

                case 7:
                    System.out.println("Enter new fuelType id: ");
                    int newFuelTypeId = readChoiceInt();
                    car.setFuelType_id(newFuelTypeId);
                    carItems[6] = String.valueOf(car.getFuelType_id() + ": current fuelType_id");
                    break;

                case 8:
                    run = false;
                    break;


            }
        }
        jdbcWriter.updateCar(car_id, car);

    }



    public static void main(String[] args) {

        new Main().run();




    }
}

