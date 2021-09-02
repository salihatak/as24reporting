package controllers;

import model.Contact;
import model.Listing;
import model.ListingByMount;
import play.libs.Files.TemporaryFile;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import services.DataRetrieveService;
import services.ReportingService;
import validator.UploadValidator;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Map;


@Singleton
public class ReportController extends Controller {

    private static final String LISTINGS_CSV = "listings.csv";
    private static final String CONTACTS_CSV = "contacts.csv";

    private final DataRetrieveService dataRetrieveService;
    private final ReportingService reportingService;
    private final UploadValidator uploadValidator;

    @Inject
    public ReportController(DataRetrieveService dataRetrieveService,
                            ReportingService reportingService,
                            UploadValidator uploadValidator) {
        this.dataRetrieveService = dataRetrieveService;
        this.reportingService = reportingService;
        this.uploadValidator = uploadValidator;
    }

    public Result renderReport() {
        List<Listing> listingList = dataRetrieveService.retrieveListingData(LISTINGS_CSV);
        List<Contact> contactList = dataRetrieveService.retrieveContactData(CONTACTS_CSV);
        return executeAggregationsAndRespond(listingList, contactList, null);
    }

    public Result upload(Http.Request request) {
        try{
            Http.MultipartFormData<TemporaryFile> body = request.body().asMultipartFormData();
            Http.MultipartFormData.FilePart<TemporaryFile> listingsFile = body.getFile("listings");
            Http.MultipartFormData.FilePart<TemporaryFile> contactsFile = body.getFile("contacts");
            uploadValidator.validateListingsFile(listingsFile);
            uploadValidator.validateContactsFile(contactsFile);

            Path listingsPath = writeFileAndGetPath(listingsFile, "listings");
            Path contactsPath = writeFileAndGetPath(contactsFile, "contacts");

            List<Listing> listingList = dataRetrieveService.retrieveListingData(listingsPath.toFile());
            List<Contact> contactList = dataRetrieveService.retrieveContactData(contactsPath.toFile());

            return executeAggregationsAndRespond(listingList, contactList, null);
        } catch (Exception e){
            e.printStackTrace();
            return executeAggregationsAndRespond(Collections.emptyList(), Collections.emptyList(), e.getMessage());
//            return badRequest().flashing("error", e.getMessage());
        }
    }

    private Result executeAggregationsAndRespond(List<Listing> listingList, List<Contact> contactList, String message) {
        Map<String, Double> averageSellingPriceBySellerType = reportingService.calculateAverageSellingPriceBySellerType(listingList);
        Map<String, Long> percentageOfCarsByMake = reportingService.calculatePercentageOfCarsByMake(listingList);
        Double averageTop30Percentage = reportingService.calculateAveragePriceOfMostContacted(listingList, contactList);
        List<ListingByMount> listingByMounts = reportingService.calculateListingIdContactCountByMonth(listingList, contactList);
        return ok(views.html.index.render(message, averageSellingPriceBySellerType, percentageOfCarsByMake, averageTop30Percentage, listingByMounts));
    }

    private Path writeFileAndGetPath(Http.MultipartFormData.FilePart<TemporaryFile> listings, String filename) {
        return listings.getRef().copyTo(Paths.get("files/" + filename + ".csv"), true);
    }
}
