import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
class FlightBookingSystem {
    private List<User> users;
    private List<Flight> flights;
    private List<Booking> bookings;
    private User currentUser;
    private boolean isLoggedIn = false;

    public FlightBookingSystem() {
        users = new ArrayList<>();
        flights = new ArrayList<>();
        bookings = new ArrayList<>();
        initializeData();
    }

    private void initializeData() {
        // Add sample data (flights, users) here
        flights.add(new Flight("F001", "Chennai", "Bangalore", 50, 30, "2024-01-18"));
        flights.add(new Flight("F009", "Chennai", "Bangalore", 50, 30, "2024-01-18"));
        flights.add(new Flight("F010", "Chennai", "Bangalore", 50, 30, "2024-01-18"));
        flights.add(new Flight("F002", "Hyderabad", "Delhi", 100, 80, "2024-01-19"));
        flights.add(new Flight("F003", "Bangalore", "Kochi", 50, 30, "2024-01-20"));
        flights.add(new Flight("F004", "Delhi", "Kolkata", 100, 80, "2024-01-19"));
        flights.add(new Flight("F005", "Kolkata", "Bangalore", 50, 30, "2024-01-21"));
        flights.add(new Flight("F006", "Kochi", "Chennai", 100, 80, "2024-01-19"));
        flights.add(new Flight("F007", "Chennai", "Hyderabad", 50, 30, "2024-01-18"));
        flights.add(new Flight("F008", "Hyderabad", "Kochi", 100, 80, "2024-01-19"));
        // Add more flights and users as needed
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            if (currentUser == null) {
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Search Flights");
                System.out.println("4. Exit");
                System.out.print("Enter choice: ");
                int choice = getIntInput(scanner);

                switch (choice) {
                    case 1:
                        registerUser(scanner);
                        break;
                    case 2:
                        loginUser(scanner);
                        break;
                    case 3:
                        searchFlights(scanner);
                        break;
                    case 4:
                        exit = true;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } else {
                System.out.println("1. Search Flights");
                System.out.println("2. Check Bookings");
                System.out.println("3. Exit");
                System.out.print("Enter choice: ");
                int choice = getIntInput(scanner);

                switch (choice) {
                    case 1:
                        searchFlights(scanner);
                        break;
                    case 2:
                        viewBookingHistory(currentUser);
                        break;
                    case 3:
                        exit = true;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        }

        scanner.close();
    }

    private void registerUser(Scanner scanner) {
        System.out.print("Enter username: ");
        scanner.nextLine();
        String username = scanner.nextLine();

        // Check if the username already exists
        if (userExists(username)) {
            System.out.println("Username already exists. Please choose a different one.");
            return;
        }

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        System.out.print("Enter gender (MALE, FEMALE, OTHER): ");
        String genderInput = scanner.nextLine();
        Gender gender = parseGenderInput(genderInput);

        // Create and add the new user
        User newUser = new User(generateUserId(), username, password, gender);
        users.add(newUser);
        System.out.println("Registration successful. Please login to continue.");
    }

    private void loginUser(Scanner scanner) {

        System.out.print("Enter username: ");
        scanner.nextLine();
        String username = scanner.nextLine().trim();

        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();

        // Check if the entered credentials match any registered user
        User matchedUser = validateUserCredentials(username, password);

        if (matchedUser != null) {
            currentUser = matchedUser;
            System.out.println("Login successful. Welcome, " + currentUser.getUsername() + "!");
            isLoggedIn = true;

        } else {
            System.out.println("Invalid username or password. Please try again.");
        }
    }

    private void searchFlights(Scanner scanner) {
        System.out.println("Enter source airport: ");
        scanner.nextLine();
        String source = scanner.nextLine();
        System.out.println("Enter destination airport: ");
        String destination = scanner.nextLine();

        System.out.print("Enter journey date (YYYY-MM-DD): ");
        String journeyDate = scanner.nextLine().trim();

        List<Flight> availableFlights = getAvailableFlights(source, destination, journeyDate, 0);

        if (availableFlights.isEmpty()) {
            System.out.println("No available flights for the selected criteria.");
            return;
        }

        System.out.println("Available Flights:");
        for (Flight flight : availableFlights) {
            System.out.println(flight);
        }
        if (isLoggedIn == true) {
            System.out.print("Enter the Flight ID to book: ");
            String selectedFlightId = scanner.nextLine();
            Flight selectedFlight = getFlightById(selectedFlightId);

            if (selectedFlight != null && selectedFlight.getAvailableSeats() > 0) {
                System.out.print("Enter the number of seats to book: ");
                int numSeats = getIntInput(scanner);

                if (numSeats > 0 && numSeats <= selectedFlight.getAvailableSeats()) {
                    List<Passenger> passengers = new ArrayList<>();
                    scanner.nextLine();
                    for (int i = 0; i < numSeats; i++) {
                        System.out.print("Enter passenger name for seat " + (i + 1) + ": ");

                        String passengerName = scanner.nextLine();
                        passengers.add(new Passenger(passengerName));
                    }

                    Booking booking = new Booking(currentUser, selectedFlight, passengers);
                    bookings.add(booking);

                    displayBookingOptions(scanner, booking);
                } else {
                    System.out.println("Invalid number of seats. Please try again.");
                }
            } else {
                System.out.println("Invalid Flight ID or no available seats.");
            }
        } else {
            System.out.println("Please login to book tickets");
        }
    }

    private List<Flight> getAvailableFlights(String source, String destination, String journeyDate, int i) {
        List<Flight> resultFlights = new ArrayList<>();
        Date date1 = parseDate(journeyDate);
        for (Flight flight : flights) {
            Date date2 = flight.getDate();
            if (flight.getAvailableSeats() > 0 && date1.compareTo(date2) == 0
                    && flight.getSource().equalsIgnoreCase(source)
                    && flight.getDestination().equalsIgnoreCase(destination)) {
                resultFlights.add(flight);
            }
        }

        return resultFlights;
    }

    private Date parseDate(String dateStr) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace(); // Handle the exception according to your needs
            return null;
        }
    }

