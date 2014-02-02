package dublin.http;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
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

    public static final String SCHEME = "http";

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

        URL urlToRead = new URL(path.toString());
        HttpSeekableByteChannel channel = new HttpSeekableByteChannel(urlToRead.openConnection());

        return channel;
	}

    static class HttpSeekableByteChannel implements SeekableByteChannel {

        private ReadableByteChannel readableChannel;
        private URLConnection urlConnection;

        public HttpSeekableByteChannel(URLConnection urlConnection)  {
            this.urlConnection = urlConnection;
        }

        public long position() {
            return 0;
        }

        public SeekableByteChannel position(long newPosition) {
            return null;
        }

        public int read(ByteBuffer dst) throws IOException {
            if (readableChannel == null) {
                URL readFrom = urlConnection.getURL();
                readableChannel = Channels.newChannel(readFrom.openStream());
            }
            return this.readableChannel.read(dst);
        }

        public long size() {
            return urlConnection.getContentLengthLong();
        }

        public SeekableByteChannel truncate(long size) {
            return null;
        }

        public int write(ByteBuffer src) {
            throw new UnsupportedOperationException();
        }

        public void close() throws IOException {
            this.readableChannel.close();
        }

        public boolean isOpen() {
            return this.readableChannel.isOpen();
        }

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

        URLConnection connection = null;
        A baseAttributes = null;

        connection = path.toUri().toURL().openConnection();
        baseAttributes = (A) new HttpBaseFileAttributes(connection);

        return baseAttributes;

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
