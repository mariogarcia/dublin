package dublin.http;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.channels.Channel;
import java.nio.channels.Channels;
import java.nio.channels.SeekableByteChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.AccessMode;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryStream;
import java.nio.file.DirectoryStream.Filter;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.spi.FileSystemProvider;
import java.util.Map;
import java.util.Set;

public class HttpFileSystemProvider extends FileSystemProvider {

    private static final String SCHEME = "http";

	@Override
	public String getScheme() {
		return SCHEME;
	}

	@Override
	public FileSystem newFileSystem(URI uri, Map<String, ?> env) throws IOException {
		return new HttpFileSystem(this);
	}

	@Override
	public FileSystem getFileSystem(URI uri) {

        FileSystem httpFileSystem = null;

        try {

            httpFileSystem = newFileSystem(uri, null);

        } catch (IOException ex) {
            ex.printStackTrace();
        }

		return httpFileSystem;

	}

	@Override
	public Path getPath(URI uri) {
		return new HttpPath(uri, getFileSystem(uri));
	}

	@Override
	public SeekableByteChannel newByteChannel(Path path, Set<? extends OpenOption> options, FileAttribute<?>... attrs) throws IOException {

        boolean isWriteable = options.contains(StandardOpenOption.WRITE);
        boolean isReadable = options.contains(StandardOpenOption.READ);

        if(isWriteable) {
            throw new IOException("Writing not implemented yet");
        }

        if (isReadable) {
            URL urlToRead = new URL(path.toString());
            ReadableByteChannel channel = Channels.newChannel(urlToRead.openStream());

            return (SeekableByteChannel) channel;
        }

        throw new IOException("File is neither readable nor writeable");
	}

	@Override
	public DirectoryStream<Path> newDirectoryStream(Path dir,
			Filter<? super Path> filter) throws IOException {
		throw new IOException("Not implemented yet");
	}

	@Override
	public void createDirectory(Path dir, FileAttribute<?>... attrs)
			throws IOException {
		throw new IOException("Not implemented yet");
	}

	@Override
	public void delete(Path path) throws IOException {
		throw new IOException("Not implemented yet");
	}

	@Override
	public void copy(Path source, Path target, CopyOption... options) throws IOException{
		throw new IOException("Not implemented yet");
	}

	@Override
	public void move(Path source, Path target, CopyOption... options)
			throws IOException {
		throw new IOException("Not implemented yet");
	}

	@Override
	public boolean isSameFile(Path path, Path path2) throws IOException {
		return new URL(path.toString()).equals(new URL(path2.toString()));
	}

	@Override
	public boolean isHidden(Path path) throws IOException {
		return false;
	}

	@Override
	public FileStore getFileStore(Path path) throws IOException {
		return null;
	}

	@Override
	public void checkAccess(Path path, AccessMode... modes) throws IOException {
		throw new IOException("Not implemented yet");
	}

	@Override
	public <V extends FileAttributeView> V getFileAttributeView(Path path,
			Class<V> type, LinkOption... options) {
        return null;
	}

	@Override
	public <A extends BasicFileAttributes> A readAttributes(Path path,
			Class<A> type, LinkOption... options) throws IOException {
		throw new IOException("Not implemented yet");
	}

	@Override
	public Map<String, Object> readAttributes(Path path, String attributes,
			LinkOption... options) throws IOException {
		throw new IOException("Not implemented yet");
	}

	@Override
	public void setAttribute(Path path, String attribute, Object value,
			LinkOption... options) throws IOException {
		throw new IOException("Not implemented yet");
	}

}
