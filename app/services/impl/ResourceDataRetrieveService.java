package services.impl;

import exception.FileFormatException;
import exception.FileParseException;
import model.Contact;
import model.Listing;
import play.Environment;
import services.DataRetrieveService;
import validator.UploadValidator;

import javax.inject.Inject;
import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ResourceDataRetrieveService implements DataRetrieveService {

    private static final String COMMA = ",";
    private final Environment environment;
    private final UploadValidator uploadValidator;

    @Inject
    public ResourceDataRetrieveService(Environment environment, UploadValidator uploadValidator){
        this.environment = environment;
        this.uploadValidator = uploadValidator;
    }

    public List<Contact> retrieveContactData(String contactsFile) {
        InputStreamReader inputStreamReader = retrieveResourceAsInputStreamReader(contactsFile);
        return mapInputStreamToList(inputStreamReader, this::mapToContact);
    }

    public List<Listing> retrieveListingData(String listingFile) {
        InputStreamReader inputStreamReader = retrieveResourceAsInputStreamReader(listingFile);
        return mapInputStreamToList(inputStreamReader, this::mapToListing);
    }

    public List<Contact> retrieveContactData(File contactFile) throws FileNotFoundException {
        InputStreamReader inputStreamReader = retrieveFileAsInputStreamReader(contactFile);
        return mapInputStreamToList(inputStreamReader, this::mapToContact);
    }

    public List<Listing> retrieveListingData(File listingFile) throws FileNotFoundException {
        InputStreamReader inputStreamReader = retrieveFileAsInputStreamReader(listingFile);
        return mapInputStreamToList(inputStreamReader, this::mapToListing);
    }

    private <T> List<T> mapInputStreamToList(InputStreamReader inputStreamReader, Function<String, T> mapperFunction){
        List<T> parsedList;
        try (BufferedReader br = new BufferedReader(inputStreamReader)) {
            parsedList = br.lines().skip(1).map(mapperFunction).collect(Collectors.toList());
        } catch (Exception e) {
            throw new FileParseException("file parse problem", e);
        }
        return parsedList;
    }

    private Contact mapToContact(String line) {
        String[] columns = line.split(COMMA);
        uploadValidator.validateContactColumn(columns);
        try {
            return new Contact(Long.parseLong(columns[0]), new Date(Long.parseLong(columns[1])));
        } catch (NumberFormatException e) {
            throw new FileFormatException("contacts file format problem", e);
        }
    }

    private Listing mapToListing(String line) {
        String[] columns = line.split(COMMA);
        uploadValidator.validateListingColumn(columns);
        try {
            return new Listing(Long.parseLong(columns[0]),
                    removeQuotes(columns[1]),
                    Double.parseDouble(columns[2]),
                    Long.parseLong(columns[3]),
                    removeQuotes(columns[4]));
        } catch (NumberFormatException e) {
            throw new FileFormatException("listings file format problem", e);
        }
    }

    private InputStreamReader retrieveResourceAsInputStreamReader(String fileName) {
        InputStream inputStream = environment.resourceAsStream(fileName);
        return new InputStreamReader(inputStream);
    }

    private InputStreamReader retrieveFileAsInputStreamReader(File file) throws FileNotFoundException {
        InputStream inputStream = new FileInputStream(file);
        return new InputStreamReader(inputStream);
    }

    private String removeQuotes(String column) {
        if(!column.isEmpty()){
            return column.replaceAll("\"","");
        }
        return column;
    }
}
