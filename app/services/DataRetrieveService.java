package services;

import com.google.inject.ImplementedBy;
import model.Contact;
import model.Listing;
import services.impl.ResourceDataRetrieveService;

import java.util.List;

@ImplementedBy(ResourceDataRetrieveService.class)
public interface DataRetrieveService {

    List<Contact> retrieveContactData(String contactsFile);

    List<Listing> retrieveListingData(String listingFile);
}
