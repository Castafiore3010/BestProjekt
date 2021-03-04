import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;



public class Main {
    public static Scanner scanner = new Scanner(System.in);
    private static JDBCWriter jdbcWriter = new JDBCWriter();


    public void run() {
        String[] menuItems = new String[]{"Connect to DB", "Cars", "Customers", "Rentals","Selects"};
        String[] subMenuItems = new String[]{"Insert", "Delete", "Update"};
        String[] subMenuItemsCustomer = new String[]{"Insert","Delete","Update"};
        String[] subMenuItemsRC = new String[]{"Insert","Delete","Update"};
        String[] subMenuItemsSelect = new String[] {"View car by ID","View customer by ID","View rental contract by ID"};
        MainMenu menu = new MainMenu("Kailua CarRental", menuItems, "Enter menu number");

        int choice;
        int subMenuChoice;


        boolean run = true;

        boolean connectedToDB = false;

        while (run) {

            boolean subMenuCarsRun = true;
            boolean subMenuCustomerRun = true;
            boolean subMenuRentalContractsRun = true;
            boolean submenuSelectsRun = true;

            menu.displayMenu();

            if (connectedToDB) {
                choice = menu.readMenuChoice() + 1;
            } else
                choice = menu.readMenuChoice();

            switch (choice) {

                case 1: // Connect to db
                    System.out.println("Please enter username");
                    String username = readChoiceString();
                    System.out.println("Please enter password");
                    String password = readChoiceString();


                    boolean b = jdbcWriter.setConnection(username, password);


                    if  (b) {
                        menu.setMenuItems(new String[]{"Cars", "Customers", "Rentals","Select"});
                        menu.setExitNumber(menu.getExitNumber() - 1);
                        connectedToDB = true;

                    } else {
                        System.out.println("Access denied");
                    }
                    if (b)
                        System.out.println("Connection made\n");
                    else
                        System.out.println("No connection made\n");
                    break;

                case 2: // Cars submenu
                    while (subMenuCarsRun) {
                        MainMenu subMenuCars = new MainMenu("Cars -- Exit returns to main menu", subMenuItems,
                                "Enter menu number");
                        subMenuCars.displayMenu();
                        subMenuChoice = subMenuCars.readMenuChoice();


                        switch (subMenuChoice) {
                            case 1: // Insert car
                                scanner.nextLine();
                                Car newCar;
                                newCar = createCar();
                                jdbcWriter.insertCar(newCar);
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

                            case 4:
                                subMenuCarsRun = false;
                                break;

                        }
                        if (subMenuChoice != 4) {
                            System.out.print("\nEnter r to return: ");
                            scanner.next();
                        }
                    } //while loop
                    break;
                case 3: // Customers submenu
                    while (subMenuCustomerRun){
                        MainMenu subMenuCustomers = new MainMenu("Customers -- Exit returns to main menu",
                                subMenuItemsCustomer,"Enter menu number");
                        subMenuCustomers.displayMenu();
                        subMenuChoice = subMenuCustomers.readMenuChoice();


                        switch (subMenuChoice) {
                            case 1: // Insert customer
                                Customer customer;
                                customer = createCustomer();
                                jdbcWriter.insertCustomer(customer);
                                break;
                            case 2: //Delete customer
                                deleteCustomer();
                                break;
                            case 3: //Update customer
                                updateCustomer();
                                break;
                            case 4:
                                subMenuCustomerRun = false;
                                break;
                        }

                        if (subMenuChoice != 4) {
                            System.out.print("\nEnter r to return: ");
                            scanner.next();
                        }
                    } // while loop
                    break;

                case 4: // RentalContract submenu
                    while (subMenuRentalContractsRun) {

                        MainMenu subMenuRentalContracts = new MainMenu("Rental Contracts -- Exit returns to main menu",
                                subMenuItemsRC,"Enter menu number");
                        subMenuRentalContracts.displayMenu();
                        subMenuChoice = subMenuRentalContracts.readMenuChoice();


                        switch(subMenuChoice) {
                            case 1: // Insert
                                RentalContract rentalContract;
                                rentalContract = createRentalContract();
                                jdbcWriter.insertRentalContract(rentalContract);
                                break;
                            case 2: // Delete
                                deleteRentalContract();
                                break;
                            case 3: // Update
                                updateRentalContract();
                                break;
                            case 4:
                                subMenuRentalContractsRun = false;
                                break;
                        }

                        if (subMenuChoice != 4) {
                            System.out.print("\nEnter r to return: ");
                            scanner.next();
                        }
                    }
                    break;

                case 5: //Select submenu
                    while (submenuSelectsRun) {
                        MainMenu subMenuSelects = new MainMenu("Rental Contracts -- Exit returns to main menu",
                                subMenuItemsSelect, "Enter menu number");
                        subMenuSelects.displayMenu();
                        subMenuChoice = subMenuSelects.readMenuChoice();

                       switch(subMenuChoice){
                           case 1://View car
                               System.out.println("Please enter car_id:");
                               int id = readChoiceInt();
                               String car_str = jdbcWriter.getCarFromDBbyIndex(id).toString();
                               System.out.println(car_str);
                               break;
                           case 2://View customer
                               System.out.println("Please enter customer_id:");
                               int id2 = readChoiceInt();
                               String customer_str = jdbcWriter.getCustomerFromDBbyId(id2).toString();
                               System.out.println(customer_str);
                               break;
                           case 3://View rental
                               System.out.println("Please enter rental contract id:");
                               int id3 = readChoiceInt();
                               String rental_str = jdbcWriter.getRentalContractByRCID(id3).toString();
                               System.out.println(rental_str);
                               break;
                           case 4:
                               submenuSelectsRun=false;
                               break;

                       }
                        if (subMenuChoice != 4) {
                            System.out.print("\nEnter r to return: ");
                            scanner.next();
                        }

                    }
                    break;

                case 6:
                    System.out.println("Thank you for using kailua database management app");
                    run = false;
                    break;
            }
        }
    }


