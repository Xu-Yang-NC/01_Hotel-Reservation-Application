import api.AdminResource;
import api.HotelResource;
import model.*;
import service.CustomerService;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

public class Application {
    public static void main(String[] args) {
        List<IRoom> rooms = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        mainMenuSelection(scanner, rooms);

    }

    // Main menu printing and choice selecting
    private static void mainMenuSelection(Scanner scanner, List<IRoom> rooms) {
        MainMenu.printMainMenu();
        boolean flag = false;
        do {
            try {
                int choice = scanner.nextInt();
                scanner.nextLine();
                // Handle the user input out of selection range
                if (choice < 1 || choice > 5) {
                    System.out.println("Please enter a number between 1 and 5");
                    flag = true;
                }
                // Switch for selections
                switch (choice) {
                    case 1:
                        findAndReserve(scanner, rooms);
                        break;
                    case 2:
                        viewReservation(scanner, rooms);
                        mainMenuSelection(scanner, rooms);
                        break;
                    case 3:
                        createAccount(scanner, rooms);
                        break;
                    case 4:
                        adminMenuSelection(scanner, flag, rooms);
                        break;
                    case 5:
                        System.exit(0);
                        break;
                }
                // Handling the exception to non-number user input
            } catch (Exception ex) {
                System.out.println("Enter number");
                scanner.nextLine();
                flag = true;
            }

        } while (flag);

    }

    // Method to print admin menu and make selection
    private static void adminMenuSelection(Scanner scanner, boolean flag, List<IRoom> rooms) {
        Collection<Customer> tempCustomers = new ArrayList<>();
//        List<IRoom> rooms = new ArrayList<>();
        AdminMenu.printAdminMenu();
        do {
            try {
                int selection = scanner.nextInt();
                scanner.nextLine();
                // Handle the user input out of selection range
                if (selection < 1 || selection > 5) {
                    System.out.println("Please enter a number between 1 and 5");
                    flag = true;
                }
                switch (selection) {
                    case 1:
                        System.out.println("List of Customers:");
                        tempCustomers = AdminResource.getInstance().getAllCustomers();
                        for (Customer customer : tempCustomers) {
                            System.out.println(customer);
                        }
                        adminMenuSelection(scanner, flag, rooms);
                        break;
                    case 2:
                        System.out.println("List of All Rooms:");
                        AdminResource.getInstance().displayAllRooms();
                        adminMenuSelection(scanner, flag, rooms);
                        break;
                    case 3:
                        System.out.println("List of All Reservations:");
                        AdminResource.getInstance().displayAllReservations();
                        adminMenuSelection(scanner, flag, rooms);
                        break;
                    case 4:
                        IRoom room = createRoom(scanner, rooms);
                        rooms.add(room);
                        System.out.println("Would you like to add another room y/n");
                        char choice = enterChoice(scanner);
                        while (choice == 'y' || choice == 'Y') {
                            room = createRoom(scanner, rooms);
                            rooms.add(room);
                            System.out.println("Would you like to add another room y/n");
                            choice = enterChoice(scanner);
                        }
                        AdminResource.getInstance().addRoom(rooms);
                        rooms.clear();
                        adminMenuSelection(scanner, flag, rooms);
                        break;
                    case 5:
                        mainMenuSelection(scanner, rooms);
                        break;
                }
            } catch (Exception ex) {
                System.out.println("Enter number");
                scanner.nextLine();
                flag = true;
            }
        } while (flag);
    }

