package services.impl;

import model.Contact;
import model.Listing;
import play.Environment;
import services.DataRetrieveService;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ResourceDataRetrieveService implements DataRetrieveService {

    private static final String COMMA = ",";
    private final Environment environment;

    @Inject
    public ResourceDataRetrieveService(Environment environment){
        this.environment = environment;
    }

    public List<Contact> retrieveContactData(String contactsFile) {
        List<Contact> contactList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(retrieveResourceAsInputStreamReader(contactsFile))) {
            contactList = br.lines().skip(1).map(this::mapToContact).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contactList;
    }

    public List<Listing> retrieveListingData(String listingFile) {
        List<Listing> listingList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(retrieveResourceAsInputStreamReader(listingFile))) {
            listingList = br.lines().skip(1).map(this::mapToListing).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listingList;
    }

    private Contact mapToContact(String line) {
        String[] columns = line.split(COMMA);
        return new Contact(Long.parseLong(columns[0]), new Date(Long.parseLong(columns[1])));
    }

    private InputStreamReader retrieveResourceAsInputStreamReader(String fileName) {
        InputStream inputStream = environment.resourceAsStream(fileName);
        return new InputStreamReader(inputStream);
    }

    private Listing mapToListing(String line) {
        String[] columns = line.split(COMMA);
        return new Listing(Long.parseLong(columns[0]), columns[1], Double.parseDouble(columns[2]), Long.parseLong(columns[3]), columns[4]);
    }
}
