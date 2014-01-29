package dublin.http

import java.nio.file.Files
import java.nio.file.Paths
import dublin.tck.PathCompatibilitySpec

class HttpPathSpec extends PathCompatibilitySpec {

    def setup() {
        pathType = HttpPath
        scheme = 'http'
    }

    def 'Get a given resource from an URL'() {
        given: 'A path'
            Path google = Paths.get('http://www.google.com')
            Path tmpFile = Files.createTempFile('site_','.html')
        when: 'Copying an online resource to a local file'
            Files.copy(google, tmpFile)
        then: 'We should have copied the resource from a remote url'
            tmpFile.toFile().exists()
    }
}