    public RentalContract createRentalContract() {
        RentalContract rentalContract;
        LocalDateTime start_time;
        String start_timestr;
        String end_timestr;
        LocalDateTime end_time;
        double max_km;
        int customer_id;
        int car_id;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        System.out.println("Enter start_time: ");
        start_timestr = readChoiceString();
        start_time = LocalDateTime.parse(start_timestr,formatter);
        System.out.println("Enter expected end_time: ");
        end_timestr = readChoiceString();
        end_time = LocalDateTime.parse(end_timestr,formatter);
        System.out.println("Enter max km: ");
        max_km = readChoiceDouble();
        System.out.println("Enter customer_id: ");
        customer_id = readChoiceInt();
        System.out.println("Enter car_id: ");
        car_id = readChoiceInt();
        scanner.nextLine(); // scannerbug


        int counter=0;
        int control = jdbcWriter.isCarAvailable(start_timestr,end_timestr,car_id);

        while (control >0){ //Kontrollerer at bilen ikke er reserveret.
            System.out.println("Car is already reserved for this time period. Please enter a new car id: ");
            car_id=readChoiceInt();
            counter++;
            if (counter>5) {
                System.out.println("It looks like you can't get any of the cars you want. Do you want to create a new car? " +
                        "1 for yes, 2 for no.");
                int yesOrNo = readChoiceInt();
                scanner.nextLine(); //scannerbug

                if (yesOrNo==1) {
                    Car car = createCar();
                    jdbcWriter.insertCar(car);
                    car_id = jdbcWriter.getLatestCarIndex();
                    control=0;
                } else {
                    return null;
                }
            }
        }

        if (jdbcWriter.checkCustomerExists(customer_id) != 1) { //Kontrol af kunden findes
            System.out.println("It looks like the customer doesn't exist.. Do you wish to create the customer?" +
                    " 1 for yes, 2 for no.");
            int yesOrNo = readChoiceInt();
            scanner.nextLine(); //scannerbug
            if (yesOrNo==1) {
                Customer newCustomer = createCustomer();
                jdbcWriter.insertCustomer(newCustomer);
                customer_id = jdbcWriter.getCustomerIdFromDB(newCustomer.getEmail());

            } else {
                return null;
            }
        }//checkCustomerExists

        if(jdbcWriter.checkCarExists(car_id) != 1){ //Kontrol af bilen findes
            System.out.println("It looks like the car doesn't exist.. Do you wish to create a new car? " +
                    "1 for yes, 2 for no.");
            int yesOrNo = readChoiceInt();
            scanner.nextLine(); //scannerbug

            if (yesOrNo==1) {
                Car car = createCar();
                jdbcWriter.insertCar(car);
                car_id = jdbcWriter.getLatestCarIndex();

            } else {
                return null;
            }
        }

        rentalContract = new RentalContract(start_time,end_time,max_km,customer_id,car_id);

        return rentalContract;
    }

