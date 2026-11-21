package model;

public class Bus {
    private int id;
    private String busNumber;
    private int capacity;
    private String operator;

    public Bus() {}

    public Bus(String busNumber, int capacity, String operator) {
        this.busNumber = busNumber;
        this.capacity = capacity;
        this.operator = operator;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getBusNumber() { return busNumber; }
    public void setBusNumber(String busNumber) { this.busNumber = busNumber; }
    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    public String getOperator() { return operator; }
    public void setOperator(String operator) { this.operator = operator; }
}