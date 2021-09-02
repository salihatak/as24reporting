package services;

import com.google.inject.ImplementedBy;
import model.Contact;
import model.Listing;
import model.ListingByMount;
import services.impl.ReportingServiceImpl;

import java.util.List;
import java.util.Map;

@ImplementedBy(ReportingServiceImpl.class)
public interface ReportingService {

    Map<String, Double> calculateAverageSellingPriceBySellerType(List<Listing> listingList);

    Map<String, Long> calculatePercentageOfCarsByMake(List<Listing> listingList);

    Double calculateAveragePriceOfMostContacted(List<Listing> listingList, List<Contact> contactList);

    List<ListingByMount> calculateListingIdContactCountByMonth(List<Listing> listingList, List<Contact> contactList);
}
