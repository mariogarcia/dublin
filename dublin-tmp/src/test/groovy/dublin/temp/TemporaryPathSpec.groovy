package dublin.temp

class TemporaryPathSpec extends PathCompatibilitySpec {

    def setup() {

        pathType = TemporaryPath
        scheme = 'tmp'

    }

    def 'Getting root path'() {
        given: 'A file system'
            def relative = getAbsolutePath()
            def tmpdir = new File(System.getProperty('java.io.tmpdir'))
        expect: 'The same root as if we were asking for the tmp dir'
            relative.root.toFile() == tmpdir
        and: 'Subsequents calls should return the same'
            pathType.isInstance(relative.root.root)
            relative.root.root.toFile() == tmpdir
    }

}
