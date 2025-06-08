package dev.siroshun.codec4j.api.file;

import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;

final class DefaultOpenOptions {

    static final OpenOption[] READ_OPEN_OPTIONS = {StandardOpenOption.READ};
    static final OpenOption[] WRITE_OPEN_OPTIONS = {StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING};

    private DefaultOpenOptions() {
        throw new UnsupportedOperationException();
    }
}
