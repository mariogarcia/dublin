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

    private static final String COLON = ":";
    private static final String SLASH = "/";
    private static final String EMPTY = "";

    /**
     * This method resolves a given file system by the URI passed as
     * argument. It will try to resolve the file system from the
     * scheme within the URI and once the FS has been acquired then
     * the path is resolved
     *
     * @param URI uri The uri we want to resolve
     * @return the resolved path
     */
    public static Path from(final URI uri) throws IOException {

        String scheme = uri.getScheme();
        String schemeFragment = scheme + COLON;

        String root = resolveRoot(scheme, uri.getAuthority());
        String path = uri.getPath();

        if (scheme == null) {
            throw new IOException("Can't resolve FileSystem without scheme info");
        }

        URI fsURI = URI.create(schemeFragment + SLASH);
     /* In order to get the file system we only pass the scheme plus
      * the minimum required info */
        FileSystem fileSystem = FileSystems.getFileSystem(fsURI);

     /* Once we get the file system we can resolve the full path */
        Path resolvedPath = fileSystem.getPath(root).resolve(path);

        return resolvedPath;
    }

    private static String resolveRoot(String scheme, String authority) {
        return scheme + COLON + SLASH + SLASH + authority;
    }

    private static String removeFirst(String from, String toRemove) {
        return from.replaceFirst(toRemove, EMPTY);
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
    public static Path from(String uri) throws IOException {
        return from(URI.create(uri));
    }

}