    public void deleteRentalContract() {

        System.out.println("Please enter ID number of rental contract you wish to delete: ");
        int rental_id = readChoiceInt();
        RentalContract rentalContract = jdbcWriter.getRentalContractByRCID(rental_id);
        int customer_id = rentalContract.getCustomer_id();

        jdbcWriter.deleteRentalContract(rental_id);

        System.out.println("Successfully deleted the rental contract with contract_id: "+rental_id+
                "\nassociated with customer_id: "+customer_id);

    }

    public void updateRentalContract() {
        System.out.println("Please enter ID of the rental contract you wish to update");
        int rental_contract_id = readChoiceInt();
        RentalContract rentalContract = jdbcWriter.getRentalContractByRCID(rental_contract_id);
        RentalContract oldRentalContract = jdbcWriter.getRentalContractByRCID(rental_contract_id);


        String[] rcItems = new String[5];

        rcItems[0] = rentalContract.getRental_start()+ ": current rental start_time";
        rcItems[1] = rentalContract.getRental_end() + ": current rental end_time";
        rcItems[2] = rentalContract.getMax_km() + ": current max km";
        rcItems[3] = rentalContract.getCustomer_id() + ": current customer ID";
        rcItems[4] = rentalContract.getCar_id() + ": current car ID";

        MainMenu updateRentalContractMenu = new MainMenu("Update Rental Contract Menu", rcItems, "Enter menu number");

        boolean run = true;


        while (run) {

            updateRentalContractMenu.displayMenu();
            int rentalContractsMenuChoice = readChoiceInt();
            scanner.nextLine();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");


            switch (rentalContractsMenuChoice) {
                case 1:
                    System.out.println("Enter new start_time: ");
                    String new_start_time_str = readChoiceString();
                    LocalDateTime new_start_time = LocalDateTime.parse(new_start_time_str,formatter);
                    rentalContract.setRental_start(new_start_time);
                    rcItems[0] = rentalContract.getRental_start() + ": current start_time";
                    break;
                case 2:
                    System.out.println("Enter new end_time: ");
                    String new_end_time_str = readChoiceString();
                    LocalDateTime new_end_time = LocalDateTime.parse(new_end_time_str,formatter);
                    rentalContract.setRental_start(new_end_time);
                    rcItems[1] = rentalContract.getRental_end() + ": current end_time";
                    break;
                case 3:
                    System.out.println("Enter new max_km: ");
                    double new_max_km = readChoiceDouble();
                    rentalContract.setMax_km(new_max_km);
                    rcItems[2] = rentalContract.getMax_km() + ": current max_km";
                    break;
                case 4:
                    System.out.println("Enter new customer ID: ");
                    int new_customer_id = readChoiceInt();
                    rentalContract.setCustomer_id(new_customer_id);
                    rcItems[3] = rentalContract.getCustomer_id() + ": current customer ID";
                    break;
                case 5:
                    System.out.println("Enter new car ID: ");
                    int new_car_id = readChoiceInt();
                    rentalContract.setCar_id(new_car_id);
                    rcItems[4] = rentalContract.getCar_id() + ": current car ID";
                    break;
                case 6:
                    jdbcWriter.updateRentalContract(rental_contract_id,rentalContract,oldRentalContract);
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

    public Customer createCustomer() {
        Customer customer;
        String first_name;
        String last_name;
        String email;
        int address_id;
        String address_name;
        int zipcode;
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
        zipcode = readChoiceInt();
        System.out.println("Enter city name: ");
        scanner.nextLine();
        city_name = readChoiceString();



        jdbcWriter.insertCity(city_name);
        city_id = jdbcWriter.getCityIDFromDbByCityName(city_name);
        jdbcWriter.insertZipCode(zipcode,city_id);
        zipcode_id = jdbcWriter.getZipIDFromDbByCityId(city_id);
        jdbcWriter.insertAddress(address_name,zipcode_id);


        address_id = jdbcWriter.getAddressIDFromDbByAddressName(address_name);
        city_id = jdbcWriter.getCityIDFromDbByCityName(city_name);
        zipcode_id = jdbcWriter.getZipcodeIdFromDbByAddressId(address_id);
        customer = new Customer(first_name,last_name,email,address_id,address_name,zipcode, zipcode_id,city_id,city_name);


        return customer;
    }

    public void deleteCustomer() {

        System.out.println("Please enter ID number of customer you wish to delete: ");
        int customer_id = readChoiceInt();
        Customer customer = jdbcWriter.getCustomerFromDBbyId(customer_id);
        String fname = customer.getFirst_name();
        String lname = customer.getLast_name();

        int address_id = customer.getAddress_id();
        int zipcode_id = customer.getZipcode_id();

        int city_id = customer.getCity_id();
        System.out.println(address_id+" "+zipcode_id+" "+city_id);

        jdbcWriter.deleteCustomer(customer_id);
        jdbcWriter.deleteAddressByAddressId(customer_id);
        jdbcWriter.deleteZipCodeIdByAddressId(customer_id);
        jdbcWriter.deleteCityByCityId(customer_id);



        System.out.println("Successfully deleted the customer with customer id: "+customer_id+
                "\nand the name: "+fname+" "+lname);

    }



    public void updateCustomer() {
        System.out.println("Please enter ID of the customer you wish to update");
        int customer_id = readChoiceInt();
        Customer customer = jdbcWriter.getCustomerFromDBbyId(customer_id);
        Customer oldCustomerCopy = jdbcWriter.getCustomerFromDBbyId(customer_id);


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
                    System.out.println("Enter new first name: ");
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
                    customerItems[5] = customer.getCity_name() + ": current city_name";
                    break;
                case 7:
                    jdbcWriter.updateCustomer(customer_id, oldCustomerCopy, customer);
                    run = false;
                    break;



            }

        }

    }

    public void updateCar() {
        System.out.println("Please enter the ID of car you wish to update");
        int car_id = readChoiceInt();
        Car car = jdbcWriter.getCarFromDBbyIndex(car_id);

        String[] carItems = new String[7];

        carItems[0] = car.getModel_name() + ": current model_name";
        carItems[1] = car.getRegistration_number() + ": current reg_nr";
        carItems[2] = car.getFirst_registration() + ": current first_reg_date";
        carItems[3] = car.getOdometer() + ": current odometer reading";
        carItems[4] = car.getCar_group_id() + ": current car_grp_id";
        carItems[5] = car.getBrand_id() + ": current brand_id";
        carItems[6] = car.getFuelType_id() + ": current fuelType_id";

        MainMenu updateCarMenu = new MainMenu("Update Car Menu", carItems, "Enter menu number");

        boolean run = true;

        while (run) {

            updateCarMenu.displayMenu();
            int carMenuChoice = readChoiceInt();
            scanner.nextLine();


            switch (carMenuChoice) {
                case 1:
                    System.out.println("Enter new model name: ");
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