    // Method for find and reserve a room
    private static void findAndReserve(Scanner scanner, List<IRoom> rooms) {
        System.out.println("Enter ChcekIn Date mm/dd/yyyy example 01/01/2021");
        Date checkIn = new Date(enterDate(scanner));
        System.out.println("Enter CheckOut Date mm/dd/yyyy example 01/02/2021");
        Date checkOut = new Date(enterDate(scanner));
        // Make sure the checkOut date is after checkin date
        while (checkOut.compareTo(checkIn) < 0) {
            System.out.println("Please enter a date after checkin date");
            checkOut = new Date(enterDate(scanner));
        }

        boolean flag = true;
        Collection<IRoom> tempScore = new ArrayList<>(HotelResource.getInstance().findARoom(checkIn, checkOut));
        while(flag){
            if(tempScore.isEmpty()){
                Calendar calendar = Calendar.getInstance();
                calendar.set(checkIn.getYear()+1900,checkIn.getMonth(),
                        checkIn.getDate()+7);
                checkIn = calendar.getTime();
                calendar.set(checkOut.getYear()+1900,checkOut.getMonth(),
                        checkOut.getDate()+7);
                checkOut = calendar.getTime();
                SimpleDateFormat myFormat = new SimpleDateFormat("EEE MMM dd " +
                        "yyyy");
                System.out.println("There is no room available for current time. " +
                        "Following is a list for available room from " + myFormat.format(checkIn) + " to " + myFormat.format(checkOut) );
                tempScore = HotelResource.getInstance().findARoom(checkIn, checkOut);
            } else {
                flag = false;
            }

        }

        // Print all the available rooms
        for (IRoom room : HotelResource.getInstance().findARoom(checkIn, checkOut)) {
            System.out.println(room);
        }

        System.out.println("\n");
        System.out.println("Would you like to book a room? y/n");
        char decision = enterChoice(scanner);
        if (decision == 'y' || decision == 'Y') {
            System.out.println("Do you have an account with us? y/n");
            decision = enterChoice(scanner);
            if (decision == 'y' || decision == 'Y') {
                System.out.println("Enter Email Format: name@domain.com");
                String email = enterEmail(scanner);
                flag = true;
                while (true) {
                    // Making sure there is a customer created with the email enter.
                    if (HotelResource.getInstance().getCustomer(email) != null) {
                        System.out.println("What room number would you like to reserve");
                        String roomId = enterRoomId(scanner, rooms,tempScore);
                        // getting the room info with the room number entered.
                        IRoom room = HotelResource.getInstance().getRoom(roomId);
                        // Add the room to booked room list.
                        HotelResource.getInstance().bookARoom(email, room,
                                checkIn,
                                checkOut);


                        // Print out all the room customer reserved
                        for(Reservation reservation : HotelResource.getInstance().getCustomersReservation(email)){
                            System.out.println(reservation);
                        }
                        mainMenuSelection(scanner, rooms);
                        break;
                    } else {
                        System.out.println("The email is not exist, creating an " +
                                "account y or n");
                        char choice = enterChoice(scanner);
                        if (choice == 'y' || choice == 'Y') {
                            createAccount(scanner, rooms);
                        } else {
                            mainMenuSelection(scanner, rooms);
                        }
                    }
                }
            } else {
                System.out.println("Creating an account y or n");
                char choice = enterChoice(scanner);
                if (choice == 'y' || choice == 'Y') {
                    createAccount(scanner, rooms);
                } else {
                    mainMenuSelection(scanner, rooms);
                }
            }
        } else {
            mainMenuSelection(scanner, rooms);
        }
    }

    // Method for reviewing all the reservation based on customer email
    private static void viewReservation(Scanner scanner, List<IRoom> rooms) {
        System.out.println("Please enter your email");
        String email = enterEmail(scanner);
        if (HotelResource.getInstance().getCustomer(email) != null) {
            if (HotelResource.getInstance().getCustomersReservation(email) != null) {
                for(Reservation reservation : HotelResource.getInstance().getCustomersReservation(email)){
                    System.out.println(reservation);
                }

            } else {
                System.out.println("No reservation found");
                mainMenuSelection(scanner, rooms);
            }
            // If email is not found, it will return to main menu
        } else {
            System.out.println("Email does not exist");
            mainMenuSelection(scanner, rooms);
        }
    }

    // Method using to create an account
    private static void createAccount(Scanner scanner, List<IRoom> rooms) {
        System.out.println("Enter Email (format: name@domain.com)");
        String email = enterEmail(scanner);
        boolean flag = true;
        while (flag) {
            if (HotelResource.getInstance().getCustomer(email) == null) {
                System.out.println("First name");
                String firstName = scanner.nextLine();
                System.out.println("Last name");
                String lastName = scanner.nextLine();
                HotelResource.getInstance().createACustomer(email, firstName, lastName);
                flag = false;
                mainMenuSelection(scanner, rooms);
            } else {
                System.out.println("The email is used by someone else, please " +
                        "enter another email");
                email = enterEmail(scanner);
            }
        }
    }


    // Method of entering date
    private static String enterDate(Scanner scanner) {
        String regex = "^(1[0-2]|0[1-9])/(3[01]|[12][0-9]|0[1-9])/[0-9]{4}$";
        Pattern pattern = Pattern.compile(regex);

        String dateEnter = scanner.nextLine();

        // Making sure customer enter the email with correct format
        while (!pattern.matcher(dateEnter).matches()) {
            System.out.println("Please enter with correct format");
            dateEnter = scanner.nextLine();
        }

        return dateEnter;
    }

