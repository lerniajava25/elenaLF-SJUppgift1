package elprices.infrastructure;

import elprices.domain.ElectricityArea;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

//Disk cache for raw JSON responses
// // Each (area, date) maps to one file. If that file already exists the caller reads it instead of hitting the network again. Implemented with java.nio.file.Files} as suggested in the assignment.

public class FileCache {

    //put structure for the FileCache
    private static final DateTimeFormatter FILE_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    //where the files should be stored:
    private final Path cacheDir;

    public FileCache(Path cacheDir) {
        this.cacheDir = cacheDir;
    }

    // Return the cached JSON if present, otherwise empty.
    public Optional<String> read(ElectricityArea area, LocalDate date) {
        var file = fileFor(area, date);
        if (!Files.exists(file)) {
            return Optional.empty();
        }
        try {
            return Optional.of(Files.readString(file, StandardCharsets.UTF_8));
        } catch (IOException e) {
            // an unreadable cache file should never crash the app, so treat it as a miss and let the caller fetch new data.
            return Optional.empty();
        }
    }

      //Create the filenam
    private Path fileFor(ElectricityArea area, LocalDate date) {
        return cacheDir.resolve("%s_%s.json".formatted(date, area.name())
        );
    }

}
