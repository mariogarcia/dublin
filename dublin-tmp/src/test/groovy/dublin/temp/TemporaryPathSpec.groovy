package dublin.temp

import java.nio.file.Paths
import java.nio.file.Files
import java.nio.file.FileSystem
import java.nio.file.FileSystems
import java.nio.file.LinkOption

class TemporaryPathSpec extends PathCompatibilitySpec {

    def setup() {

        pathType = TemporaryPath
        scheme = 'tmp'

    }

    def 'Getting root path'() {
        given: 'A file system'
            def fileSystem = createFileSystem()
            def relative = new TemporaryPath(fileSystem)
            def tmpdir = new File(System.getProperty('java.io.tmpdir'))
        expect: 'The same root as if we were asking for the tmp dir'
            relative.root.toFile() == tmpdir
        and: 'Subsequents calls should return the same'
            relative.root.root instanceof TemporaryPath
            relative.root.root.toFile() == tmpdir
    }

    def 'Building a new path only with the file system'() {
        given: 'The file system'
            def fileSystem = createFileSystem()
        when: 'Building a path with passing it as parameter'
            def testingPath = new TemporaryPath(fileSystem)
        then: 'We should be able to ask whether it exists or not'
            Files.exists(testingPath, LinkOption.NOFOLLOW_LINKS)
    }

    def 'Building a new path only with the file system and the URI'() {
        given: 'The file system'
            def fileSystem = createFileSystem()
        when: 'Building a path with passing a URI and the file system as parameter'
            def testingPath = new TemporaryPath(specificationURI, fileSystem).getRoot()
        then: 'We should be able to ask whether it exists or not'
            Files.exists(testingPath, LinkOption.NOFOLLOW_LINKS)
    }

}
