package service;

import model.Customer;
import model.IRoom;
import model.Reservation;

import java.text.SimpleDateFormat;
import java.util.*;

public class ReservationService {
    private static ReservationService reservationService;
    private ArrayList<IRoom> rooms;
    private ArrayList<Reservation> reservations;
    private Collection<Reservation> customerReserve;

    private ReservationService() {
        this.rooms = new ArrayList<>();
        this.reservations = new ArrayList<>();
    }

    public static ReservationService getInstance() {
        if (reservationService == null) {
            reservationService = new ReservationService();
        }
        return reservationService;
    }

    public void addRoom(IRoom room) {
        rooms.add(room);
    }

    public IRoom getARoom(String roomId) {
        for (int i = 0; i < rooms.size(); i++) {
            if (rooms.get(i).getRoomNumber().equals(roomId)) {
                return rooms.get(i);
            }
        }
        return null;
    }

    public Reservation reserveARoom(Customer customer, IRoom room,
                                    Date checkInDate, Date checkOutDate) {
        Reservation reservation = new Reservation(customer, room, checkInDate,
                checkOutDate);
        reservations.add(reservation);
        return reservation;
    }


    public Collection<IRoom> findRooms(Date checkInDate, Date checkOutDate) {
        Collection<IRoom> availableRooms = new ArrayList<>();
        List<Reservation> tempReservations = new ArrayList<>();
        List<IRoom> tempRooms = new ArrayList<>();
        boolean flag = false;

        if(!reservations.isEmpty()) {
            for (Reservation reservation : reservations) {
                if ((reservation.getCheckInDate().equals(checkInDate)) && reservation.getCheckOutDate().equals(checkOutDate)) {
                    tempReservations.add(reservation);
                } else if ((reservation.getCheckInDate().compareTo(checkInDate) <= 0 && reservation.getCheckOutDate().compareTo(checkInDate) >= 0) ||
                        (reservation.getCheckInDate().compareTo(checkOutDate) <= 0 &&(reservation.getCheckOutDate().compareTo(checkOutDate)) >= 0)){
                    tempReservations.add(reservation);
                } else if((reservation.getCheckInDate().compareTo(checkInDate) > 0 && reservation.getCheckOutDate().compareTo(checkInDate) > 0) &&
                        (reservation.getCheckInDate().compareTo(checkOutDate) < 0 &&(reservation.getCheckOutDate().compareTo(checkOutDate)) < 0)) {
                    tempReservations.add(reservation);
                }
            }
        } else {
            availableRooms.addAll(rooms);
            return availableRooms;
        }


        if(!tempReservations.isEmpty()) {
            for (Reservation tempReservation : tempReservations) {
                tempRooms.add(tempReservation.getRoom());
            }
        }

        if (tempReservations.isEmpty()) {
            availableRooms.addAll(rooms);
            return availableRooms;
        } else {
            for (IRoom room : rooms) {
                if (!tempRooms.contains(room)) {
                    availableRooms.add(room);
                }
            }
        }

        return availableRooms;
    }

    public Collection<Reservation> getCustomerReservation(Customer customer) {
        this.customerReserve = new ArrayList<>();
        for (int i = 0; i < reservations.size(); i++) {
            if (reservations.get(i).getCustomer().equals(customer)) {
                customerReserve.add(reservations.get(i));
            }
        }
        return customerReserve;
    }

    public void printAllReservation() {
        if (reservations.isEmpty()) {
            System.out.println("No room is reserved");
        } else {
            for (Reservation reservation : reservations) {
                System.out.println(reservation);
            }
        }
    }

    public ArrayList<Reservation> getReservation() {
        return this.reservations;
    }

    public void printAllRooms() {
        for (IRoom room : rooms) {
            System.out.println(room);
        }
    }

    public ArrayList<IRoom> getRooms() {
        return rooms;
    }


}