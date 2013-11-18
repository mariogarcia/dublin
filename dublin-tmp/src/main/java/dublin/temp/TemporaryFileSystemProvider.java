/*
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dublin.temp;

import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.net.URI;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
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
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.spi.FileSystemProvider;
import java.util.Map;
import java.util.Set;

/**
 * @author marioggar
 *
 */
public class TemporaryFileSystemProvider extends FileSystemProvider {

    static final String SCHEME = "tmp";
    static FileSystem NON_THREAD_SAFE_FILE_SYSTEM;

	/* (non-Javadoc)
	 * @see java.nio.file.spi.FileSystemProvider#getScheme()
	 */
	@Override
	public String getScheme() {
		return SCHEME;
	}

	/* (non-Javadoc)
	 * @see java.nio.file.spi.FileSystemProvider#newFileSystem(java.net.URI, java.util.Map)
	 */
	@Override
	public FileSystem newFileSystem(URI uri, Map<String, ?> env) throws IOException {

        String passedScheme = uri.getScheme();

        if(!passedScheme.equals(SCHEME)) {
            throw new IOException("Can't create a temporal file system from the given URI: " + passedScheme);
        }

        if (NON_THREAD_SAFE_FILE_SYSTEM == null) {
            NON_THREAD_SAFE_FILE_SYSTEM = new TemporaryFileSystem(this);
        }

        return NON_THREAD_SAFE_FILE_SYSTEM;

	}

	/* (non-Javadoc)
	 * @see java.nio.file.spi.FileSystemProvider#getFileSystem(java.net.URI)
	 */
	@Override
	public FileSystem getFileSystem(URI uri) {

		FileSystem fileSystem = null;

        try {

            fileSystem = newFileSystem(uri, null);

        } catch(IOException ex) {
            ex.printStackTrace();
        }

        return fileSystem;

	}

	/* (non-Javadoc)
	 * @see java.nio.file.spi.FileSystemProvider#getPath(java.net.URI)
	 */
	@Override
	public Path getPath(URI uri) {
		return new TemporaryPath(uri, NON_THREAD_SAFE_FILE_SYSTEM);
	}

	@Override
	public SeekableByteChannel newByteChannel(Path path,
			Set<? extends OpenOption> options, FileAttribute<?>... attrs)
			throws IOException {

        boolean isReadable = options.contains(StandardOpenOption.READ);
        boolean isWriteable= options.contains(StandardOpenOption.WRITE);
        File file = path.toFile();

        if (isReadable && isWriteable) {
            return new RandomAccessFile(file,"rw").getChannel();
        }

        if (isReadable) {
            return new FileInputStream(file).getChannel();
        }

        if (isWriteable) {
            return new FileOutputStream(file).getChannel();
        }

        throw new IOException("File is neither readable nor writeable");

	}

	/* (non-Javadoc)
	 * @see java.nio.file.spi.FileSystemProvider#newDirectoryStream(java.nio.file.Path, java.nio.file.DirectoryStream.Filter)
	 */
	@Override
	public DirectoryStream<Path> newDirectoryStream(Path dir,
			Filter<? super Path> filter) throws IOException {
        return Files.newDirectoryStream(dir, filter);
	}

	/* (non-Javadoc)
	 * @see java.nio.file.spi.FileSystemProvider#createDirectory(java.nio.file.Path, java.nio.file.attribute.FileAttribute[])
	 */
	@Override
	public void createDirectory(Path dir, FileAttribute<?>... attrs)
			throws IOException {
        Files.createDirectory(dir, attrs);
	}

	/* (non-Javadoc)
	 * @see java.nio.file.spi.FileSystemProvider#delete(java.nio.file.Path)
	 */
	@Override
	public void delete(Path path) throws IOException {
        Files.delete(path);
	}

	/* (non-Javadoc)
	 * @see java.nio.file.spi.FileSystemProvider#copy(java.nio.file.Path, java.nio.file.Path, java.nio.file.CopyOption[])
	 */
	@Override
	public void copy(Path source, Path target, CopyOption... options)
			throws IOException {
        Files.copy(source, target, options);
	}

	/* (non-Javadoc)
	 * @see java.nio.file.spi.FileSystemProvider#move(java.nio.file.Path, java.nio.file.Path, java.nio.file.CopyOption[])
	 */
	@Override
	public void move(Path source, Path target, CopyOption... options)
			throws IOException {
        Files.move(source, target, options);
	}

	/* (non-Javadoc)
	 * @see java.nio.file.spi.FileSystemProvider#isSameFile(java.nio.file.Path, java.nio.file.Path)
	 */
	@Override
	public boolean isSameFile(Path path, Path path2) throws IOException {
        return Files.isSameFile(path, path2);
	}

	/* (non-Javadoc)
	 * @see java.nio.file.spi.FileSystemProvider#isHidden(java.nio.file.Path)
	 */
	@Override
	public boolean isHidden(Path path) throws IOException {
        return Files.isHidden(path);
	}

	/* (non-Javadoc)
	 * @see java.nio.file.spi.FileSystemProvider#getFileStore(java.nio.file.Path)
	 */
	@Override
	public FileStore getFileStore(Path path) throws IOException {
        return Files.getFileStore(path);
	}

	/* (non-Javadoc)
	 * @see java.nio.file.spi.FileSystemProvider#checkAccess(java.nio.file.Path, java.nio.file.AccessMode[])
	 */
	@Override
	public void checkAccess(Path path, AccessMode... modes) throws IOException {
        throw new IOException("Not implemented yet");
	}

	/* (non-Javadoc)
	 * @see java.nio.file.spi.FileSystemProvider#getFileAttributeView(java.nio.file.Path, java.lang.Class, java.nio.file.LinkOption[])
	 */
	@Override
	public <V extends FileAttributeView> V getFileAttributeView(Path path,
			Class<V> type, LinkOption... options) {
        return Files.getFileAttributeView(path, type, options);
	}

	/* (non-Javadoc)
	 * @see java.nio.file.spi.FileSystemProvider#readAttributes(java.nio.file.Path, java.lang.Class, java.nio.file.LinkOption[])
	 */
	@Override
	public <A extends BasicFileAttributes> A readAttributes(Path path,
			Class<A> type, LinkOption... options) throws IOException {

        return (A) new TemporaryBaseFileAttributes(path.toFile());

	}

	/* (non-Javadoc)
	 * @see java.nio.file.spi.FileSystemProvider#readAttributes(java.nio.file.Path, java.lang.String, java.nio.file.LinkOption[])
	 */
	@Override
	public Map<String, Object> readAttributes(Path path, String attributes,
			LinkOption... options) throws IOException {
        throw new IOException("Not implemented yet");
	}

	/* (non-Javadoc)
	 * @see java.nio.file.spi.FileSystemProvider#setAttribute(java.nio.file.Path, java.lang.String, java.lang.Object, java.nio.file.LinkOption[])
	 */
	@Override
	public void setAttribute(Path path, String attribute, Object value,
			LinkOption... options) throws IOException {
        Files.setAttribute(path, attribute, value, options);
	}

}
