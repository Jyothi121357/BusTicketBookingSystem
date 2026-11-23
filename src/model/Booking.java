package model;

public class Booking {
    private int id;
    private int userId;
    private int busId;
    private String bookingDate;

    public Booking(int userId, int busId) {
        this.userId = userId;
        this.busId = busId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public int getBusId() {
        return busId;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }
}