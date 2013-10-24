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
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.BasicFileAttributes;

public class TemporaryBaseFileAttributes implements BasicFileAttributes {

    private final File file;

    public TemporaryBaseFileAttributes(File file) {
        this.file = file;
    }

    public FileTime creationTime() {
        return FileTime.fromMillis(file.lastModified());
    }

    public Object fileKey() {
        return file.getAbsolutePath();
    }

    public boolean isDirectory() {
        return file.isDirectory();
    }

    public boolean isOther() {
        return false;
    }

    public boolean isRegularFile() {
        return !file.isDirectory();
    }

    public boolean isSymbolicLink() {
        return false;
    }

    public FileTime lastAccessTime() {
        return FileTime.fromMillis(file.lastModified());
    }

    public FileTime lastModifiedTime() {
        return FileTime.fromMillis(file.lastModified());
    }

    public long size() {
        return file.length();
    }

}
