package dublin.http

import java.net.URI;

import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.Files

import dublin.tck.PathCompatibilitySpec

class HttpPathSpec extends PathCompatibilitySpec {

    def setup() {
        pathType = HttpPath
        scheme = 'http'
    }

    def 'Get a given resource from an URL'() {
        given: 'A path'
            HttpFileSystem httpFileSystem = createFileSystem()
            Path google = new HttpPath(new URI('http://www.google.com'), httpFileSystem)
            Path tmpFile =
                Paths.get(
                    Files.createTempDirectory('dublin').toString(),
                    Paths.get('google.html').toString()
                )
        when: 'Copying an online resource to a local file'
            Files.copy(google, tmpFile)
        then: 'We should have copied the resource from a remote url'
            tmpFile.toFile().exists()
    }

    // def 'Copying from http to tmp'() {
    //     given: 'A path'
    //         Path google = from('http://www.google.com').to('tmp://dublin/google.html')
    //     expect: 'We should have copied the resource from a remote url'
    //         google.toFile().exists()
    // }

}
