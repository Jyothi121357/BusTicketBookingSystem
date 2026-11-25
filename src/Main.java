import dao.BusDAO;
import dao.UserDAO;
import dao.BookingDAO;
import model.Bus;
import model.User;
import java.util.List;
import java.util.Scanner;

public class Main {
    // This tracks who is currently logged in (Session Tracker)
    static User currentUser = null;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        UserDAO userDAO = new UserDAO();
        BusDAO busDAO = new BusDAO();
        BookingDAO bookingDAO = new BookingDAO();

        while (true) {
            System.out.println("\n--- Bus Ticket Booking System ---");
            System.out.println("1. Login as Admin");
            System.out.println("2. Login as User");
            System.out.println("3. Register");
            System.out.println("4. Exit");

            int choice = getIntInput(sc, "Choose an option: ");

            if (choice == 1 || choice == 2) {
                // 1. Prevent simultaneous logins
                if (currentUser != null) {
                    System.out.println(
                            "Error: You are already logged in as " + currentUser.getName() + ". Please logout first.");
                    continue;
                }

                // 2. Ask for credentials
                System.out.print("Enter Email: ");
                String email = sc.nextLine();
                System.out.print("Enter Password: ");
                String password = sc.nextLine();

                // 3. Check database
                User user = userDAO.loginUser(email, password);

                if (user != null) {
                    String dbRole = user.getRole();

                    // --- LOGIC FOR ADMIN LOGIN (Choice 1) ---
                    if (choice == 1) {
                        if (dbRole.equalsIgnoreCase("user")) {
                            // Permanently upgrade them to ADMIN in the database
                            userDAO.updateUserRole(email, "ADMIN");
                            user.setRole("ADMIN");
                            System.out.println("Notice: Your account has been permanently upgraded to ADMIN.");
                        }

                        currentUser = user;
                        System.out.println("Login Successful! Welcome Admin, " + currentUser.getName());
                        adminMenu(sc, busDAO, bookingDAO);
                        currentUser = null; // Clear session after logout
                    }
                    // --- LOGIC FOR USER LOGIN (Choice 2) ---
                    else if (choice == 2) {
                        if (dbRole.equalsIgnoreCase("ADMIN")) {
                            // Block them from entering the User menu
                            System.out
                                    .println("Error: This email is permanently an ADMIN. You cannot log in as a User.");
                            continue; // Skips the rest of the loop and goes back to main menu
                        }

                        currentUser = user;
                        System.out.println("Login Successful! Welcome User, " + currentUser.getName());
                        userMenu(sc, busDAO, bookingDAO, user);
                        currentUser = null; // Clear session after logout
                    }
                } else {
                    System.out.println("Invalid email or password.");
                }
            } else if (choice == 3) {
                // Register Flow
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
                    System.out.println("Registration failed.");
                }
            } else if (choice == 4) {
                System.out.println("Exiting...");
                break;
            } else {
                System.out.println("Invalid choice.");
            }
        }
        sc.close();
    }

    private static void adminMenu(Scanner sc, BusDAO busDAO, BookingDAO bookingDAO) {
        while (true) {
            System.out.println("\n--- Admin Menu ---");
            System.out.println("1. Add Bus");
            System.out.println("2. View All Buses");
            System.out.println("3. View All Bookings");
            System.out.println("4. Delete a Bus");
            System.out.println("5. Logout");

            int choice = getIntInput(sc, "Choose an option: ");

            if (choice == 1) {
                System.out.print("Enter Bus Number: ");
                String busNumber = sc.nextLine();
                int capacity = getIntInput(sc, "Enter Capacity: ");
                System.out.print("Enter Operator: ");
                String operator = sc.nextLine();

                Bus newBus = new Bus(busNumber, capacity, operator);
                if (busDAO.addBus(newBus)) {
                    System.out.println("Bus added successfully!");
                } else {
                    System.out.println("Failed to add bus.");
                }
            } else if (choice == 2) {
                List<Bus> buses = busDAO.getAllBuses();
                if (buses.isEmpty()) {
                    System.out.println("No buses found.");
                } else {
                    System.out.println("\n--- All Buses ---");
                    for (Bus b : buses) {
                        System.out.println("ID: " + b.getId() + " | Number: " + b.getBusNumber() +
                                " | Operator: " + b.getOperator() + " | Capacity: " + b.getCapacity());
                    }
                }
            } else if (choice == 3) {
                bookingDAO.getAllBookings();
            } else if (choice == 4) {
                int busId = getIntInput(sc, "Enter Bus ID to delete: ");
                if (busDAO.deleteBus(busId)) {
                    System.out.println("Bus deleted successfully!");
                } else {
                    System.out.println("Failed to delete bus. Check the ID.");
                }
            } else if (choice == 5) {
                System.out.println("Logging out...");
                break;
            } else {
                System.out.println("Invalid choice.");
            }
        }
    }

    private static void userMenu(Scanner sc, BusDAO busDAO, BookingDAO bookingDAO, User user) {
        while (true) {
            System.out.println("\n--- User Menu ---");
            System.out.println("1. View Available Buses");
            System.out.println("2. Book Ticket");
            System.out.println("3. View My Bookings");
            System.out.println("4. Cancel a Booking");
            System.out.println("5. Logout");

            int choice = getIntInput(sc, "Choose an option: ");

            if (choice == 1) {
                List<Bus> buses = busDAO.getAllBuses();
                if (buses.isEmpty()) {
                    System.out.println("No buses available right now.");
                } else {
                    System.out.println("\n--- Available Buses ---");
                    for (Bus b : buses) {
                        System.out.println("ID: " + b.getId() + " | Number: " + b.getBusNumber() +
                                " | Operator: " + b.getOperator() + " | Capacity: " + b.getCapacity());
                    }
                }
            } else if (choice == 2) {
                int busId = getIntInput(sc, "Enter the Bus ID you want to book: ");

                List<Bus> allBuses = busDAO.getAllBuses();
                int busCapacity = -1;
                for (Bus b : allBuses) {
                    if (b.getId() == busId) {
                        busCapacity = b.getCapacity();
                        break;
                    }
                }

                if (busCapacity == -1) {
                    System.out.println("Invalid Bus ID.");
                } else if (bookingDAO.isBusFull(busId, busCapacity)) {
                    System.out.println("Sorry, this bus is fully booked!");
                } else {
                    if (bookingDAO.bookTicket(user.getId(), busId)) {
                        System.out.println("Ticket booked successfully!");
                    } else {
                        System.out.println("Failed to book ticket.");
                    }
                }
            } else if (choice == 3) {
                bookingDAO.getMyBookings(user.getId());
            } else if (choice == 4) {
                int bookingId = getIntInput(sc, "Enter Booking ID to cancel: ");
                if (bookingDAO.cancelBooking(bookingId)) {
                    System.out.println("Booking canceled successfully!");
                } else {
                    System.out.println("Failed to cancel booking. Check the ID.");
                }
            } else if (choice == 5) {
                System.out.println("Logging out...");
                break;
            } else {
                System.out.println("Invalid choice.");
            }
        }
    }

    // --- THIS IS THE MAGIC METHOD THAT PREVENTS CRASHES ---
    private static int getIntInput(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                // Read as string first, then convert to int
                return Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a valid number (no letters).");
            }
        }
    }
}