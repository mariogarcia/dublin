package dublin.http;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchEvent.Modifier;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Arrays;
import java.util.List;
import java.util.Iterator;

public class HttpPath implements Path {

    private final URI universalPath;
    private final FileSystem httpFileSystem;

    public HttpPath(URI uri, FileSystem httpFileSystem) {
        this.universalPath = uri;
        this.httpFileSystem = httpFileSystem;
    }

	@Override
	public FileSystem getFileSystem() {
		return this.httpFileSystem;
	}

	@Override
	public boolean isAbsolute() {
		return universalPath.isAbsolute();
	}

	@Override
	public Path getRoot() {
        return getPathFromURI(getURIFromSchemeAndHost());
	}

    private URI getURIFromSchemeAndHost() {

        URI uri = null;

        try {
            uri = new URI(
                universalPath.getScheme(),
                universalPath.getHost(),
                null,
                null
            );
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }

        return uri;
    }

	@Override
	public Path getFileName() {
		return this.getName(this.getNameCount() - 1);
	}

	@Override
	public Path getParent() {
        boolean isRoot = this.compareTo(getRoot()) == 0;

        if (isRoot) {
            return null;
        }

        URI relativeURI = URI.create("http://../");
        URI parentURI = this.universalPath.relativize(relativeURI);
        HttpPath parentPath =
            new HttpPath(parentURI, this.httpFileSystem);

		return parentPath;
	}

	@Override
	public int getNameCount() {
		return getInternalPath().getNameCount();
	}

	@Override
	public Path getName(int index) {
        return this.subpath(index, index + 1);
	}

	@Override
	public Path subpath(int beginIndex, int endIndex) {
        int count = getNameCount() + 1;

        if (beginIndex < 0 || endIndex > (count + 1)) {
            throw new IllegalArgumentException("Index out of the bounds of the path");
        }

        return getInternalPath().subpath(beginIndex, endIndex);
	}

    private Path getInternalPath() {
        return Paths.get(this.universalPath.getPath().toString());
    }

    private URI getFileSystemRepresentation() {
        return URI.create(
           this.universalPath.getScheme() + "://" +
           this.universalPath.getAuthority() + "/"
        );
    }

    private URI resolveURIWithPath(String path) {
        URI resolvedURI = null;
        try {
            resolvedURI = new URI(
                universalPath.getScheme(),
                universalPath.getHost(),
                path,
                null
            );
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }

        return resolvedURI;
    }

	@Override
	public boolean startsWith(Path other) {
		return this.toString().startsWith(other.toString());
	}

	@Override
	public boolean startsWith(String other) {
		return this.toString().startsWith(other);
	}

	@Override
	public boolean endsWith(Path other) {
		return this.toString().endsWith(other.toString());
	}

	@Override
	public boolean endsWith(String other) {
		return this.toString().endsWith(other);
	}

	@Override
	public Path normalize() {
		return this.getPathFromURI(this.universalPath.normalize());
	}

	@Override
	public Path resolve(Path other) {
        return this.getPathFromURI(resolvePath(this.universalPath, other));
	}

	@Override
	public Path resolve(String other) {
        return this.resolve(Paths.get(other));
	}

    private URI resolvePath(URI uri, Path path) {
        URI fileSystemFragment =
            URI.create(
                uri.getScheme() + "://" + uri.getAuthority() + "/"
            );
        Path pathFragment = Paths.get(uri.getPath()).resolve(path);

        return fileSystemFragment.resolve(pathFragment.toString());
    }

	@Override
	public Path resolveSibling(Path other) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Path resolveSibling(String other) {
		// TODO Auto-generated method stub
		return null;
	}

    	@Override
	public Path relativize(Path other) {
		return this.getPathFromURI(this.universalPath.relativize(other.toUri()));
	}

    private Path getPathFromURI(URI uri) {
        return new HttpPath(uri, this.httpFileSystem);
    }

    public String toString() {
        return this.toUri().toString();
    }

	@Override
	public URI toUri() {
		return this.universalPath;
	}

	@Override
	public Path toAbsolutePath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Path toRealPath(LinkOption... options) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public File toFile() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WatchKey register(WatchService watcher, Kind<?>[] events,
			Modifier... modifiers) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WatchKey register(WatchService watcher, Kind<?>... events)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<Path> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int compareTo(Path other) {
        return this.toString().compareTo(other.toString());
	}

}
