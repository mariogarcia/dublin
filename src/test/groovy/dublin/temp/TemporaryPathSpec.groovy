package dublin.temp

import java.nio.file.Paths
import java.nio.file.Files
import java.nio.file.FileSystem
import java.nio.file.FileSystems
import java.nio.file.LinkOption

import spock.lang.Unroll
import spock.lang.Specification

class TemporaryPathSpec extends Specification {

    def 'Building a new temporary path only with the file system'() {
        given: 'The temporary file system'
            def temporaryFileSystem = createTemporaryFileSystem()
        when: 'Building a path with passing it as parameter'
            def temporaryPath = new TemporaryPath(temporaryFileSystem)
        then: 'We should be able to ask whether it exists or not'
            Files.exists(temporaryPath, LinkOption.NOFOLLOW_LINKS)
    }

    def 'Building a new temporary path only with the file system and the URI'() {
        given: 'The temporary file system'
            def temporaryFileSystem = createTemporaryFileSystem()
            def explicitURI = new File(System.getProperty('java.io.tmpdir')).toURI()
        when: 'Building a path with passing a URI and the file system as parameter'
            def temporaryPath = new TemporaryPath(explicitURI, temporaryFileSystem)
        then: 'We should be able to ask whether it exists or not'
            Files.exists(temporaryPath, LinkOption.NOFOLLOW_LINKS)
    }

    @Unroll
    def 'Checking whether a given path is absolute or not [absolute:#is_absolute]'() {
        expect: 'All expectations succeed'
            sample_path.isAbsolute() == is_absolute
        where: 'Possible paths are'
            sample_path  | is_absolute
            absolutePath | true
            relativePath | false
    }

    def createTemporaryFileSystem() {

        URI temporaryFileURI = URI.create('tmp://authority')
        Map temporaryFileSystemProperties = [:]
        FileSystem temporaryFileSystem = FileSystems.newFileSystem(temporaryFileURI, temporaryFileSystemProperties)

        temporaryFileSystem

    }

    def getAbsolutePath() {

        createTemporaryFileSystem().rootDirectories.first()

    }

    def getRelativePath() {

        Paths.get('tmp://something')

    }

}
