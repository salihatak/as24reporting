package services.impl;

import exception.FileParseException;
import model.Contact;
import model.Listing;
import org.junit.Before;
import org.junit.Test;
import play.Environment;
import validator.UploadValidator;

import java.io.*;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;

import static java.nio.file.Files.createTempFile;
import static java.nio.file.Files.write;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ResourceDataRetrieveServiceTest {

    private ResourceDataRetrieveService resourceDataRetrieveService;
    private Environment environment;
    private UploadValidator uploadValidator;

    @Before
    public void setUp() {
        environment = mock(Environment.class);
        uploadValidator = mock(UploadValidator.class);
        resourceDataRetrieveService = new ResourceDataRetrieveService(environment, uploadValidator);
    }

    @Test
    public void should_retrieve_contact_data_from_resources() {
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
    public void should_retrieve_listing_data_from_resources() {
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

    @Test
    public void should_retrieve_contact_data_from_file_upload() throws IOException {
        //given
        Path contactFilePath = createTempFile(null, "contactTempFile");
        write(contactFilePath, "\"listing_id\",\"contact_date\"\n1244,1592498493000\n1085,1582474057000\n1288,1579365755000".getBytes("utf-8"));
        File contactFile = contactFilePath.toFile();

        //when
        List<Contact> contacts = resourceDataRetrieveService.retrieveContactData(contactFile);

        //then
        assertThat(contacts)
                .isNotNull()
                .hasSize(3)
                .extracting("id", "contactDate")
                .contains(
                        tuple(1244L, new Date(1592498493000L)),
                        tuple(1085L, new Date(1582474057000L)),
                        tuple(1288L, new Date(1579365755000L))
                );
    }

    @Test
    public void should_retrieve_listing_data_from_file_upload() throws IOException {
        //given
        Path listingTempFile = createTempFile(null, "listingTempFile");
        write(listingTempFile, "\"id\",\"make\",\"price\",\"mileage\",\"seller_type\"\n1000,\"Audi\",49717,6500,\"private\"\n1001,\"Mazda\",22031,7000,\"private\"\n1002,\"BWM\",17742,6000,\"dealer\"".getBytes("utf-8"));
        File listingFile = listingTempFile.toFile();

        //when
        List<Listing> listingList = resourceDataRetrieveService.retrieveListingData(listingFile);

        //then
        assertThat(listingList)
                .isNotNull()
                .hasSize(3)
                .extracting("id", "make", "price", "mileage", "sellerType")
                .contains(
                        tuple(1000L, "Audi", 49717d, 6500L, "private"),
                        tuple(1001L, "Mazda", 22031d, 7000L, "private"),
                        tuple(1002L, "BWM", 17742d, 6000L, "dealer")
                );
    }

    @Test
    public void should_throw_exception_while_retrieve_contact_data_resources_listing_id_column_format_problem() {
        //given
        String contactsFileName = "contacts.csv";
        String csvString = "\"listing_id\",\"contact_date\"\n" +
                "1244L,1592498493000";
        InputStream inputStream = new ByteArrayInputStream(csvString.getBytes());
        when(environment.resourceAsStream(contactsFileName)).thenReturn(inputStream);

        //when
        Throwable throwable = catchThrowable(() -> resourceDataRetrieveService.retrieveContactData(contactsFileName)) ;

        //then
        assertThat(throwable).isNotNull()
                .isInstanceOf(FileParseException.class);
    }

    @Test
    public void should_throw_exception_while_retrieve_listing_data_from_resources_price_colum_format_problem() {
        //given
        String listingsFileName = "contacts.csv";
        String csvString = "\"id\",\"make\",\"price\",\"mileage\",\"seller_type\"\n" +
                "1000,\"Audi\",49717AAA,6500,\"private\"";
        InputStream inputStream = new ByteArrayInputStream(csvString.getBytes());
        when(environment.resourceAsStream(listingsFileName)).thenReturn(inputStream);

        //when
        Throwable throwable = catchThrowable(() -> resourceDataRetrieveService.retrieveListingData(listingsFileName));

        //then
        assertThat(throwable).isNotNull()
                .isInstanceOf(FileParseException.class);
    }
}