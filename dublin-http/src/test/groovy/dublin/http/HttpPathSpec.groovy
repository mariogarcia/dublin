package dublin.http

import static dublin.core.Dublin.from
import static java.nio.file.Paths.get
import static java.nio.file.Files.copy
import static java.nio.file.Files.createTempDirectory

import java.net.URI
import java.nio.file.Path
import java.nio.file.FileSystems

import dublin.tck.PathCompatibilitySpec

/**
 * Test to cover HttpPath functionality using the
 * PathCompatibilitySpec
 *
 */
class HttpPathSpec extends PathCompatibilitySpec {

    def setup() {
        pathType = HttpPath
        scheme = 'http'
    }

    def 'Get a given resource from an URL'() {
        given: 'A couple of paths for copying information to each other'
            Path google = from("http://www.google.com")
            Path tmpFile = get(
                createTempDirectory('dublin').toString(),
                get('google.html').toString()
            )
        when: 'Copying an online resource to a local file'
            copy(google, tmpFile)
        then: 'We should have copied the resource from a remote url'
            tmpFile.toFile().exists()
    }

    def getSpecificationURI() {
        URI.create("http://www.google.com")
    }
}
