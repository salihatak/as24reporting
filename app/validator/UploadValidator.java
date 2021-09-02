package validator;

import exception.FileFormatException;
import exception.FileNotExistException;
import play.libs.Files;
import play.mvc.Http.MultipartFormData.FilePart;

import java.text.MessageFormat;
import java.util.Objects;

public class UploadValidator {

    private static final int LISTING_COLUMN_SIZE = 5;
    private static final int CONTACT_COLUMN_SIZE = 2;
    private static final String TEXT_CSV = "text/csv";

    public void validateListingsFile(FilePart<Files.TemporaryFile> listings) {
        validateFileExist(listings, "listings");
        validateFileContentType(listings);
    }

    public void validateContactsFile(FilePart<Files.TemporaryFile> contacts) {
        validateFileExist(contacts, "contacts");
        validateFileContentType(contacts);
    }

    public void validateListingColumn(String[] columns) {
        if(columns.length != LISTING_COLUMN_SIZE){
            throw new FileFormatException(MessageFormat.format("listings file should have {0} columns", LISTING_COLUMN_SIZE));
        }
        if(!columns[1].contains("\"")){
            throw new FileFormatException(MessageFormat.format("listing file column {0} with value {1} is not a string", 1, columns[1]));
        }
        if(!columns[4].contains("\"")){
            throw new FileFormatException(MessageFormat.format("listing file column {0} with value {1} is not a string", 4, columns[4]));
        }
    }

    public void validateContactColumn(String[] columns) {
        if(columns.length != CONTACT_COLUMN_SIZE){
            throw new FileFormatException(MessageFormat.format("contacts file should have {0} columns", CONTACT_COLUMN_SIZE));
        }
    }

    private void validateFileContentType(FilePart<Files.TemporaryFile> file) {
        if(!TEXT_CSV.equals(file.getContentType())){
            throw new FileFormatException("file type should be CSV");
        }
    }

    private void validateFileExist(FilePart<Files.TemporaryFile> file, String fileType) {
        if(Objects.isNull(file)){
            throw new FileNotExistException(MessageFormat.format("{0} file not exist", fileType));
        }
    }
}
