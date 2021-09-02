package validator;

import exception.FileFormatException;
import exception.FileNotExistException;
import org.junit.Before;
import org.junit.Test;
import play.libs.Files.TemporaryFile;
import play.mvc.Http.MultipartFormData.FilePart;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.mock;

public class UploadValidatorTest {

    private UploadValidator uploadValidator;

    @Before
    public void setUp() {
        uploadValidator = new UploadValidator();
    }

    @Test
    public void should_validate_listings_file() {
        //given
        TemporaryFile temporaryFile = mock(TemporaryFile.class);
        FilePart<TemporaryFile> listings = new FilePart<>("key", "filename", "text/csv", temporaryFile);

        //when
        Throwable throwable = catchThrowable(() -> uploadValidator.validateListingsFile(listings));

        //then
        assertThat(throwable).isNull();
    }

    @Test
    public void should_validate_contacts_file() {
        //given
        TemporaryFile temporaryFile = mock(TemporaryFile.class);
        FilePart<TemporaryFile> contacts = new FilePart<>("key", "filename", "text/csv", temporaryFile);

        //when
        Throwable throwable = catchThrowable(() -> uploadValidator.validateContactsFile(contacts));

        //then
        assertThat(throwable).isNull();
    }

    @Test
    public void should_validate_contact_column() {
        //given
        String[] columns = new String[]{"1000", "1586674958000"};

        //when
        Throwable throwable = catchThrowable(() -> uploadValidator.validateContactColumn(columns));

        //then
        assertThat(throwable).isNull();
    }

    @Test
    public void should_validate_listing_column() {
        //given
        String[] columns = new String[]{"1000", "\"make\"", "1000", "1000", "\"sellerType\""};

        //when
        Throwable throwable = catchThrowable(() -> uploadValidator.validateListingColumn(columns));

        //then
        assertThat(throwable).isNull();
    }

    @Test
    public void should_throw_exception_while_validate_listings_file_existence() {
        //given
        FilePart<TemporaryFile> listings = null;

        //when
        Throwable throwable = catchThrowable(() -> uploadValidator.validateListingsFile(listings));

        //then
        assertThat(throwable).isNotNull()
                .isInstanceOf(FileNotExistException.class);
    }

    @Test
    public void should_throw_exception_while_validate_contacts_file_existence() {
        //given
        FilePart<TemporaryFile> contacts = null;

        //when
        Throwable throwable = catchThrowable(() -> uploadValidator.validateContactsFile(contacts));

        //then
        assertThat(throwable).isNotNull()
                .isInstanceOf(FileNotExistException.class);
    }

    @Test
    public void should_throw_exception_while_validate_listings_file_content_type() {
        //given
        TemporaryFile temporaryFile = mock(TemporaryFile.class);
        FilePart<TemporaryFile> listings = new FilePart<>("key", "filename", "application/pdf", temporaryFile);

        //when
        Throwable throwable = catchThrowable(() -> uploadValidator.validateListingsFile(listings));

        //then
        assertThat(throwable).isNotNull()
                .isInstanceOf(FileFormatException.class);
    }

    @Test
    public void should_throw_exception_while_validate_contacts_file_content_type() {
        //given
        TemporaryFile temporaryFile = mock(TemporaryFile.class);
        FilePart<TemporaryFile> contacts = new FilePart<>("key", "filename", "application/pdf", temporaryFile);

        //when
        Throwable throwable = catchThrowable(() -> uploadValidator.validateContactsFile(contacts));

        //then
        assertThat(throwable).isNotNull()
                .isInstanceOf(FileFormatException.class);
    }

    @Test
    public void should_throw_exception_while_validate_contact_column_not_correct() {
        //given
        String[] columns = new String[]{"1000"};

        //when
        Throwable throwable = catchThrowable(() -> uploadValidator.validateContactColumn(columns));

        //then
        assertThat(throwable).isNotNull()
                .isInstanceOf(FileFormatException.class);
    }

    @Test
    public void should_throw_exception_while_validate_listing_column_not_correct() {
        //given
        String[] columns = new String[]{"1000", "\"make\"", "1000", "1000"};

        //when
        Throwable throwable = catchThrowable(() -> uploadValidator.validateListingColumn(columns));

        //then
        assertThat(throwable).isNotNull()
                .isInstanceOf(FileFormatException.class);
    }

    @Test
    public void should_throw_exception_while_validate_listing_column_make_not_string() {
        //given
        String[] columns = new String[]{"1000", "make", "1000", "1000", "\"sellerType\""};

        //when
        Throwable throwable = catchThrowable(() -> uploadValidator.validateListingColumn(columns));

        //then
        assertThat(throwable).isNotNull()
                .isInstanceOf(FileFormatException.class);
    }

    @Test
    public void should_throw_exception_while_validate_listing_column_seller_type_not_string() {
        //given
        String[] columns = new String[]{"1000", "\"make\"", "1000", "1000", "sellerType"};

        //when
        Throwable throwable = catchThrowable(() -> uploadValidator.validateListingColumn(columns));

        //then
        assertThat(throwable).isNotNull()
                .isInstanceOf(FileFormatException.class);
    }
}