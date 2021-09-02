package exception;

import java.text.MessageFormat;

public class FileParseException extends RuntimeException {
    public FileParseException(String message, Throwable e) {
        super(message, e);
    }

    public FileParseException(Throwable e, String message, Object... arguments) {
        super(MessageFormat.format(message, arguments), e);
    }
}
