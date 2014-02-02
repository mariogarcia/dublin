package dublin.core;

import java.net.URI;
import java.util.HashMap;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;


/**
 * This class is intended to be an entry point for resolving Path instances no matter
 * the file system they have to be resolve against.
 *
 * This should be the job java.nio.file.Paths should be doing but unfortunately
 * current version only resolves paths based on the local file system.
 *
 * @author Mario Garcia
 * @since version 1.0.0
 *
 */
public final class Dublin {

    /**
     * The UnixFileSystemProvider expects just the part
     * identifying the file system. If you oass a
     * complete URI it will complain throwing an exception
     */
    private static final String JDK_FILE_PROVIDER_HACK = ":/";

    /**
     * This method resolves a given file system by the URI passed as
     * argument. It will try to resolve the file system from the
     * scheme within the URI and once the FS has been acquired then
     * the path is resolved
     *
     * @param URI uri The uri we want to resolve
     * @return the resolved path
     */
    public static Path from(URI uri) {

        String scheme = uri.getScheme();
        URI fsURI = URI.create(scheme + JDK_FILE_PROVIDER_HACK);
     /* In order to get the file system we only pass the scheme plus
      * the minimum required info */
        FileSystem fileSystem = FileSystems.getFileSystem(fsURI);

     /* Once we get the file system we can resolve the full path */
        Path path = fileSystem.getPath(uri.toString().substring(5));

        return path;
    }

    /**
     * This method resolves a given file system by the String passed as
     * argument. It will try to resolve the file system from the
     * scheme within the URI and once the FS has been acquired then
     * the path is resolved
     *
     * @param String uri The uri we want to resolve
     * @return the resolved path
     */
    public static Path from(String uri) {
        return from(URI.create(uri));
    }

}
