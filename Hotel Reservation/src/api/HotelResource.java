package api;

import model.Customer;
import model.IRoom;
import model.Reservation;
import service.CustomerService;
import service.ReservationService;

import java.util.Collection;
import java.util.Date;

public class HotelResource {
    private static HotelResource hotelResource;

    private HotelResource(){}
    public static HotelResource getInstance(){
        if(hotelResource == null){
            hotelResource = new HotelResource();
        }
        return hotelResource;
    }

    public Customer getCustomer(String email){
       return CustomerService.getInstance().getCustomer(email);
    }

    public void createACustomer(String email,String firstName,String lastName){
        CustomerService.getInstance().addCustomer(email, firstName, lastName);
    }

    public IRoom getRoom(String roomNumber){
        return ReservationService.getInstance().getARoom(roomNumber);
    }

    public Reservation bookARoom(String customerEmail, IRoom room,
                                 Date checkInDate, Date checkOutDate){

        return ReservationService.getInstance().reserveARoom(getCustomer(customerEmail),room,
                checkInDate,checkOutDate);
    }

    public Collection<Reservation> getCustomersReservation(String customerEmail){
        return ReservationService.getInstance().getCustomerReservation(getCustomer(customerEmail));
    }

    public Collection<IRoom> findARoom(Date checkIn, Date checkOut){
        return ReservationService.getInstance().findRooms(checkIn, checkOut);
    }

}
