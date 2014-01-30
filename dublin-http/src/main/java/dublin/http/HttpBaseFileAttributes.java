package dublin.http;

import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URISyntaxException;

import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

public class HttpBaseFileAttributes implements BasicFileAttributes {

    private final URLConnection urlConnection;

    public HttpBaseFileAttributes(URLConnection urlConnection) {
        this.urlConnection = urlConnection;
    }

	@Override
	public FileTime lastModifiedTime() {
		return FileTime.fromMillis(this.urlConnection.getLastModified());
	}

	@Override
	public FileTime lastAccessTime() {
        throw new UnsupportedOperationException();
	}

	@Override
	public FileTime creationTime() {
        throw new UnsupportedOperationException();
	}

	@Override
	public boolean isRegularFile() {
		return true;
	}

	@Override
	public boolean isDirectory() {
		return false;
	}

	@Override
	public boolean isSymbolicLink() {
		return false;
	}

	@Override
	public boolean isOther() {
		return false;
	}

	@Override
	public long size() {
		return this.urlConnection.getContentLengthLong();
	}

	@Override
	public Object fileKey() {

		URI internalURI = null;

        try {

            internalURI = this.urlConnection.getURL().toURI();

        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }

        return internalURI;
	}

}
