import java.util.*;

class Booking {
    private static int nextBookingId = 1;

    private String bookingId;
    private User user;
    private Flight flight;
    private List<Passenger> passengers;

    public Booking(User user, Flight flight, List<Passenger> passengers) {
        this.bookingId = "B" + nextBookingId++;
        this.user = user;
        this.flight = flight;
        this.passengers = passengers;
        // Update available seats in the flight
        flight.setAvailableSeats(flight.getAvailableSeats() - passengers.size());
    }

    public User getUser() {
        return user;
    }

    public String getBookingId() {
        return bookingId;
    }

    public Flight getFlight() {
        return flight;
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }

    @Override
    public String toString() {
        return "Booking ID: " + bookingId +
                "\nFlight: " + flight +
                "\nPassengers: " + passengers.size();
    }
}