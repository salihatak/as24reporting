package services.impl;

import model.Contact;
import model.Listing;
import model.ListingByMount;
import model.ListingWithContactCount;
import services.ReportingService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.*;
import static java.util.stream.Collectors.toList;

public class ReportingServiceImpl implements ReportingService {

    private static final int TOP_LISTINS_SIZE = 5;
    private static final DateFormat MONTH_FORMAT = new SimpleDateFormat("MM.yyyy");

    @Override
    public Map<String, Double> calculateAverageSellingPriceBySellerType(List<Listing> listingList) {
        return listingList.stream()
                .collect(groupingBy(Listing::getSellerType, averagingDouble(Listing::getPrice)));
    }

    @Override
    public Map<String, Long> calculatePercentageOfCarsByMake(List<Listing> listingList) {
        long totalCount = listingList.size();
        return listingList.stream()
                .collect(groupingBy(Listing::getMake, collectingAndThen(toList(), listings -> listings.size() * 100L / totalCount)));
    }

    @Override
    public Double calculateAveragePriceOfMostContacted(List<Listing> listingList, List<Contact> contactList) {
        Map<Long, Long> listingIdByContactCount = contactList.stream()
                .collect(groupingBy(Contact::getId, counting()));

        int countOf30Percentage = listingList.size() * 30 / 100;
        return listingList.stream()
                .filter(listing -> Objects.nonNull(listingIdByContactCount.get(listing.getId())))
                .map(listing -> new ListingWithContactCount(listing, listingIdByContactCount.get(listing.getId())))
                .sorted(comparing(ListingWithContactCount::getContactCount).reversed())
                .limit(countOf30Percentage)
                .collect(averagingDouble(Listing::getPrice));
    }

    @Override
    public List<ListingByMount> calculateListingIdContactCountByMonth(List<Listing> listingList, List<Contact> contactList) {
        Map<String, Map<Long, Long>> listingIdByContactCount = contactList.stream()
                .collect(groupingBy( contact -> MONTH_FORMAT.format(contact.getContactDate()) , groupingBy(Contact::getId, counting())));
        return listingIdByContactCount.entrySet().stream()
                .map(mapEntry -> retrieveTop5Listings(listingList, mapEntry))
                .sorted(comparing(ListingByMount::getMonth))
                .collect(toList());
    }

    private ListingByMount retrieveTop5Listings(List<Listing> listingList, Map.Entry<String, Map<Long, Long>> mapEntry) {
        List<ListingWithContactCount> top5ListingByMonth = listingList.stream()
                .filter(listing -> Objects.nonNull(mapEntry.getValue().get(listing.getId())))
                .map(listing -> new ListingWithContactCount(listing, mapEntry.getValue().get(listing.getId())))
                .sorted(comparing(ListingWithContactCount::getContactCount).reversed())
                .limit(TOP_LISTINS_SIZE)
                .collect(toList());
        return new ListingByMount(mapEntry.getKey(), top5ListingByMonth);
    }
}
