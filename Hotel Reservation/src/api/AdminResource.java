package api;

import model.Customer;
import model.IRoom;
import model.Reservation;
import service.CustomerService;
import service.ReservationService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class AdminResource {
    private static AdminResource adminResource;

    private AdminResource(){};
    public static AdminResource getInstance(){
        if(adminResource == null){
            adminResource = new AdminResource();
        }
        return adminResource;
    }

    public Customer getCustomer(String email){
        return CustomerService.getInstance().getCustomer(email);
    }

    public void addRoom(List<IRoom> rooms){
        for(IRoom room : rooms) {
            ReservationService.getInstance().addRoom(room);
        }
    }

    public Collection<Customer> getAllCustomers(){
        return CustomerService.getInstance().getAllCustomers();
    }

    public void displayAllReservations(){
        ReservationService.getInstance().printAllReservation();
    }

    public void displayAllRooms(){
        ReservationService.getInstance().printAllRooms();
    }

    public ArrayList<Reservation> getRoomReservation(){
       return ReservationService.getInstance().getReservation();
    }

    public ArrayList<IRoom> getRooms(){
        return ReservationService.getInstance().getRooms();
    }
}
