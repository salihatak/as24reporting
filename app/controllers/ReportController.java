package controllers;

import model.Contact;
import model.Listing;
import model.ListingByMount;
import play.mvc.Controller;
import play.mvc.Result;
import services.DataRetrieveService;
import services.ReportingService;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;

@Singleton
public class ReportController extends Controller {

    private static final String LISTINGS_CSV = "listings.csv";
    private static final String CONTACTS_CSV = "contacts.csv";

    private final DataRetrieveService dataRetrieveService;
    private final ReportingService reportingService;

    @Inject
    public ReportController(DataRetrieveService dataRetrieveService, ReportingService reportingService) {
        this.dataRetrieveService = dataRetrieveService;
        this.reportingService = reportingService;
    }

    public Result renderReport() {
        List<Listing> listingList = dataRetrieveService.retrieveListingData(LISTINGS_CSV);
        List<Contact> contactList = dataRetrieveService.retrieveContactData(CONTACTS_CSV);

        Map<String, Double> averageSellingPriceBySellerType = reportingService.calculateAverageSellingPriceBySellerType(listingList);
        Map<String, Long> percentageOfCarsByMake = reportingService.calculatePercentageOfCarsByMake(listingList);
        Double averageTop30Percentage = reportingService.calculateAveragePriceOfMostContacted(listingList, contactList);
        List<ListingByMount> listingByMounts = reportingService.calculateListingIdContactCountByMonth(listingList, contactList);

        return ok(views.html.index.render(averageSellingPriceBySellerType, percentageOfCarsByMake,averageTop30Percentage, listingByMounts));
    }
}
