import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Vector;

public class JDBCWriter {
    private static Connection connection = null;


    public boolean setConnection() {
        final String url = "jdbc:mysql://127.0.0.1:3306/kailuacarrental?serverTimezone=UTC";
        boolean bres = false;

        try {
            connection = DriverManager.getConnection(url, "root", "fisse123");
            bres = true;
        } catch (SQLException error) {
            System.out.println("\nNo connection made");
        }
        return bres;

    }

    public int updateCar(int carId, Car car) {

        String model_name = car.getModel_name();
        String registration_number = car.getRegistration_number();
        LocalDate first_registration = car.getFirst_registration();
        double odometer = car.getOdometer();
        int car_group_id = car.getCar_group_id();
        int brand_id = car.getBrand_id();
        int fuelType_id = car.getFuelType_id();


        PreparedStatement preparedStatement;
        String updateStr = "UPDATE cars SET model_name = ?, registration_number = ?, first_registration = ?," +
                " odometer = ?, car_group_id = ?," +
                "brand_id = ?, fueltype_id = ? where car_id = ?";

        int result = -1;


        try {
            preparedStatement = connection.prepareStatement(updateStr);
            preparedStatement.setString(1, model_name);
            preparedStatement.setString(2,registration_number);
            preparedStatement.setString(3,first_registration.toString());
            preparedStatement.setDouble(4,odometer);
            preparedStatement.setInt(5,car_group_id);
            preparedStatement.setInt(6,brand_id);
            preparedStatement.setInt(7, fuelType_id);
            preparedStatement.setInt(8, carId);
            result = preparedStatement.executeUpdate();


        } catch (SQLException error) {
            error.printStackTrace();
        }
        return result;


    }

    public ArrayList<Car> getCarsFromDatabase() {
        ArrayList<Car> cars = new ArrayList<>();

        PreparedStatement preparedStatement;
        String searchStr = "SELECT * from cars";

        try {
            preparedStatement = connection.prepareStatement(searchStr);
            ResultSet resultSet = preparedStatement.executeQuery();

            int i = 1;
            while (resultSet.next()) {

                String car_id = "" + resultSet.getObject("car_id");
                Integer help = Integer.parseInt(car_id);
                String model_name = "" + resultSet.getObject("model_name");
                String registration_number = "" + resultSet.getObject("registration_number");
                LocalDate first_registration = LocalDate.parse("" + resultSet.getObject("first_registration"));
                String odometer =  "" + resultSet.getObject("odometer");
                Double odometer2 = Double.parseDouble(odometer);
                int car_group_id = (int) resultSet.getObject("car_group_id");
                int brand_id = (int) resultSet.getObject("brand_id");
                int fuelType_id = (int) resultSet.getObject("fueltype_id");

                Car car = new Car(model_name, registration_number, first_registration, odometer2, car_group_id,
                        brand_id, fuelType_id);

                car.setCar_id(help);

                cars.add(car);
            }

        } catch (SQLException error) {
            error.printStackTrace();
        }

        return cars;


    }


    public Car getCarFromDBbyIndex(int id) {
        ArrayList<Car> cars = new ArrayList<>();
        PreparedStatement preparedStatement;

        String searchStr = "SELECT * from cars where car_id = ?";



        try {
            preparedStatement = connection.prepareStatement(searchStr);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                String car_id = "" + resultSet.getObject("car_id");
                Integer tempCar_id = Integer.parseInt(car_id);
                String model_name = "" + resultSet.getObject("model_name");
                String registration_number = "" + resultSet.getObject("registration_number");
                LocalDate first_registration = LocalDate.parse("" + resultSet.getObject("first_registration"));
                String odometer =  "" + resultSet.getObject("odometer");
                Double odometer2 = Double.parseDouble(odometer);
                int car_group_id = (int) resultSet.getObject("car_group_id");
                int brand_id = (int) resultSet.getObject("brand_id");
                int fuelType_id = (int) resultSet.getObject("fueltype_id");

                Car car = new Car(model_name, registration_number, first_registration, odometer2, car_group_id,
                        brand_id, fuelType_id);

                car.setCar_id(tempCar_id);

                cars.add(car);



            }

        } catch (SQLException error) {
            error.printStackTrace();
        }



