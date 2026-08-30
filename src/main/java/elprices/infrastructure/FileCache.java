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

        //Create the filenameema
    private Path fileFor(ElectricityArea area, LocalDate date) {
        return cacheDir.resolve("%s_%s.json".formatted(date, area.name())
        );
    }

}
