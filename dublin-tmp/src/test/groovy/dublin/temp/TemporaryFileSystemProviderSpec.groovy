package dublin.temp

import java.nio.ByteBuffer
import java.nio.file.Paths
import java.nio.file.Files
import java.nio.file.FileSystems
import java.nio.file.StandardOpenOption
import java.nio.file.attribute.PosixFilePermission
import java.nio.file.attribute.PosixFilePermissions
import java.nio.file.attribute.FileAttribute

import spock.lang.Specification

class TemporaryFileSystemProviderSpec extends Specification {

    def scheme = 'tmp'

    def 'Creating a new byte channel from a given path'() {
        given: 'The provider we will be testing'
            def fileSystem = createFileSystem()
            def fileSystemProvider = fileSystem.provider
            def temporaryFile = Files.createTempFile('dublin','file')
        when: 'Asking for a seekable byte channel'
            def optionsSet = [option] as Set
            def seekableByteChannel =
                fileSystemProvider.newByteChannel(temporaryFile, optionsSet, filePermissions)
        then: 'The test has been passed'
            seekableByteChannel
        where:
            option  << [StandardOpenOption.READ, StandardOpenOption.WRITE]
    }

    def 'Creating a new byte channel for writing'() {
        given: 'The provider we will be testing'
            def fileSystem = createFileSystem()
            def fileSystemProvider = fileSystem.provider
            def temporaryFile = Files.createTempFile('dublin','file')
        when: 'Asking for a seekable byte channel'
            def optionsSet = [StandardOpenOption.WRITE] as Set
            def byteBuffer = ByteBuffer.allocate(1024)
            def seekableByteChannel =
                fileSystemProvider.newByteChannel(temporaryFile, optionsSet, filePermissions)
        and: 'Writing some data to it'
            byteBuffer.put("Some data is inside this file".getBytes('UTF-8'))
            byteBuffer.flip()
            seekableByteChannel.write(byteBuffer)
            seekableByteChannel.close()
        then: 'The test has been passed'
           temporaryFile.toFile().text == 'Some data is inside this file'

    }

    def createFileSystem() {

        FileSystems.newFileSystem(specificationURI, [:] )

    }

    FileAttribute<PosixFilePermission> getFilePermissions() {

        Set<PosixFilePermissions> perms = PosixFilePermissions.fromString("rw-------");
        FileAttribute<Set<PosixFilePermission>> attr = PosixFilePermissions.asFileAttribute(perms)

        return attr

    }

    def getAbsolutePath() {

        createFileSystem().rootDirectories.first()

    }

    def getRelativePath() {

        Paths.get("${scheme}://something")

    }

    def getSpecificationURI() {

        URI.create("${scheme}://authority")

    }

}
