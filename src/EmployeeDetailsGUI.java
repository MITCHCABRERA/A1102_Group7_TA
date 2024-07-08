import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class EmployeeDetailsGUI extends JFrame {

    static double sssContribution;
    static double philhealthContribution;
    static double pagIbigContribution;
    static double employeeTax;

    private JTextPane textPane;
    private JTextField employeeNumberField;

    public EmployeeDetailsGUI() {
        setTitle("Employee Details");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create UI elements
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());

        JLabel employeeNumberLabel = new JLabel("Enter employee number:");
        employeeNumberField = new JTextField(10);
        JButton displayButton = new JButton("Display Employee Details");
        JButton calculateButton = new JButton("Calculate Salary");

        topPanel.add(employeeNumberLabel);
        topPanel.add(employeeNumberField);
        topPanel.add(displayButton);
        topPanel.add(calculateButton);

        textPane = new JTextPane();
        textPane.setContentType("text/html");
        textPane.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textPane);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        add(panel);

        // Add action listeners
        displayButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayEmployeeDetails();
            }
        });

        calculateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                calculateSalary();
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new EmployeeDetailsGUI().setVisible(true);
            }
        });
    }

    public void calculateSalary() {
        String csvFilePath = "salarycalc.csv";
        int targetLine = Integer.parseInt(employeeNumberField.getText());
        int lineNumber = -1;
        textPane.setText("");

        try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (lineNumber == targetLine) {
                    String[] parts = line.split(",");
                    StringBuilder html = new StringBuilder();
                    html.append("<html>");
                    html.append("<h2>Employee Details</h2>");
                    html.append("<table border='1'>");
                    html.append("<tr><td>Employee ID:</td><td>").append(parts[0]).append("</td></tr>");
                    html.append("<tr><td>First Name:</td><td>").append(parts[2]).append("</td></tr>");
                    html.append("<tr><td>Last Name:</td><td>").append(parts[1]).append("</td></tr>");
                    html.append("<tr><td>Basic Salary:</td><td>").append(parts[10]).append("</td></tr>");
                    html.append("<tr><td>Gross Semi-monthly Rate:</td><td>").append(parts[11]).append("</td></tr>");
                    html.append("<tr><td>Hourly Rate:</td><td>").append(parts[12]).append("</td></tr>");
                    html.append("<tr><td>SSS#:</td><td>").append(parts[3]).append("</td></tr>");
                    html.append("<tr><td>Philhealth#:</td><td>").append(parts[4]).append("</td></tr>");
                    html.append("<tr><td>TIN#:</td><td>").append(parts[5]).append("</td></tr>");
                    html.append("<tr><td>PagIbig#:</td><td>").append(parts[6]).append("</td></tr>");
                    html.append("<tr><td>Rice Subsidy:</td><td>").append(parts[7]).append("</td></tr>");
                    html.append("<tr><td>Phone Allowance:</td><td>").append(parts[8]).append("</td></tr>");
                    html.append("<tr><td>Clothing Allowance:</td><td>").append(parts[9]).append("</td></tr>");
                    html.append("</table>");

                    double basicSalary = Double.parseDouble(parts[10].replace(" ", ""));
                    double grossRate = Double.parseDouble(parts[11].replace(" ", ""));
                    double hourlyRate = Double.parseDouble(parts[12].replace(" ", ""));

                    double riceSubsidy = Double.parseDouble(parts[7]);
                    double phoneAllowance = Double.parseDouble(parts[8]);
                    double clothingAllowance = Double.parseDouble(parts[9]);

                    double totalAllowances = riceSubsidy + phoneAllowance + clothingAllowance;

                    calculateSSSContribution(basicSalary);
                    calculatePhilHealthContribution(basicSalary);
                    calculatePagIbigContribution(basicSalary);
                    calculateEmployeeTax(grossRate);

                    double grossDeduction = sssContribution + philhealthContribution + pagIbigContribution + employeeTax;

                    html.append("<h2>Salary Calculation</h2>");
                    html.append("<table border='1'>");
                    html.append("<tr><td>Total Allowances:</td><td>").append(totalAllowances).append("</td></tr>");
                    html.append("<tr><td>SSS Contribution:</td><td>").append(sssContribution).append("</td></tr>");
                    html.append("<tr><td>PhilHealth Contribution:</td><td>").append(philhealthContribution).append("</td></tr>");
                    html.append("<tr><td>Pag-IBIG Contribution:</td><td>").append(pagIbigContribution).append("</td></tr>");
                    html.append("<tr><td>Employee Tax:</td><td>").append(employeeTax).append("</td></tr>");
                    html.append("<tr><td>Gross Deduction:</td><td>").append(grossDeduction).append("</td></tr>");
                    html.append("<tr><td>Gross Income:</td><td>").append(String.format("%,.2f", grossRate)).append("</td></tr>");
                    html.append("<tr><td>Benefits:</td><td>").append(String.format("%,.2f", totalAllowances)).append("</td></tr>");
                    html.append("<tr><td>Total Deductions:</td><td>").append(String.format("%,.2f", grossDeduction)).append("</td></tr>");
                    html.append("<tr><td>TAKE HOME PAY:</td><td>").append(String.format("%,.2f", (grossRate + totalAllowances - grossDeduction))).append("</td></tr>");
                    html.append("</table>");
                    html.append("</html>");

                    textPane.setText(html.toString());
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void displayEmployeeDetails() {
        String csvFilePath = "Employeedetails.csv";
        int targetLine = Integer.parseInt(employeeNumberField.getText());
        int lineNumber = -1;
        textPane.setText("");

        try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (lineNumber == targetLine) {
                    String[] parts = line.split(",");
                    StringBuilder html = new StringBuilder();
                    html.append("<html>");
                    html.append("<h2>Employee Details</h2>");
                    html.append("<table border='1'>");
                    html.append("<tr><td>Employee ID:</td><td>").append(parts[0]).append("</td></tr>");
                    html.append("<tr><td>First Name:</td><td>").append(parts[2]).append("</td></tr>");
                    html.append("<tr><td>Last Name:</td><td>").append(parts[1]).append("</td></tr>");
                    html.append("<tr><td>Birthday:</td><td>").append(parts[3]).append("</td></tr>");
                    html.append("<tr><td>Address:</td><td>").append(parts[4]).append("</td></tr>");
                    html.append("<tr><td>Phone Number:</td><td>").append(parts[5]).append("</td></tr>");
                    html.append("<tr><td>Status:</td><td>").append(parts[10]).append("</td></tr>");
                    html.append("<tr><td>Position:</td><td>").append(parts[11]).append("</td></tr>");
                    html.append("<tr><td>Immediate Supervisor:</td><td>").append(parts[12]).append("</td></tr>");
                    html.append("</table>");
                    html.append("</html>");

                    textPane.setText(html.toString());
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void calculateSSSContribution(double basicSalary) {
      if (basicSalary >= 0 && basicSalary <= 3250) {
            sssContribution = 135.00;
        } else if (basicSalary > 3250 && basicSalary <= 3750) {
            sssContribution = 157.50;
        } else if (basicSalary > 3750 && basicSalary <= 4250) {
            sssContribution = 180.00;
        } else if (basicSalary > 4250 && basicSalary <= 4750) {
            sssContribution = 202.50;
        } else if (basicSalary > 4750 && basicSalary <= 5250) {
            sssContribution = 225.00;
        } else if (basicSalary > 5250 && basicSalary <= 5750) {
            sssContribution = 247.50;
        } else if (basicSalary > 5750 && basicSalary <= 6250) {
            sssContribution = 270.00;
        } else if (basicSalary > 6250 && basicSalary <= 6750) {
            sssContribution = 292.50;
        } else if (basicSalary > 6750 && basicSalary <= 7250) {
            sssContribution = 315.00;
        } else if (basicSalary > 7250 && basicSalary <= 7750) {
            sssContribution = 337.50;
        } else if (basicSalary > 7750 && basicSalary <= 8250) {
            sssContribution = 360.00;
        } else if (basicSalary > 8250 && basicSalary <= 8750) {
            sssContribution = 382.50;
        } else if (basicSalary > 8750 && basicSalary <= 9250) {
            sssContribution = 405.00;
        } else if (basicSalary > 9250 && basicSalary <= 9750) {
            sssContribution = 427.50;
        } else if (basicSalary > 9750 && basicSalary <= 10250) {
            sssContribution = 450.00;
        } else if (basicSalary > 10250 && basicSalary <= 10750) {
            sssContribution = 472.50;
        } else if (basicSalary > 10750 && basicSalary <= 11250) {
            sssContribution = 495.00;
        } else if (basicSalary > 11250 && basicSalary <= 11750) {
            sssContribution = 517.50;
        } else if (basicSalary > 11750 && basicSalary <= 12250) {
            sssContribution = 540.00;
        } else if (basicSalary > 12250 && basicSalary <= 12750) {
            sssContribution = 562.50;
        } else if (basicSalary > 12750 && basicSalary <= 13250) {
            sssContribution = 585.00;
        } else if (basicSalary > 13250 && basicSalary <= 13750) {
            sssContribution = 607.50;
        } else if (basicSalary > 13750 && basicSalary <= 14250) {
            sssContribution = 630.00;
        } else if (basicSalary > 14250 && basicSalary <= 14750) {
            sssContribution = 652.50;
        } else if (basicSalary > 14750 && basicSalary <= 15250) {
            sssContribution = 675.00;
        } else if (basicSalary > 15250 && basicSalary <= 15750) {
            sssContribution = 697.50;
        } else if (basicSalary > 15750 && basicSalary <= 16250) {
            sssContribution = 720.00;
        } else if (basicSalary > 16250 && basicSalary <= 16750) {
            sssContribution = 742.50;
        } else if (basicSalary > 16750 && basicSalary <= 17250) {
            sssContribution = 765.00;
        } else if (basicSalary > 17250 && basicSalary <= 17750) {
            sssContribution = 787.50;
        } else if (basicSalary > 17750 && basicSalary <= 18250) {
            sssContribution = 810.00;
        } else if (basicSalary > 18250 && basicSalary <= 18750) {
            sssContribution = 832.50;
        } else if (basicSalary > 18750 && basicSalary <= 19250) {
            sssContribution = 855.00;
        } else if (basicSalary > 19250 && basicSalary <= 19750) {
            sssContribution = 877.50;
        } else if (basicSalary > 19750 && basicSalary <= 20250) {
            sssContribution = 900.00;
        } else if (basicSalary > 20250 && basicSalary <= 20750) {
            sssContribution = 922.50;
        } else if (basicSalary > 20750 && basicSalary <= 21250) {
            sssContribution = 945.00;
        } else if (basicSalary > 21250 && basicSalary <= 21750) {
            sssContribution = 967.50;
        } else if (basicSalary > 21750 && basicSalary <= 22250) {
            sssContribution = 990.00;
        } else if (basicSalary > 22250 && basicSalary <= 22750) {
            sssContribution = 1012.50;
        } else if (basicSalary > 22750 && basicSalary <= 23250) {
            sssContribution = 1035.00;
        } else if (basicSalary > 23250 && basicSalary <= 23750) {
            sssContribution = 1057.50;
        } else if (basicSalary > 23750 && basicSalary <= 24250) {
            sssContribution = 1080.00;
        } else if (basicSalary > 24250 && basicSalary <= 24750) {
            sssContribution = 1102.50;
        } else {
            sssContribution = 1125.00;
        }
    }

    static void calculatePhilHealthContribution(double basicSalary) {
      if (basicSalary > 10000 && basicSalary <= 59999.99) {
            philhealthContribution = 300.0 + ((basicSalary - 10000.01) * 0.03);
            if (philhealthContribution > 1800.0) {
                philhealthContribution = 1800.0;
            }
        } else if (basicSalary > 59999.99) {
            philhealthContribution = 1800.0;
        } else {
            philhealthContribution = 300.0;
        }
    }

    static void calculatePagIbigContribution(double basicSalary) {
        if (basicSalary >= 1000 && basicSalary <= 1500) {
            pagIbigContribution = 0.03 * basicSalary;
        } else if (basicSalary > 1500) {
            pagIbigContribution = 0.04 * basicSalary;
        } else {
            pagIbigContribution = 0.0;
        }
    }
   
        static void calculateEmployeeTax(double grossRate) {
       double totalDeductions = sssContribution + philhealthContribution + pagIbigContribution;

        double taxableIncome = grossRate - totalDeductions;

        if (taxableIncome <= 20832) {
            employeeTax = 0.0;
        } else if (taxableIncome <= 33333) {
            employeeTax = (taxableIncome - 20833) * 0.20;
        } else if (taxableIncome <= 66667) {
            employeeTax = 2500 + (taxableIncome - 33333) * 0.25;
        } else if (taxableIncome <= 166667) {
            employeeTax = 10833 + (taxableIncome - 66667) * 0.30;
        } else if (taxableIncome <= 666667) {
            employeeTax = 40833.33 + (taxableIncome - 166667) * 0.32;
        } else {
            employeeTax = 200833.33 + (taxableIncome - 666667) * 0.35;
        }
   }
}