package dublin.http;

import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.PathMatcher;
import java.nio.file.WatchService;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.nio.file.spi.FileSystemProvider;
import java.util.Set;
import java.util.ArrayList;
import java.net.URI;
import java.net.URISyntaxException;

public class HttpFileSystem extends FileSystem {

    private final HttpFileSystemProvider provider;

    public HttpFileSystem(HttpFileSystemProvider provider) {
        this.provider = provider;
    }

	@Override
	public FileSystemProvider provider() {
		return this.provider;
	}

	@Override
	public void close() throws IOException {
        throw new UnsupportedOperationException();
	}

	@Override
	public boolean isOpen() {
		return true;
	}

	@Override
	public boolean isReadOnly() {
		return true;
	}

	@Override
	public String getSeparator() {
		return "/";
	}

	@Override
	public Iterable<Path> getRootDirectories() {
		return new ArrayList<Path>();
	}

	@Override
	public Iterable<FileStore> getFileStores() {
		return new ArrayList<FileStore>();
	}

	@Override
	public Set<String> supportedFileAttributeViews() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Path getPath(String first, String... more) {
        return new HttpPath(createURIFrom(first, more), this);
	}

    private URI createURIFrom(String host, String... paths) {

        URI resolvedURI = null;

        try {

            resolvedURI = new URI(Paths.get(host, paths).toString());

        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }

        return resolvedURI;
    }

	@Override
	public PathMatcher getPathMatcher(String syntaxAndPattern) {
        throw new UnsupportedOperationException();
	}

	@Override
	public UserPrincipalLookupService getUserPrincipalLookupService() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WatchService newWatchService() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
