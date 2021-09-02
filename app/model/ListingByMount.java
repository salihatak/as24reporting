package model;

import java.util.List;

public class ListingByMount {

    private String month;
    private List<ListingWithContactCount> listingList;

    public ListingByMount(String month, List<ListingWithContactCount> listingList) {
        this.month = month;
        this.listingList = listingList;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public List<ListingWithContactCount> getListingList() {
        return listingList;
    }

    public void setListingList(List<ListingWithContactCount> listingList) {
        this.listingList = listingList;
    }
}