    // Method of entering y or n choice
    private static char enterChoice(Scanner scanner) {
        boolean flag = false;
        do {
            // Making sure customer entering letter
            try {
                char choice = scanner.next().charAt(0);
                scanner.nextLine();
                // Making sure customer entering y or n regarding the case.
                if (choice == 'n' || choice == 'N' || choice == 'Y' || choice == 'y') {
                    flag = false;
                    return choice;
                } else {
                    System.out.println("Please enter y or n");
                    flag = true;
                }
                // Catching the non-letter user input.
            } catch (Exception ex) {
                System.out.println("Please enter y or n");
                scanner.next();
                flag = true;
            }
        } while (flag);

        return 'n';
    }

    // Method of entering email
    private static String enterEmail(Scanner scanner) {
        String email = scanner.nextLine();
        String regex = "^(.+)@(.+).(com)$";
        Pattern pattern = Pattern.compile(regex);
        // Making sure the email is entered with correct format
        while (!pattern.matcher(email).matches()) {
            System.out.println("Please enter with correct format");
            email = scanner.nextLine();
        }
        return email;
    }

    // Method of entering room number
    private static String enterRoomId(Scanner scanner, List<IRoom> rooms,Collection<IRoom> tempScore) {
        String roomId = scanner.nextLine();
        List<String> roomIds = new ArrayList<>();
        for(IRoom room: tempScore){
            roomIds.add(room.getRoomNumber());
        }
        boolean flag = true;
        while(true){
            if(HotelResource.getInstance().getRoom(roomId) == null){
                System.out.println("The room is not exist, enter another number or press q to return to the main menu");
                roomId = scanner.nextLine();
                if (roomId.equals("q") || roomId.equals("Q")) {
                    mainMenuSelection(scanner, rooms);
                    break;
                }
            } else {
                if(!roomIds.contains(roomId)){
                    System.out.println("The room is reserved, enter another " +
                            "number");
                    roomId = scanner.nextLine();
                } else {
                    flag = false;
                    return roomId;
                }
            }

        }

        return roomId;
    }

    // Method for creating a room
    private static IRoom createRoom(Scanner scanner, List<IRoom> rooms) {
        boolean flag = true;
        System.out.println("Enter room number");
        String roomId = scanner.nextLine();
        for (int i = 0; i < AdminResource.getInstance().getRooms().size(); i++) {
            while (AdminResource.getInstance().getRooms().get(i).getRoomNumber().equals(roomId)) {
                System.out.println("The room is already exist, enter another room " +
                        "number or press q to return to the main menu");
                roomId = scanner.nextLine();
                if (roomId.equals("q") || roomId.equals("Q")) {
                    mainMenuSelection(scanner, rooms);
                    break;
                }
            }
        }
        for(int i = 0; i < rooms.size(); i++){
            while(rooms.get(i).getRoomNumber().equals(roomId)){
                System.out.println("The room is already exist, enter another room " +
                        "number or press q to return to the main menu");
                roomId = scanner.nextLine();
                if (roomId.equals("q") || roomId.equals("Q")) {
                    mainMenuSelection(scanner, rooms);
                    break;
                }
            }
        }

        System.out.println("Enter price");
        double price = 0.0;
        while (flag) {
            try {
                price = scanner.nextDouble();
                scanner.nextLine();
                flag = false;
            } catch (Exception ex) {
                System.out.println("Please enter number");
                scanner.nextLine();
            }
        }
        System.out.println("Enter room type: 1 for single bed, 2 for double bed");
        RoomType singleBed = RoomType.SINGLE;
        RoomType doubleBed = RoomType.DOUBLE;
        RoomType selection = null;
        int oneOrTwo = 0;
        flag = true;
        while (flag) {
            try {
                oneOrTwo = scanner.nextInt();
                scanner.nextLine();
                if (oneOrTwo == 1) {
                    selection = singleBed;
                    flag = false;
                } else if (oneOrTwo == 2) {
                    selection = doubleBed;
                    flag = false;
                } else {
                    System.out.println("Please enter 1 or 2");
                }
            } catch (Exception ex) {
                System.out.println("Please enter number");
                scanner.nextLine();
            }
        }
        return new Room(roomId, price, selection);
    }
}