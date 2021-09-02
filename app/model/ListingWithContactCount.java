package model;

public class ListingWithContactCount extends Listing {

    private Long contactCount;

    public ListingWithContactCount(Listing listing, Long contactCount) {
        super(listing.getId(), listing.getMake(), listing.getPrice(), listing.getMileage(), listing.getSellerType());
        this.contactCount = contactCount;
    }

    public Long getContactCount() {
        return contactCount;
    }

    public void setContactCount(Long contactCount) {
        this.contactCount = contactCount;
    }
}
