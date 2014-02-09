package dublin.core

import org.apache.commons.vfs2.VFS
import org.apache.commons.vfs2.FileObject


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
final class Dublin {

    /**
     * This method resolves a given file system by the URI passed as
     * argument. It will try to resolve the file system from the
     * scheme within the URI and once the FS has been acquired then
     * the path is resolved
     *
     * @param URI uri The uri we want to resolve
     * @return the resolved path
     * @throws IOException
     */
    static FileObject from(final URI uri) throws IOException {
        return from(uri.toString())
    }

    /**
     * This method resolves a given file system by the String passed as
     * argument. It will try to resolve the file system from the
     * scheme within the URI and once the FS has been acquired then
     * the path is resolved
     *
     * @param String uri The uri we want to resolve
     * @return the resolved file object
     * @throws IOException
     */
    static FileObject from(String uri) throws IOException {
        return VFS.manager.resolveFile(uri)
    }

}
