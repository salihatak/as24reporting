package services.impl;

import model.Contact;
import model.Listing;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import play.Environment;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.Mockito.*;

public class ResourceDataRetrieveServiceTest {

    private ResourceDataRetrieveService resourceDataRetrieveService;
    private Environment environment;

    @Before
    public void setUp() {
        environment = mock(Environment.class);
        resourceDataRetrieveService = new ResourceDataRetrieveService(environment);
    }

    @Test
    public void should_retrieve_contact_data() {
        //given
        String contactsFileName = "contacts.csv";
        String csvString = "\"listing_id\",\"contact_date\"\n" +
                "1244,1592498493000\n" +
                "1085,1582474057000\n" +
                "1288,1579365755000\n" +
                "1231,1585159440000";
        InputStream inputStream = new ByteArrayInputStream(csvString.getBytes());
        when(environment.resourceAsStream(contactsFileName)).thenReturn(inputStream);

        //when
        List<Contact> contacts = resourceDataRetrieveService.retrieveContactData(contactsFileName);

        //then
        assertThat(contacts)
                .isNotNull()
                .hasSize(4)
                .extracting("id", "contactDate")
                .contains(
                        tuple(1244L, new Date(1592498493000L)),
                        tuple(1085L, new Date(1582474057000L)),
                        tuple(1288L, new Date(1579365755000L)),
                        tuple(1231L, new Date(1585159440000L))
                );
    }

    @Test
    public void should_retrieve_listing_data() {
        //given
        String listingsFileName = "contacts.csv";
        String csvString = "\"id\",\"make\",\"price\",\"mileage\",\"seller_type\"\n" +
                "1000,\"Audi\",49717,6500,\"private\"\n" +
                "1001,\"Mazda\",22031,7000,\"private\"\n" +
                "1002,\"BWM\",17742,6000,\"dealer\"\n" +
                "1003,\"Toyota\",11768,0,\"dealer\"\n" +
                "1004,\"Mazda\",25219,3000,\"other\"";
        InputStream inputStream = new ByteArrayInputStream(csvString.getBytes());
        when(environment.resourceAsStream(listingsFileName)).thenReturn(inputStream);

        //when
        List<Listing> listingList = resourceDataRetrieveService.retrieveListingData(listingsFileName);

        //then
        assertThat(listingList)
                .isNotNull()
                .hasSize(5)
                .extracting("id", "make", "price", "mileage", "sellerType")
                .contains(
                        tuple(1000L, "Audi", 49717d, 6500L, "private"),
                        tuple(1001L, "Mazda", 22031d, 7000L, "private"),
                        tuple(1002L, "BWM", 17742d, 6000L, "dealer"),
                        tuple(1003L, "Toyota", 11768d, 0L, "dealer"),
                        tuple(1004L, "Mazda", 25219d, 3000L, "other")
                );
    }

}