    private Flight getFlightById(String flightId) {
        for (Flight flight : flights) {
            if (flight.getFlightId().equals(flightId)) {
                return flight;
            }
        }
        return null;
    }

    private void displayBookingOptions(Scanner scanner, Booking booking) {
        System.out.println("1. View Booking Details");
        System.out.println("2. Cancel Booking");
        System.out.print("Enter choice: ");
        int choice = getIntInput(scanner);

        switch (choice) {
            case 1:
                viewBookingDetails(booking);
                break;
            case 2:
                cancelBooking(booking);
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private void viewBookingDetails(Booking booking) {
        System.out.println("Booking Details:");
        System.out.println("Booking ID: " + booking.getBookingId());
        System.out.println("Flight: " + booking.getFlight());
        System.out.println("Passengers:");
        for (Passenger passenger : booking.getPassengers()) {
            System.out.println(passenger.getName());
        }
    }

    private void cancelBooking(Booking booking) {
        // Implement cancellation logic (remove booking from the list, free up seats,
        // etc.)
        bookings.remove(booking);
        System.out.println("Booking cancelled successfully.");
    }

    private int getIntInput(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a valid integer.");
            scanner.next(); // Consume the invalid input
        }
        return scanner.nextInt();
    }

    private boolean userExists(String username) {
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return true;
            }
        }
        return false;
    }

    private String generateUserId() {
        return "U" + (users.size() + 1);
    }

    private Gender parseGenderInput(String genderInput) {
        try {
            return Gender.valueOf(genderInput.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid gender input. Using default: OTHER");
            return Gender.OTHER;
        }
    }

    private User validateUserCredentials(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username) && user.checkPassword(password)) {
                return user;
            }
        }
        return null;
    }

    private void viewBookingHistory(User user) {
        // List < Booking > bookings;

        for (Booking booking : bookings) {
            if (booking.getUser() == currentUser) {
                System.out.println("Booking Id " + booking.getBookingId() + " || " + booking.getUser()
                        + " || Passengers: " + booking.getPassengers());
            }

        }

    }
}