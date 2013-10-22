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
package dublin.temp

import static org.junit.Assert.assertFalse
import static java.nio.file.LinkOption.NOFOLLOW_LINKS

import spock.lang.Specification

import java.nio.file.Path
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.FileSystem
import java.nio.file.FileSystems

/**
 * This class checks {@link dublin.temporary.TemporaryFileSystem} functionality
 *
 * @author marioggar
 * @since 0.1
 *
 */
class TemporaryFileSystemSpec extends Specification {

    def "Is temporary file system read-only"() {
        when: "A remote temporary file system"
            FileSystem temporaryFileSystem = createTemporaryFileSystem()
        then: "The temporal file system is not readonly"
            temporaryFileSystem.isReadOnly() == false
    }

    def "Copying a temporary file to the local file system"() {
        given: "A remote temporary file system"
            FileSystem temporaryFileSystem = createTemporaryFileSystem()
        and: "Both remote an local files"
            Long timestamp = generateNewTimestamp()
            Path sourcePath = temporaryFileSystem.getPath("source${timestamp}.txt")
            Path destinationPath = Paths.get("/tmp/destination${timestamp}.txt")
        and: "Creating the source file"
            sourcePath.toFile().createNewFile()
        and: "Writing some text into it"
            sourcePath.toFile() << "remote content"
        when: "Trying to copy the file to a local file"
            Files.copy(sourcePath, destinationPath)
        then: "We should be able to check that the local file's been created successfully"
            destinationPath.toFile().exists()
        and: "That the file has the proper content"
            destinationPath.toFile().text.contains "remote content"
    }

    def "Deleting a temporary file from the local file system"() {
        given: "A remote temporary file system"
            FileSystem temporaryFileSystem = createTemporaryFileSystem()
        and: "Both remote an local files"
            Long timestamp = generateNewTimestamp()
            Path sourcePath = temporaryFileSystem.getPath("source${timestamp}.txt")
        when: "Creating the source file"
            sourcePath.toFile().createNewFile()
        and: "Writing some text into it"
            Files.delete(sourcePath)
        then: "The file should no longer exist"
            assertFalse sourcePath.toFile().exists()
    }

    def "Getting temporary filesystem root path"() {
        given: "A remote temporary file system"
            FileSystem temporaryFileSystem = createTemporaryFileSystem()
        when: "Getting root directories"
            Long timestamp = generateNewTimestamp()
            Path rootPath = temporaryFileSystem.rootDirectories.first()
        then: "Checking that root path exists and it's the expected"
            Files.exists(rootPath, NOFOLLOW_LINKS)
            rootPath.toFile() == new File(System.getProperty("java.io.tmpdir"))
    }

    def createTemporaryFileSystem() {

        URI temporaryFileURI = URI.create("tmp://authority")
        Map temporaryFileSystemProperties = [:]
        FileSystem temporaryFileSystem = FileSystems.newFileSystem(temporaryFileURI, temporaryFileSystemProperties)

        temporaryFileSystem

    }

    def generateNewTimestamp() {

        new Date().getTime()

    }

}
