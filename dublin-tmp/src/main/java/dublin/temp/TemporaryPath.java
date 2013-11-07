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

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchEvent.Modifier;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Iterator;

public class TemporaryPath implements Path {

    private static final URI ROOT_URI = new File(System.getProperty("java.io.tmpdir")).toURI();

    private final URI universalPath;
    private final FileSystem temporalFileSystem;

    public TemporaryPath(URI uri, FileSystem temporalFileSystem) {
        this.universalPath = uri;
        this.temporalFileSystem = temporalFileSystem;
    }

    public TemporaryPath(FileSystem temporalFileSystem) {
        this.temporalFileSystem = temporalFileSystem;
        this.universalPath = ROOT_URI;
    }

	@Override
	public FileSystem getFileSystem() {
		return this.temporalFileSystem;
	}

	@Override
	public boolean isAbsolute() {
		return universalPath.isAbsolute();
	}

	@Override
	public Path getRoot() {
		return new TemporaryPath(ROOT_URI, this.temporalFileSystem);
	}

	@Override
	public Path getFileName() {

        int nameCount = this.getNameCount();

		return this.getName(nameCount - 1);

	}

	@Override
	public Path getParent() {

        boolean isRoot = this.compareTo(getRoot()) == 0;

        if (isRoot) {

            return null;

        }

        URI relativeURI = URI.create("tmp://../");
        URI parentUri = this.universalPath.relativize(relativeURI);
        TemporaryPath parentPath =
            new TemporaryPath(
                parentUri,
                this.temporalFileSystem
            );

		return parentPath;

	}

	@Override
	public int getNameCount() {
		return this.getTemporalPathFromFile().getNameCount();
	}

	@Override
	public Path getName(int index) {
		return this.getTemporalPathFromFile().getName(index);
	}

	@Override
	public Path subpath(int beginIndex, int endIndex) {
		return this.getTemporalPathFromFile().subpath(beginIndex, endIndex);
	}

	@Override
	public boolean startsWith(Path other) {
		return this.getTemporalPathFromFile().startsWith(other);
	}

	@Override
	public boolean startsWith(String other) {
		return this.getTemporalPathFromFile().startsWith(other);
	}

	@Override
	public boolean endsWith(Path other) {
		return this.getTemporalPathFromFile().endsWith(other);
	}

	@Override
	public boolean endsWith(String other) {
		return this.getTemporalPathFromFile().endsWith(other);
	}

	@Override
	public Path normalize() {
		return this.getTemporalPathFromFile().normalize();
	}

	@Override
	public Path resolve(Path other) {
		return this.getTemporalPathFromFile().resolve(other);
	}

	@Override
	public Path resolve(String other) {
		return this.getTemporalPathFromFile().resolve(other);
	}

	@Override
	public Path resolveSibling(Path other) {
		return this.getTemporalPathFromFile().resolveSibling(other);
	}

	@Override
	public Path resolveSibling(String other) {
		return this.getTemporalPathFromFile().resolveSibling(other);
	}

	@Override
	public Path relativize(Path other) {
		return this.getTemporalPathFromFile().relativize(other);
	}

	@Override
	public URI toUri() {
		return this.getTemporalPathFromFile().toUri();
	}

	@Override
	public Path toAbsolutePath() {
		return this.getTemporalPathFromFile().toAbsolutePath();
	}

	@Override
	public Path toRealPath(LinkOption... options) throws IOException {
		return this.getTemporalPathFromFile().toRealPath(options);
	}

	@Override
	public File toFile() {
        return new File(this.universalPath.getPath());
	}

    private Path getTemporalPathFromFile() {
        return this.toFile().toPath();
    }

	@Override
	public WatchKey register(WatchService watcher, Kind<?>[] events,
			Modifier... modifiers) throws IOException {
		return this.getTemporalPathFromFile().register(watcher, events, modifiers);
	}

	@Override
	public WatchKey register(WatchService watcher, Kind<?>... events)
			throws IOException {
		return this.getTemporalPathFromFile().register(watcher, events);
	}

	@Override
	public Iterator<Path> iterator() {
		return this.getTemporalPathFromFile().iterator();
	}

	@Override
	public int compareTo(Path other) {

        if (other == null) {
            return 1;
        }

		return this.toFile().toString().compareTo(other.toFile().toString());
	}

}
