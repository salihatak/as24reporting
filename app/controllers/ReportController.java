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

        Map<String, Double> averageSellingPriceBySellerType = reportingService.calculateAverageSellingPriceBySellerType(listingList);
        Map<String, Long> percentageOfCarsByMake = reportingService.calculatePercentageOfCarsByMake(listingList);
        Double averageTop30Percentage = reportingService.calculateAveragePriceOfMostContacted(listingList, contactList);
        List<ListingByMount> listingByMounts = reportingService.calculateListingIdContactCountByMonth(listingList, contactList);

        return ok(views.html.index.render(averageSellingPriceBySellerType, percentageOfCarsByMake,averageTop30Percentage, listingByMounts));
    }

    public Result upload(Http.Request request) {
        Http.MultipartFormData<TemporaryFile> body = request.body().asMultipartFormData();
        Http.MultipartFormData.FilePart<TemporaryFile> listings = body.getFile("listings");
        Http.MultipartFormData.FilePart<TemporaryFile> contacts = body.getFile("contacts");
        if(!uploadValidator.validateListingsFile(listings) || !uploadValidator.validateContactsFile(contacts)) {
            return badRequest().flashing("error", "Missing file");
        }

        TemporaryFile listingsRef = listings.getRef();

        TemporaryFile contactsRef = contacts.getRef();

          //  file.copyTo(Paths.get("/tmp/picture/destination.jpg"), true);
        return ok("File uploaded");
    }
}
