import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SalaryCalculationApp extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton viewSalaryButton;
    private JPanel salaryDetailsPanel;
    private JButton backButton;

    private List<Employee> employees; // Store employees with their data

    // Rates for each employee
    private double[] basicSalaries = {
        90000, 60000, 60000, 60000, 52670, 52670, 42975, 22500, 22500, 52670,
        50825, 38475, 24000, 24000, 53500, 42975, 41850, 22500, 22500, 23250,
        23250, 24000, 22500, 22500, 24000, 24750, 24750, 24000, 22500, 22500,
        22500, 52670, 52670, 52670
    };

    private double[] monthlyRates = {
        45000, 30000, 30000, 30000, 26335, 26335, 21488, 11250, 11250, 26335,
        25413, 19238, 12000, 12000, 26750, 21488, 20925, 11250, 11250, 11625,
        11625, 12000, 11250, 11250, 12000, 12375, 12375, 12000, 11250, 11250,
        11250, 26335, 26335, 26335
    };

    private double[] hourlyRates = {
        535.71, 357.14, 357.14, 357.14, 313.51, 313.51, 255.80, 133.93, 133.93,
        313.51, 302.53, 229.02, 142.86, 142.86, 318.45, 255.80, 249.11, 133.93,
        133.93, 138.39, 138.39, 142.86, 133.93, 133.93, 142.86, 147.32, 147.32,
        142.86, 133.93, 133.93, 133.93, 313.51, 313.51, 313.51
    };

    public SalaryCalculationApp() {
        setTitle("Salary Calculation App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        // Initialize components
        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);
        viewSalaryButton = new JButton("View Salary");
        salaryDetailsPanel = new JPanel();
        salaryDetailsPanel.setLayout(new BorderLayout());
        backButton = new JButton("Back");

        // Load employee data from CSV (assuming EmployeeRecord.csv contains necessary details)
        loadEmployeeData();

        // Populate the table with employee data
        populateTable();

        // Add action listener to the view salary button
        viewSalaryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    // Get the selected employee
                    Employee selectedEmployee = employees.get(selectedRow);

                    // Create a panel to display salary details
                    JPanel detailsPanel = createSalaryDetailsPanel(selectedEmployee);

                    // Display details panel
                    displayDetailsPanel(detailsPanel);
                } else {
                    JOptionPane.showMessageDialog(SalaryCalculationApp.this,
                            "Please select an employee to view salary details.",
                            "No Employee Selected",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        // Add components to the frame
        JScrollPane scrollPane = new JScrollPane(table);
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(viewSalaryButton, BorderLayout.SOUTH);
        add(mainPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    // Method to load employee data (assuming details are in a CSV file)
    private void loadEmployeeData() {
        employees = new ArrayList<>();
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader("EmployeeRecord.csv"))) {
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                // Assuming CSV format: Employee Number, Last Name, First Name, SSS, PhilHealth, TIN, Pag-ibig
                String employeeNumber = data[0];
                String lastName = data[1];
                String firstName = data[2];
                String sss = data[3];
                String philHealth = data[4];
                String tin = data[5];
                String pagIbig = data[6];

                // Create Employee object and add to list
                Employee employee = new Employee(employeeNumber, lastName, firstName, sss, philHealth, tin, pagIbig);
                employees.add(employee);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to populate the table with employee data
    private void populateTable() {
        // Define table columns
        String[] columns = {"Employee Number", "Last Name", "First Name", "SSS", "PhilHealth", "TIN", "Pag-ibig"};
        tableModel.setColumnIdentifiers(columns);

        // Populate table with data
        for (Employee employee : employees) {
            tableModel.addRow(new String[]{
                employee.getEmployeeNumber(), employee.getLastName(), employee.getFirstName(),
                employee.getSss(), employee.getPhilHealth(), employee.getTin(), employee.getPagIbig()
            });
        }
    }

    // Method to create a panel displaying salary details for the selected employee
    private JPanel createSalaryDetailsPanel(Employee employee) {
        JPanel detailsPanel = new JPanel(new BorderLayout());
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Display employee details
        JLabel nameLabel = new JLabel("Employee Name: " + employee.getFirstName() + " " + employee.getLastName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        detailsPanel.add(nameLabel, BorderLayout.NORTH);

        // Display month selection and salary computation
        JPanel computationPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        computationPanel.setBorder(BorderFactory.createTitledBorder("Salary Details"));

        // Month selection combo box
        String[] months = {"January", "February", "March", "April", "May", "June",
                           "July", "August", "September", "October", "November", "December"};
        JComboBox<String> monthComboBox = new JComboBox<>(months);
        monthComboBox.setSelectedIndex(0); // Default selection
        computationPanel.add(new JLabel("Select Month:"));
        computationPanel.add(monthComboBox);

        // Compute button
        JButton computeButton = new JButton("Compute");
        computationPanel.add(computeButton);

        // Display area for computed details
        JTextArea detailsTextArea = new JTextArea(10, 30);
        detailsTextArea.setEditable(false);
        JScrollPane detailsScrollPane = new JScrollPane(detailsTextArea);
        detailsPanel.add(detailsScrollPane, BorderLayout.CENTER);

        // Back button to return to main panel
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                detailsPanel.setVisible(false);
                getContentPane().remove(detailsPanel);
            }
        });
        computationPanel.add(backButton);

        // Add computation action listener
        computeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedMonthIndex = monthComboBox.getSelectedIndex();

                // Get rates for the selected month
                double basicSalary = basicSalaries[selectedMonthIndex];
                double monthlyRate = monthlyRates[selectedMonthIndex];
                double hourlyRate = hourlyRates[selectedMonthIndex];

                // Calculate deductions (example calculations, replace with actual formulas)
                double sssDeduction = calculateSSSDeduction(basicSalary);
                double philHealthDeduction = calculatePhilHealthDeduction(basicSalary);
                double tinDeduction = calculateTINDeduction(basicSalary);
                double pagIbigDeduction = calculatePagIbigDeduction(basicSalary);

                // Compute net deduction
                double totalDeduction = sssDeduction + philHealthDeduction + tinDeduction + pagIbigDeduction;

                // Display computed details
                detailsTextArea.setText("Employee Number: " + employee.getEmployeeNumber() + "\n" +
                                        "SSS: " + employee.getSss() + "\n" +
                                        "PhilHealth: " + employee.getPhilHealth() + "\n" +
                                        "TIN: " + employee.getTin() + "\n" +
                                        "Pag-ibig: " + employee.getPagIbig() + "\n\n" +
                                        "Month: " + months[selectedMonthIndex] + "\n\n" +
                                        "Basic Salary: " + String.format("%.2f", basicSalary) + "\n" +
                                        "Monthly Rate: " + String.format("%.2f", monthlyRate) + "\n" +
                                        "Hourly Rate: " + String.format("%.2f", hourlyRate) + "\n\n" +
                                        "SSS Deduction: " + String.format("%.2f", sssDeduction) + "\n" +
                                        "PhilHealth Deduction: " + String.format("%.2f", philHealthDeduction) + "\n" +
                                        "TIN Deduction: " + String.format("%.2f", tinDeduction) + "\n" +
                                        "Pag-ibig Deduction: " + String.format("%.2f", pagIbigDeduction) + "\n\n" +
                                        "Total Deduction: " + String.format("%.2f", totalDeduction) + "\n" +
                                        "Net Salary: " + String.format("%.2f", (basicSalary - totalDeduction)));
            }
        });

        detailsPanel.add(computationPanel, BorderLayout.SOUTH);

        return detailsPanel;
    }

    // Method to display details panel
    private void displayDetailsPanel(JPanel panel) {
        getContentPane().add(panel, BorderLayout.EAST);
        panel.setVisible(true);
        revalidate();
        repaint();
    }

    // Example deduction calculations (replace with actual formulas)
    private double calculateSSSDeduction(double salary) {
        // Example: Assume 1% SSS deduction
        return salary * 0.01;
    }

    private double calculatePhilHealthDeduction(double salary) {
        // Example: Assume 2% PhilHealth deduction
        return salary * 0.02;
    }

    private double calculateTINDeduction(double salary) {
        // Example: Assume 2% TIN deduction
        return salary * 0.02;
    }

    private double calculatePagIbigDeduction(double salary) {
        // Example: Assume 3% Pag-ibig deduction
        return salary * 0.03;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SalaryCalculationApp();
            }
        });
    }
}

class Employee {
    private String employeeNumber;
    private String lastName;
    private String firstName;
    private String sss;
    private String philHealth;
    private String tin;
    private String pagIbig;

    public Employee(String employeeNumber, String lastName, String firstName, String sss,
                    String philHealth, String tin, String pagIbig) {
        this.employeeNumber = employeeNumber;
        this.lastName = lastName;
        this.firstName = firstName;
        this.sss = sss;
        this.philHealth = philHealth;
        this.tin = tin;
        this.pagIbig = pagIbig;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSss() {
        return sss;
    }

    public String getPhilHealth() {
        return philHealth;
    }

    public String getTin() {
        return tin;
    }

    public String getPagIbig() {
        return pagIbig;
    }
}
