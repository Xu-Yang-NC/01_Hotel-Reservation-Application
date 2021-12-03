package service;

import model.Customer;
import model.IRoom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CustomerService {
    private static CustomerService customerService;
    private Map<String,Customer> customers;

    private CustomerService(){
        this.customers = new HashMap<>();
    }

    public static CustomerService getInstance(){
        if(customerService == null){
            customerService = new CustomerService();
        }
        return customerService;
    }

    public void addCustomer(String email, String firstName, String lastName){
        customers.put(email, new Customer(firstName, lastName, email));
    }

    public Customer getCustomer(String customerEmail){
        return customers.get(customerEmail);
    }

    public Collection<Customer> getAllCustomers(){
        Collection<Customer> customersInfo = new ArrayList<>();
        customersInfo = customers.values();
        return customersInfo;
    }
}
