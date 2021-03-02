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
            connection = DriverManager.getConnection(url, "Marc", "wilma");
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


    public int delete(int id) {

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


    public Vector<String> getLines(String url, String line) {
        String searchString = "select left(line, 20) as line from urlreads where url like ? and line like ? LIMIT 20";
        PreparedStatement preparedStatement;
        Vector<String> v1 = new Vector<>();

        try {
            preparedStatement = connection.prepareStatement(searchString);
            preparedStatement.setString(1, "%" + url + "%");
            preparedStatement.setString(2, "%" + line + "%");
            ResultSet resSet = preparedStatement.executeQuery();
            String str1;

            while (resSet.next()) {
                str1 = "" + resSet.getObject("line");
                v1.add(str1);

            }

        } catch (SQLException error) {
            error.printStackTrace();
        }

        return v1;

    }
}