        return cars.get(0);



    }

    public int getLatestCarIndex() {
        PreparedStatement preparedStatement;
        String searchStr = "SELECT max(car_id) from cars";
        String result = "";

        try {
            preparedStatement = connection.prepareStatement(searchStr);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                result = "" + resultSet.getObject(1);
            }

        } catch (SQLException error) {
            error.printStackTrace();
        }

        return Integer.parseInt(result);

    }

    public Car getLatestCar(int index) {
        ArrayList<Car> cars = new ArrayList<>();
        PreparedStatement preparedStatement;
        String searchStr = "SELECT * from cars where car_id = ?";

        try {
            preparedStatement = connection.prepareStatement(searchStr);
            preparedStatement.setInt(1,index);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                String car_id = "" + resultSet.getObject("car_id");
                Integer help = Integer.parseInt(car_id);
                String model_name = "" + resultSet.getObject("model_name");
                String registration_number = "" + resultSet.getObject("registration_number");
                LocalDate first_registration = LocalDate.parse("" + resultSet.getObject("first_registration"));
                String odometer =  "" + resultSet.getObject("odometer");
                Double odometer2 = Double.parseDouble(odometer);
                int car_group_id = (int) resultSet.getObject("car_group_id");
                int brand_id = (int) resultSet.getObject("brand_id");
                int fuelType_id = (int) resultSet.getObject("fueltype_id");

                 Car car = new Car(model_name, registration_number, first_registration, odometer2, car_group_id,
                        brand_id, fuelType_id);

                car.setCar_id(help);

                cars.add(car);
            }

        } catch (SQLException error) {
            error.printStackTrace();
        }

        Car tempCar;

        int length = cars.size();
        tempCar = cars.get(length - 1);

        return tempCar;



    }

    public int getCarIdFromDB(String modelName) {

        PreparedStatement preparedStatement;
        String searchStr = "SELECT car_id from cars where model_name = ?";
        int result = -1;

        try {
            preparedStatement = connection.prepareStatement(searchStr);
            preparedStatement.setString(1, modelName);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                result = resultSet.getInt(1);
            }

        } catch (SQLException error) {
            error.printStackTrace();
        }

        return result;


    }



    public String getBrandNameFromDB(int id) {

        PreparedStatement preparedStatement;
        String searchStr = "SELECT brand_name from brands where brand_id = ?";
        String result = "";

        try {
            preparedStatement = connection.prepareStatement(searchStr);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                result = "" + resultSet.getObject(1);
            }

        } catch (SQLException error) {
            error.printStackTrace();
        }

        return result;

    }


    public int deleteCar(int id) {

        String deleteString = "DELETE from cars where car_id = ?";
        PreparedStatement preparedStatement;
        int result = -1;

        try {
            preparedStatement = connection.prepareStatement(deleteString);
            preparedStatement.setInt(1,id);
            result = preparedStatement.executeUpdate();

        } catch (SQLException error) {
            error.printStackTrace();
        }
        return result;
    }

    public int searchDB(String url, String line) {

        String searchStr = "SELECT count(*) from urlreads where url like ? and line like ?";
        PreparedStatement preparedStatement;
        int result = -1;

        try {

            preparedStatement = connection.prepareStatement(searchStr);
            preparedStatement.setString(1, "%" + url + "%");
            preparedStatement.setString(2, "%" + line + "%");
            ResultSet resSet = preparedStatement.executeQuery();
            if (resSet.next()) {
                String str = "" + resSet.getObject(1);
                result = Integer.parseInt(str);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return result;
    }


    public int writeLines(Car car) throws NullPointerException {

        String insstr = "INSERT INTO cars(model_name, registration_number, first_registration, " +
                "odometer, car_group_id, brand_id, fueltype_id) " +
                "VALUES ('" + car.getModel_name() + "','" + car.getRegistration_number() + "','" +
                car.getFirst_registration() + "','" + car.getOdometer() + "','" + car.getCar_group_id() +
                "','" + car.getBrand_id() + "','" + car.getFuelType_id() + "')";

        PreparedStatement preparedStatement;
        int result = 0;


        try {
            preparedStatement = connection.prepareStatement(insstr);
            int rowCount = preparedStatement.executeUpdate();
            result = rowCount;

        } catch (SQLException sqlerror) {
            System.out.println("sql fejl i writeline=" + sqlerror.getMessage());
        }


        System.out.println("\nSuccesfully saved car entry to DataBase\n");
        return result;

    }



    // ---------------------------------------------------
    // ---------------------------------------------------
    // ---------------------------------------------------
    // ---------------------------------------------------
    // ---------------------------------------------------
    // ---------------------------------------------------
    // ---------------------------------------------------
    // ---------------------------------------------------

    public int insertCustomer(Customer customer) throws NullPointerException {

        String insstr = "INSERT INTO customers(first_name, last_name, email, address_id) " +
                "VALUES ('" + customer.getFirst_name() + "','" + customer.getLast_name() + "','" +
                customer.getEmail() + "','" + customer.getAddress_id() + "')";

        PreparedStatement preparedStatement;
        int result = 0;

        try {
            preparedStatement = connection.prepareStatement(insstr);
            int rowCount = preparedStatement.executeUpdate();
            result = rowCount;

        } catch (SQLException sqlerror) {
            System.out.println("sql fejl i writeline=" + sqlerror.getMessage());
        }


        System.out.println("\nSuccesfully saved customer entry to DataBase\n");
        return result;

    }

    public int insertCity(String cityName) throws NullPointerException {

        String insstr = "INSERT INTO cities(city_name) " +
                "VALUES ('" + cityName + "')";

        PreparedStatement preparedStatement;
        int result = 0;

        try {
            preparedStatement = connection.prepareStatement(insstr);
            int rowCount = preparedStatement.executeUpdate();
            result = rowCount;

        } catch (SQLException sqlerror) {
            System.out.println("sql fejl i writeline=" + sqlerror.getMessage());
        }


        System.out.println("\nSuccesfully saved city entry to DataBase\n");
        return result;

    }

    public boolean zipCodeExists(int zipcode_id){
        boolean exists = false;


        PreparedStatement preparedStatement;
        String searchStr = "SELECT zipcode_id from zipcodes where zipcode_id = ?";
        int result = -1;

        try {
            preparedStatement = connection.prepareStatement(searchStr);
            preparedStatement.setInt(1, zipcode_id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                result = resultSet.getInt(1);
            }

            exists = true;
        } catch (SQLException error) {
            error.printStackTrace();
        }

        return exists;
    }

    public boolean cityExists(String city_name){
        boolean exists = false;


        PreparedStatement preparedStatement;
        String searchStr = "SELECT city_id from cities where city_name = ?";
        int result = -1;

        try {
            preparedStatement = connection.prepareStatement(searchStr);
            preparedStatement.setString(1, city_name);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                result = resultSet.getInt(1);
            }

            exists = true;
        } catch (SQLException error) {
            error.printStackTrace();
        }

        return exists;
    }
    public boolean addressExists(int address_id){
        boolean exists = false;


        PreparedStatement preparedStatement;
        String searchStr = "SELECT address_id from addresses where address_id = ?";
        int result = -1;

        try {
            preparedStatement = connection.prepareStatement(searchStr);
            preparedStatement.setInt(1, address_id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                result = resultSet.getInt(1);
            }

            exists = true;
        } catch (SQLException error) {
            error.printStackTrace();
        }

        return exists;
    }

    public int insertZipCode(int zipcode_id,String city_name) throws NullPointerException {
        int result = 0;


        String insstr = "INSERT INTO zipcodes(zipcode_id,city_id) " +
                "VALUES ('" + zipcode_id +"','" + getCityIDFromDbByCityName(city_name) + "')";

        PreparedStatement preparedStatement;


        try {
            preparedStatement = connection.prepareStatement(insstr);
            int rowCount = preparedStatement.executeUpdate();
            result = rowCount;

        } catch (SQLException sqlerror) {
            System.out.println("sql fejl i writeline=" + sqlerror.getMessage());
        }


        System.out.println("\nSuccesfully saved zipcode entry to DataBase\n");


        return result;
    }

    public int insertAddress(String address_name,int zipcode_id) throws NullPointerException {

        String insstr = "INSERT INTO addresses(address_name,zipcode_id) " +
                "VALUES ('" + address_name +"','" + zipcode_id + "')";

        PreparedStatement preparedStatement;
        int result = 0;

        try {
            preparedStatement = connection.prepareStatement(insstr);
            int rowCount = preparedStatement.executeUpdate();
            result = rowCount;

        } catch (SQLException sqlerror) {
            System.out.println("sql fejl i writeline=" + sqlerror.getMessage());
        }


        System.out.println("\nSuccesfully saved address entry to DataBase\n");
        return result;

    }

    public int getAddressIDFromDbByAddressName(String address_name) {

        PreparedStatement preparedStatement;
        String searchStr = "SELECT address_id from addresses where address_name = ?";
        int result = -1;

        try {
            preparedStatement = connection.prepareStatement(searchStr);
            preparedStatement.setString(1, address_name);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                result = resultSet.getInt(1);
            }

        } catch (SQLException error) {
            error.printStackTrace();
        }

        return result;

    }

    public int getCityIDFromDbByCityName(String city_name) {

        PreparedStatement preparedStatement;
        String searchStr = "SELECT city_id from cities where city_name = ?";
        int result = -1;

        try {
            preparedStatement = connection.prepareStatement(searchStr);
            preparedStatement.setString(1, city_name);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                result = resultSet.getInt(1);
            }

        } catch (SQLException error) {
            error.printStackTrace();
        }

        return result;

    }
    public int getZipcodeFromDbByCityName(String city_name) {

        PreparedStatement preparedStatement;
        String searchStr = "SELECT zipcode_id from zipcodes where city_name = ?";
        int result = -1;

        try {
            preparedStatement = connection.prepareStatement(searchStr);
            preparedStatement.setString(1, city_name);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                result = resultSet.getInt(1);
            }

        } catch (SQLException error) {
            error.printStackTrace();
        }

        return result;

    }

    public String getCityNameFromDbByCustomerId(int customer_id) {

        PreparedStatement preparedStatement;
        String searchStr = "SELECT city_name from customerdata where customer_id = ?";
        String result ="";

        try {
            preparedStatement = connection.prepareStatement(searchStr);
            preparedStatement.setInt(1, customer_id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                result = resultSet.getString("city_name");
            }

        } catch (SQLException error) {
            error.printStackTrace();
        }

        return result;

    }

    public Customer getCustomerFromDBbyId(int id) {
        ArrayList<Customer> customer = new ArrayList<>();
        PreparedStatement preparedStatement;

        String searchStr = "SELECT * from customers where customer_id = ?";

        try {
            preparedStatement = connection.prepareStatement(searchStr);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                String customer_id = "" + resultSet.getObject("customer_id");
                Integer tempCustomer_id = Integer.parseInt(customer_id);
                String first_name = "" + resultSet.getObject("first_name");
                String last_name = "" + resultSet.getObject("last_name");
                String email = "" + resultSet.getObject("email");
                int address_id = Integer.parseInt("" + resultSet.getObject("address_id"));
                String city_name ;
                int city_id = getCityIDFromDbByCityName(city_name);
                int zipcode_id;
                String address_name;


                Car car = new Car(model_name, registration_number, first_registration, odometer2, car_group_id,
                        brand_id, fuelType_id);

                car.setCar_id(tempCar_id);

                cars.add(car);



            }

        } catch (SQLException error) {
            error.printStackTrace();
        }



        return cars.get(0);



    }

    public int deleteCustomer(int id) {
        String deleteString = "DELETE from customers where customer_id = ?";
        PreparedStatement preparedStatement;
        int result = -1;

        try {
            preparedStatement = connection.prepareStatement(deleteString);
            preparedStatement.setInt(1,id);
            result = preparedStatement.executeUpdate();

        } catch (SQLException error) {
            error.printStackTrace();
        }
        return result;
    }

    public int getCustomerIdFromDB(String first_name,String last_name) {

        PreparedStatement preparedStatement;
        String searchStr = "SELECT customer_id from customers where first_name = ? and last_name = ?";
        int result = -1;

        try {
            preparedStatement = connection.prepareStatement(searchStr);
            preparedStatement.setString(1, first_name);
            preparedStatement.setString(2, last_name);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                result = resultSet.getInt(1);
            }

        } catch (SQLException error) {
            error.printStackTrace();
        }

        return result;

    }


    public int updateCustomer(int customer_id, Customer customer) {

        String first_name = customer.getFirst_name();
        String last_name = customer.getLast_name();
        String email = customer.getEmail();

        int city_id = customer.getCity_id();
        String city_name = customer.getCity_name();

        if(!cityExists(city_name)) {
        insertCity(city_name);
        city_id = getCityIDFromDbByCityName(city_name);
        }

        int zipcode_id = customer.getZipcode_id();

        //Kontrollerer at zipcode ikke allerede eksisterer.
        if (!zipCodeExists(zipcode_id)) {
            insertZipCode(zipcode_id,city_name);
            zipcode_id = getZipcodeFromDbByCityName(city_name);
        }


        int address_id = customer.getAddress_id();
        String address_name = customer.getAddress_name();

        if(!addressExists(address_id)){
            insertAddress(address_name,zipcode_id);
            address_id = getAddressIDFromDbByAddressName(address_name);
        }


        PreparedStatement preparedStatement;
        String updateStr = "UPDATE customers SET first_name = ?, last_name = ?, email = ?," +
                " address_id = ? WHERE customer_id = ?";

        int result = -1;

        try {
            preparedStatement = connection.prepareStatement(updateStr);
            preparedStatement.setString(1, first_name);
            preparedStatement.setString(2, last_name);
            preparedStatement.setString(3,email);
            preparedStatement.setInt(4,address_id);
            preparedStatement.setInt(5,customer_id);
            result = preparedStatement.executeUpdate();


        } catch (SQLException error) {
            error.printStackTrace();
        }
        return result;


    }

    // ---------------------------------------------------
    // ---------------------------------------------------
    // ---------------------------------------------------
    // ---------------------------------------------------
    // ---------------------------------------------------
    // ---------------------------------------------------
    // ---------------------------------------------------

}
