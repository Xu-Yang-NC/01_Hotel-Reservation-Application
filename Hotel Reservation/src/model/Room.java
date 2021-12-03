package model;

public class Room implements IRoom{
    protected String roomNumber;
    protected Double price;
    protected RoomType enumeration;

    public Room(String roomNumber, Double price, RoomType enumeration) {
        this.roomNumber = roomNumber;
        this.price = price;
        this.enumeration = enumeration;
    }

    @Override
    public String getRoomNumber() {
        return this.roomNumber;
    }

    @Override
    public Double getRoomPrice() {
        return this.price;
    }

    @Override
    public RoomType getRoomType() {
        return this.enumeration;
    }

    @Override
    public boolean isFree() {
        if(price == 0) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        String roomType;
        if(enumeration == RoomType.SINGLE){
            roomType = "Single bed";
        } else{
            roomType = "Double beds";
        }
        return roomNumber + " - " + roomType + "  " +
                "Price: $" + price + " price per night";
    }
}
