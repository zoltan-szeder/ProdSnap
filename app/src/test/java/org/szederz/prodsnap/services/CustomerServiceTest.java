package org.szederz.prodsnap.services;

import org.junit.Test;
import org.szederz.prodsnap.entities.Customer;
import org.szederz.prodsnap.storage.BasketStorage;

import static org.junit.Assert.assertEquals;

public class CustomerServiceTest {
    @Test
    public void testNameGiven(){
        BasketStorage storage = new BasketStorage();
        CustomerService service = new CustomerService(storage);

        Customer customer = new Customer();
        customer.setDetails("detail");
        service.setCustomer(customer);

        assertEquals("detail", storage.load().getCustomer().getDetails());
    }

}