import dao.BusDAO;
import dao.UserDAO;
import model.Bus;
import model.User;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        UserDAO userDAO = new UserDAO();
        BusDAO busDAO = new BusDAO();

        while (true) {
            System.out.println("\n--- Bus Ticket Booking System ---");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 1) {
                System.out.print("Enter Email: ");
                String email = sc.nextLine();
                System.out.print("Enter Password: ");
                String password = sc.nextLine();

                User user = userDAO.loginUser(email, password);
                if (user != null) {
                    System.out.println("Login Successful! Welcome, " + user.getName());
                    if (user.getRole().equals("admin")) {
                        adminMenu(sc, busDAO);
                    } else {
                        userMenu(sc, busDAO);
                    }
                } else {
                    System.out.println("Invalid email or password.");
                }
            } else if (choice == 2) {
                System.out.print("Enter Name: ");
                String name = sc.nextLine();
                System.out.print("Enter Email: ");
                String email = sc.nextLine();
                System.out.print("Enter Password: ");
                String password = sc.nextLine();

                User newUser = new User(name, email, password, "user");
                if (userDAO.registerUser(newUser)) {
                    System.out.println("Registration successful! You can now login.");
                } else {
                    System.out.println("Registration failed. Email might already exist.");
                }
            } else if (choice == 3) {
                System.out.println("Exiting...");
                break;
            } else {
                System.out.println("Invalid choice.");
            }
        }
        sc.close();
    }

    private static void adminMenu(Scanner sc, BusDAO busDAO) {
        while (true) {
            System.out.println("\n--- Admin Menu ---");
            System.out.println("1. Add Bus");
            System.out.println("2. View All Buses");
            System.out.println("3. Logout");
            System.out.print("Choose an option: ");
            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 1) {
                System.out.print("Enter Bus Number: ");
                String busNumber = sc.nextLine();
                System.out.print("Enter Capacity: ");
                int capacity = sc.nextInt();
                sc.nextLine();
                System.out.print("Enter Operator: ");
                String operator = sc.nextLine();

                Bus bus = new Bus(busNumber, capacity, operator);
                if (busDAO.addBus(bus)) {
                    System.out.println("Bus added successfully!");
                }
            } else if (choice == 2) {
                List<Bus> buses = busDAO.getAllBuses();
                if (buses.isEmpty()) {
                    System.out.println("No buses found.");
                } else {
                    System.out.println("\n--- Available Buses ---");
                    for (Bus b : buses) {
                        System.out.println("ID: " + b.getId() + " | Number: " + b.getBusNumber() + " | Operator: " + b.getOperator() + " | Capacity: " + b.getCapacity());
                    }
                }
            } else if (choice == 3) {
                break;
            }
        }
    }

    private static void userMenu(Scanner sc, BusDAO busDAO) {
        while (true) {
            System.out.println("\n--- User Menu ---");
            System.out.println("1. View Available Buses");
            System.out.println("2. Book Ticket (Coming Soon)");
            System.out.println("3. Logout");
            System.out.print("Choose an option: ");
            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 1) {
                List<Bus> buses = busDAO.getAllBuses();
                if (buses.isEmpty()) {
                    System.out.println("No buses available right now.");
                } else {
                    System.out.println("\n--- Available Buses ---");
                    for (Bus b : buses) {
                        System.out.println("ID: " + b.getId() + " | Number: " + b.getBusNumber() + " | Operator: " + b.getOperator() + " | Capacity: " + b.getCapacity());
                    }
                }
            } else if (choice == 2) {
                System.out.println("Booking feature will be added in the next step.");
            } else if (choice == 3) {
                break;
            }
        }
    }
}