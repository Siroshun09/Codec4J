package dev.siroshun.codec4j.api.file;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;

public final class DefaultOpenOptions {

    @Contract(value = " -> new", pure = true)
    public static @NotNull OpenOption @NotNull [] fileOpenOptions() {
        return new OpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING};
    }

    private DefaultOpenOptions() {
        throw new UnsupportedOperationException();
    }
}
