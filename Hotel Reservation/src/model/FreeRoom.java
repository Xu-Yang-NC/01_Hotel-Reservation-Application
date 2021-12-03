package model;

public class FreeRoom extends Room{

    public FreeRoom(String roomNumber, Double price, RoomType enumeration) {
        super(roomNumber, 0.0, enumeration);
    }

    @Override
    public String toString() {
        String roomType;
        if(enumeration == RoomType.SINGLE){
            roomType = "Single bed";
        } else{
            roomType = "Double beds";
        }
        return roomNumber + " - " + roomType + "\n" +
                "Price: $" + price + " price per night";
    }
}
