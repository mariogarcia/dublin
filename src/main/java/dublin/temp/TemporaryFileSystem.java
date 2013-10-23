/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
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

import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.PathMatcher;
import java.nio.file.spi.FileSystemProvider;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

import dublin.core.AbstractFileSystem;

/**
 *
 * This class simulates a file system based on the current file system's temporary
 * directory
 *
 * @author marioggar
 * @since 0.1
 *
 */
public class TemporaryFileSystem extends AbstractFileSystem {

    private TemporaryFileSystemProvider provider;

    TemporaryFileSystem(TemporaryFileSystemProvider provider) {
        this.provider = provider;
    }

	/* (non-Javadoc)
	 * @see java.nio.file.FileSystem#provider()
	 */
	@Override
	public FileSystemProvider provider() {
		return this.provider;
	}

	/* (non-Javadoc)
	 * @see java.nio.file.FileSystem#close()
	 */
	@Override
	public void close() throws IOException { }

	/* (non-Javadoc)
	 * @see java.nio.file.FileSystem#isOpen()
	 */
	@Override
	public boolean isOpen() {
		return true;
	}

	/* (non-Javadoc)
	 * @see java.nio.file.FileSystem#isReadOnly()
	 */
	@Override
	public boolean isReadOnly() {
		return false;
	}

	/* (non-Javadoc)
	 * @see java.nio.file.FileSystem#getSeparator()
	 */
	@Override
	public String getSeparator() {
		return System.getProperty("file.separator");
	}

	/* (non-Javadoc)
	 * @see java.nio.file.FileSystem#getRootDirectories()
	 */
	@Override
	public Iterable<Path> getRootDirectories() {

        Path uniqueRoot = null;
        Iterable<Path> result = new ArrayList<Path>();

        try {

            uniqueRoot = new TemporaryPath(new URI(TemporaryFileSystemProvider.SCHEME), this).getRoot();
            result = Arrays.asList(uniqueRoot);

        } catch (URISyntaxException exception) {
            exception.printStackTrace();
        }

		return result;

	}

	/* (non-Javadoc)
	 * @see java.nio.file.FileSystem#getFileStores()
	 */
	@Override
	public Iterable<FileStore> getFileStores() {
		return new ArrayList<FileStore>();
	}

	/* (non-Javadoc)
	 * @see java.nio.file.FileSystem#supportedFileAttributeViews()
	 */
	@Override
	public Set<String> supportedFileAttributeViews() {
		return FileSystems.getDefault().supportedFileAttributeViews();
	}

	/* (non-Javadoc)
	 * @see java.nio.file.FileSystem#getPath(java.lang.String, java.lang.String[])
	 */
	@Override
	public Path getPath(String first, String... more) {

        Path root = new TemporaryPath(this);
        Path builtPath = Paths.get(root.toFile().getAbsolutePath(), first);

		return Paths.get(builtPath.toString(), more);

	}

	/* (non-Javadoc)
	 * @see java.nio.file.FileSystem#getPathMatcher(java.lang.String)
	 */
	@Override
	public PathMatcher getPathMatcher(String syntaxAndPattern) {
        throw new UnsupportedOperationException();
	}

}
