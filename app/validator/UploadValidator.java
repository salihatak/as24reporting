package validator;

import play.libs.Files;
import play.mvc.Http.MultipartFormData.FilePart;

import java.util.Objects;

public class UploadValidator {

    private static final String TEXT_CSV = "text/csv";

    public boolean validateListingsFile(FilePart<Files.TemporaryFile> listings) {
        return validateFileExist(listings) && validateFileContentType(listings);
    }

    private boolean validateFileContentType(FilePart<Files.TemporaryFile> file) {
        return TEXT_CSV.equals(file.getContentType());
    }

    public boolean validateContactsFile(FilePart<Files.TemporaryFile> contacts) {
        return validateFileExist(contacts) && validateFileContentType(contacts);
    }

    private boolean validateFileExist(FilePart<Files.TemporaryFile> file) {
        return !Objects.isNull(file);
    }
}
