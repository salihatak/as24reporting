package services.impl;

import model.Contact;
import model.Listing;
import model.ListingByMount;
import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

public class ReportingServiceImplTest {

    private ReportingServiceImpl reportingService;

    @Before
    public void setUp() {
        reportingService = new ReportingServiceImpl();
    }

    @Test
    public void should_calculate_average_selling_price_by_sellerType() {
        //given
        ArrayList<Listing> listingList = new ArrayList<>();
        listingList.add(new Listing(1L, "BMW", 20000d, 100L, "private"));
        listingList.add(new Listing(2L, "Audi", 40000d, 100L, "private"));
        listingList.add(new Listing(3L, "Audi", 40000d, 100L, "dealer"));
        listingList.add(new Listing(4L, "Audi", 40000d, 100L, "dealer"));
        listingList.add(new Listing(5L, "Audi", 50000d, 100L, "other"));

        //when
        Map<String, Double> calculationMap = reportingService.calculateAverageSellingPriceBySellerType(listingList);

        //then
        assertThat(calculationMap)
                .isNotNull()
                .hasSize(3)
                .containsKeys("private", "dealer", "other")
                .containsValues(30000d, 40000d, 50000d);
    }

    @Test
    public void should_calculate_average_price_of_most_contacted() {
        //given
        ArrayList<Listing> listingList = new ArrayList<>();
        listingList.add(new Listing(1L, "BMW", 20000d, 100L, "private"));
        listingList.add(new Listing(2L, "Audi", 40000d, 100L, "private"));
        listingList.add(new Listing(3L, "Audi", 30000d, 100L, "dealer"));
        listingList.add(new Listing(4L, "Audi", 40000d, 100L, "dealer"));
        listingList.add(new Listing(5L, "Audi", 50000d, 100L, "other"));
        listingList.add(new Listing(6L, "Audi", 50000d, 100L, "other"));
        listingList.add(new Listing(7L, "Audi", 50000d, 100L, "other"));
        listingList.add(new Listing(8L, "Audi", 50000d, 100L, "other"));
        listingList.add(new Listing(9L, "Audi", 50000d, 100L, "other"));
        listingList.add(new Listing(10L, "Audi", 50000d, 100L, "other"));

        Date contactDate = new Date();
        List<Contact> contactList = new ArrayList<>();
        contactList.add(new Contact(1L, contactDate));
        contactList.add(new Contact(1L, contactDate));
        contactList.add(new Contact(2L, contactDate));
        contactList.add(new Contact(2L, contactDate));
        contactList.add(new Contact(3L, contactDate));
        contactList.add(new Contact(3L, contactDate));
        contactList.add(new Contact(4L, contactDate));
        //when
        Double averagePriceOfMostContacted30p = reportingService.calculateAveragePriceOfMostContacted(listingList, contactList);

        //then
        assertThat(averagePriceOfMostContacted30p)
                .isNotNull()
                .isEqualTo(30000L);
    }

    @Test
    public void should_calculate_percentage_of_cars_by_make() {
        //given
        ArrayList<Listing> listingList = new ArrayList<>();
        listingList.add(new Listing(1L, "BMW", 20000d, 100L, "private"));
        listingList.add(new Listing(2L, "BMW", 40000d, 100L, "private"));
        listingList.add(new Listing(3L, "Audi", 30000d, 100L, "dealer"));
        listingList.add(new Listing(4L, "Audi", 40000d, 100L, "dealer"));
        listingList.add(new Listing(5L, "Audi", 50000d, 100L, "other"));
        listingList.add(new Listing(6L, "Audi", 50000d, 100L, "other"));
        listingList.add(new Listing(7L, "Audi", 50000d, 100L, "other"));
        listingList.add(new Listing(8L, "Audi", 50000d, 100L, "other"));
        listingList.add(new Listing(9L, "Audi", 50000d, 100L, "other"));
        listingList.add(new Listing(10L, "Renault", 50000d, 100L, "other"));

        //when
        Map<String, Long> percentageOfCarsByMake = reportingService.calculatePercentageOfCarsByMake(listingList);

        //then
        assertThat(percentageOfCarsByMake)
                .isNotNull()
                .hasSize(3)
                .containsKeys("BMW", "Audi", "Renault")
                .containsValues(20L, 70L, 10L);
    }

    @Test
    public void should_calculate_listing_id_contact_count_by_month() throws ParseException {
        //given
        ArrayList<Listing> listingList = new ArrayList<>();
        listingList.add(new Listing(1L, "BMW", 20000d, 100L, "private"));
        listingList.add(new Listing(2L, "Renault", 40000d, 100L, "private"));
        listingList.add(new Listing(3L, "Audi", 30000d, 100L, "dealer"));
        listingList.add(new Listing(4L, "BMW", 40000d, 100L, "dealer"));
        listingList.add(new Listing(5L, "BMW", 50000d, 100L, "other"));
        listingList.add(new Listing(6L, "Audi", 50000d, 100L, "other"));
        listingList.add(new Listing(7L, "Audi", 50000d, 100L, "other"));
        listingList.add(new Listing(8L, "Audi", 50000d, 100L, "other"));
        listingList.add(new Listing(9L, "Audi", 50000d, 100L, "other"));
        listingList.add(new Listing(10L, "Renault", 50000d, 100L, "other"));

        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Date firstMonth = dateFormat.parse("15.01.2021");
        Date secondMonth = dateFormat.parse("15.02.2021");
        List<Contact> contactList = new ArrayList<>();
        contactList.add(new Contact(1L, firstMonth));
        contactList.add(new Contact(1L, firstMonth));
        contactList.add(new Contact(2L, firstMonth));
        contactList.add(new Contact(2L, firstMonth));
        contactList.add(new Contact(3L, secondMonth));
        contactList.add(new Contact(3L, secondMonth));
        contactList.add(new Contact(4L, secondMonth));

        //when
        List<ListingByMount> listingByMounts = reportingService.calculateListingIdContactCountByMonth(listingList, contactList);

        //then
        assertThat(listingByMounts)
                .isNotNull()
                .hasSize(2)
                .extractingResultOf("getMonth")
                .contains("01.2021", "02.2021");

        assertThat(listingByMounts.get(0).getListingList())
                .isNotNull()
                .extracting("id", "make", "price", "mileage", "sellerType", "contactCount")
                .contains(
                        tuple(1L, "BMW", 20000d, 100L, "private", 2L),
                        tuple(2L, "Renault", 40000d, 100L, "private", 2L)
                );

        assertThat(listingByMounts.get(1).getListingList())
                .isNotNull()
                .extracting("id", "make", "price", "mileage", "sellerType", "contactCount")
                .contains(
                        tuple(3L, "Audi", 30000d, 100L, "dealer", 2L),
                        tuple(4L, "BMW", 40000d, 100L, "dealer", 1L)
                );
    }
}