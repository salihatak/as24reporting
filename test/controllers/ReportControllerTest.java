package controllers;

import akka.stream.IOResult;
import akka.stream.Materializer;
import akka.stream.javadsl.FileIO;
import akka.stream.javadsl.Source;
import akka.util.ByteString;
import org.junit.Test;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.libs.Files.TemporaryFileCreator;
import play.mvc.Http;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import play.test.WithApplication;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletionStage;

import static java.nio.file.Files.createTempFile;
import static java.nio.file.Files.write;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.*;

public class ReportControllerTest extends WithApplication {

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder().build();
    }

    @Test
    public void should_render_report(){
        //given
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/");

        //when
        Result result = route(app, request);

        //then
        assertEquals(OK, result.status());
        assertEquals("text/html", result.contentType().get());
        assertEquals("utf-8", result.charset().get());
        assertTrue(contentAsString(result).contains("Data Reports"));
    }

    @Test
    public void should_upload_file() throws IOException {
        //given
        Application app = fakeApplication();
        TemporaryFileCreator temporaryFileCreator = app.injector().instanceOf(TemporaryFileCreator.class);
        Materializer materializer = app.injector().instanceOf(Materializer.class);

        Path listingTempFile = createTempFile(null, "listingTempFile");
        Path contactTempFile = createTempFile(null, "contactTempFile");
        write(listingTempFile, "\"id\",\"make\",\"price\",\"mileage\",\"seller_type\"\n1000,\"Audi\",49717,6500,\"private\"\n1001,\"Mazda\",22031,7000,\"private\"\n1002,\"BWM\",17742,6000,\"dealer\"".getBytes("utf-8"));
        write(contactTempFile, "\"listing_id\",\"contact_date\"\n1244,1592498493000\n1085,1582474057000\n1288,1579365755000".getBytes("utf-8"));

        Source<ByteString, CompletionStage<IOResult>> listingSource = FileIO.fromPath(listingTempFile);
        Source<ByteString, CompletionStage<IOResult>> contactSource = FileIO.fromPath(contactTempFile);
        FilePart<Source<ByteString, ?>> listingFilePart = new FilePart<>("listings", "filename", "text/csv", listingSource);
        FilePart<Source<ByteString, ?>> contactsFilePart = new FilePart<>("contacts", "filename", "text/csv", contactSource);

        //when
        final Result[] result = new Result[1];
        running(app, () -> {
            Http.RequestBuilder request = fakeRequest()
                    .method(POST)
                    .bodyRaw(List.of(listingFilePart,contactsFilePart), temporaryFileCreator, materializer)
                    .uri("/");

            result[0] = route(app, request);
        });

        //then
        String actual = contentAsString(result[0]);
        assertTrue(actual.contains("€ 17,742,-"));
        assertTrue(actual.contains("€ 35,874,-"));
        assertTrue(actual.contains("33%"));
    }
}