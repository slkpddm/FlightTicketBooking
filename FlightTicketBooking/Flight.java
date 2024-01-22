import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

class Flight {
    private String flightId;
    private String source;
    private String destination;
    private int totalSeats;
    private int availableSeats;
    private Date date;

    public Flight(String flightId, String source, String destination, int totalSeats, int availableSeats, String date) {
        this.flightId = flightId;
        this.source = source;
        this.destination = destination;
        this.totalSeats = totalSeats;
        this.availableSeats = availableSeats;
        try {
            this.date = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (ParseException e) {
            e.printStackTrace(); // Handle the exception according to your needs
        }
    }

    public String getFlightId() {
        return flightId;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Flight " + flightId + " - " + source + " to " + destination +
                " | Date: " + new SimpleDateFormat("yyyy-MM-dd").format(date) +
                " | Available Seats: " + availableSeats;
    }
}