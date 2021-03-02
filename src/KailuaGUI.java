import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public class KailuaGUI {
    private static JDBCWriter jdbcWriter = new JDBCWriter();
    private String welcomeString;
    private JFrame frame;
    private final JSplitPane splitPaneMidHorizontal;
    private final JScrollPane scrollPaneRight;
    private final JScrollPane scrollPaneLeft;
    private final JTextArea textAreaRight;
    private final JTextArea textAreaLeft;
    private final JPanel leftPanel;
    private final JPanel topLeftPanel;
    private final JPanel botLeftPanel;
    private final JPanel rightPanel;
    private final JPanel carPanel;
    private final JPanel customerPanel;
    private final JPanel rentalPanel;
    private final JPanel connectPanel;
    private final JPanel latestCarPanel;
    private final JLabel carLabel;
    private final JLabel customerLabel;
    private final JLabel rentalLabel;
    private final JLabel textAreaRightLabel;
    private final JLabel textAreaLeftLabel;
    JTextField model_name_field;
    JTextField registration_field;
    JTextField first_registration_field;
    JTextField odometer_field;
    JTextField car_group_id_field;
    JTextField brand_id_field;
    JTextField fuelType_id_field;




    public Car createCar() {
        Car car;


        car = new Car(model_name_field.getText(), registration_field.getText(), LocalDate.parse(first_registration_field.getText()),
                Double.parseDouble(odometer_field.getText()), Integer.parseInt(car_group_id_field.getText()), Integer.parseInt(brand_id_field.getText()),
                Integer.parseInt(fuelType_id_field.getText()));


        return car;

    }


    public void fillTextFieldsAttributes(CarUpdateGUI carUpdateGUI) {

        model_name_field = carUpdateGUI.getModelNameTextField();
        registration_field = carUpdateGUI.getRegistrationNumberTextField();
        first_registration_field = carUpdateGUI.getFirstRegistrationTextField();
        odometer_field = carUpdateGUI.getOdometerTextField();
        car_group_id_field = carUpdateGUI.getCarGrpIdTextField();
        brand_id_field = carUpdateGUI.getBrandIdTextField();
        fuelType_id_field = carUpdateGUI.getFuelTypeIdTextField();

    }

    public void fillTextFields(CarUpdateGUI carUpdateGUI, Car car) {


        model_name_field = carUpdateGUI.getModelNameTextField();
        registration_field = carUpdateGUI.getRegistrationNumberTextField();
        first_registration_field = carUpdateGUI.getFirstRegistrationTextField();
        odometer_field = carUpdateGUI.getOdometerTextField();
        car_group_id_field = carUpdateGUI.getCarGrpIdTextField();
        brand_id_field = carUpdateGUI.getBrandIdTextField();
        fuelType_id_field = carUpdateGUI.getFuelTypeIdTextField();







        model_name_field.setText(car.getModel_name());
        registration_field.setText(car.getRegistration_number());
        first_registration_field.setText(String.valueOf(car.getFirst_registration()));
        odometer_field.setText(String.valueOf(car.getOdometer()));
        car_group_id_field.setText(String.valueOf(car.getCar_group_id()));
        brand_id_field.setText(String.valueOf(car.getBrand_id()));
        fuelType_id_field.setText(String.valueOf(car.getFuelType_id()));




    }


    public void updateCarPress() {
        final String[] model_nameSEARCH = new String[1];
        jdbcWriter.setConnection();


        CarUpdateGUI carUpdateGUI = new CarUpdateGUI();
        UserIDPrompt userIDPrompt = new UserIDPrompt();
        JButton applyBtnID = userIDPrompt.getApplyBtn();
        JButton updateBtn = carUpdateGUI.getApplyBtn();

        userIDPrompt.run();




        applyBtnID.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Car car = userIDPrompt.applyPress();
                model_nameSEARCH[0] = car.getModel_name();
                carUpdateGUI.run();
                fillTextFields(carUpdateGUI, car);

                updateBtn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String model_nameSearchString = model_nameSEARCH[0];

                        Car car = createCar();
                        int car_id = jdbcWriter.getCarIdFromDB(model_nameSearchString);

                        System.out.println(car_id);

                        jdbcWriter.updateCar(car_id, car);







                    }
                });


            }
        });



    }



    public void insertCarPress() {
        String eventString = "Inserting car into DataBase...";

        textAreaLeft.setText(textAreaLeft.getText() + eventString + "\n");


        CarCreateGUI carCreateGUI = new CarCreateGUI();
        JButton tempApplyBtn = carCreateGUI.getApplyBtn();
        JButton tempReturnBtn = carCreateGUI.getReturnBtn();
        JFrame tempFrame = frame;
        frame = carCreateGUI.getFrame();
        frame.setVisible(true);


        tempApplyBtn.addActionListener(a -> textAreaLeft.setText(textAreaLeft.getText() +
                "Successfully Added Car to Database\n"));
        tempReturnBtn.addActionListener(a -> textAreaLeft.setText(textAreaLeft.getText() +
                "Returned to main - no Cars added to Database\n"));

        frame = tempFrame;


    }

    public void latestCarAdded() {
        JDBCWriter jdbcWriter = new JDBCWriter();
        jdbcWriter.setConnection();
        textAreaLeft.setText(textAreaLeft.getText() + jdbcWriter.getLatestCar(jdbcWriter.getLatestCarIndex()));
    }


    public KailuaGUI() {
        welcomeString = "Welcome to Kailua DataBase Management Tool";

        // frame
        frame = new JFrame("Kailua DataBase Management Tool");

        // frame splits
        splitPaneMidHorizontal = new JSplitPane();


        // panels

        leftPanel = new JPanel();

        topLeftPanel = new JPanel();
        rightPanel = new JPanel();
        botLeftPanel = new JPanel();

        carPanel = new JPanel();
        customerPanel = new JPanel();
        rentalPanel = new JPanel();
        latestCarPanel = new JPanel();
        connectPanel = new JPanel();





        // Labels

        carLabel = new JLabel("Cars");
        customerLabel = new JLabel("Customers");
        rentalLabel = new JLabel("Rentals");
        textAreaRightLabel = new JLabel("Console");
        textAreaLeftLabel = new JLabel("Log");

        // Log Buttons

        JButton latestCarBtn = new JButton("SHOW LATEST CAR INSERTION");

        // Connect Buttons
        JButton connectBtn = new JButton("CONNECT TO DATABASE");

        // Car Buttons
        JButton insertCarBtn = new JButton("INSERT ");
        JButton deleteCarBtn = new JButton("DELETE");
        JButton updateCarBtn = new JButton("UPDATE");

        // action listeners
        insertCarBtn.addActionListener(a -> insertCarPress());
        latestCarBtn.addActionListener(a -> latestCarAdded());
        updateCarBtn.addActionListener(a -> updateCarPress());





        // Customer Buttons
        JButton insertCustomerBtn = new JButton("INSERT ");
        JButton deleteCustomerBtn = new JButton("DELETE");
        JButton updateCustomerBtn = new JButton("UPDATE");

        // Rental Buttons
        JButton insertRentalBtn = new JButton("INSERT ");
        JButton deleteRentalBtn = new JButton("DELETE");
        JButton updateRentalBtn = new JButton("UPDATE");

        // Add components to Panels
        carPanel.setLayout(new BoxLayout(carPanel, BoxLayout.Y_AXIS));
        carPanel.add(carLabel);
        carPanel.add(insertCarBtn);
        carPanel.add(deleteCarBtn);
        carPanel.add(updateCarBtn);

        customerPanel.setLayout(new BoxLayout(customerPanel, BoxLayout.Y_AXIS));
        customerPanel.add(customerLabel);
        customerPanel.add(insertCustomerBtn);
        customerPanel.add(deleteCustomerBtn);
        customerPanel.add(updateCustomerBtn);

        rentalPanel.setLayout(new BoxLayout(rentalPanel, BoxLayout.Y_AXIS));
        rentalPanel.add(rentalLabel);
        rentalPanel.add(insertRentalBtn);
        rentalPanel.add(deleteRentalBtn);
        rentalPanel.add(updateRentalBtn);

        latestCarPanel.setLayout(new BoxLayout(latestCarPanel, BoxLayout.Y_AXIS));
        latestCarPanel.add(latestCarBtn);

        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        scrollPaneRight = new JScrollPane();
        textAreaRight = new JTextArea(welcomeString,2, 2);
        textAreaRight.setEditable(false);
        rightPanel.add(textAreaRightLabel);
        rightPanel.add(scrollPaneRight);
        scrollPaneRight.setViewportView(textAreaRight);



        topLeftPanel.add(carPanel);
        topLeftPanel.add(customerPanel);
        topLeftPanel.add(rentalPanel);
        topLeftPanel.add(latestCarPanel);

        botLeftPanel.setLayout(new BoxLayout(botLeftPanel, BoxLayout.LINE_AXIS));
        scrollPaneLeft = new JScrollPane();
        textAreaLeft = new JTextArea("", 1, 1);
        textAreaLeft.setEditable(false);
        botLeftPanel.add(textAreaLeftLabel);
        botLeftPanel.add(scrollPaneLeft);
        scrollPaneLeft.setViewportView(textAreaLeft);


        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.add(topLeftPanel);
        leftPanel.add(botLeftPanel);







        // frame LAYOUT

        frame.setPreferredSize(new Dimension(800, 600));
        frame.setResizable(false);
        frame.getContentPane().setLayout(new GridLayout());
        frame.getContentPane().add(splitPaneMidHorizontal);


        // SplitPane edit

        splitPaneMidHorizontal.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        splitPaneMidHorizontal.setDividerLocation(300);
        splitPaneMidHorizontal.setDividerSize(10);
        splitPaneMidHorizontal.setEnabled(false);
        splitPaneMidHorizontal.setLeftComponent(leftPanel);
        splitPaneMidHorizontal.setRightComponent(rightPanel);



        frame.setBounds(100, 100, 800, 600);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(false);

    }

    public void run() {
        frame.setVisible(true);

    }



    public static void main(String[] args) {

        new KailuaGUI().run();
    }


































}
