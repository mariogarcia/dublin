package dublin.core;

import java.net.URI;
import java.util.HashMap;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;


public final class Dublin {

    public static Path from(URI uri) {
        FileSystem fileSystem = FileSystems.getFileSystem(uri);
        Path path = fileSystem.getPath(uri.toString());

        return path;
    }

    public static Path from(String uri) {
        return from(URI.create(uri));
    }

}
