package dublin.core;

import static dublin.core.Dublin.from;

import java.nio.file.Path
import spock.lang.Unroll
import spock.lang.Specification

/**
 *
 */
class DublinSpec extends Specification {

    @Unroll
    def 'Building a path from both a String and an URI [#path]'() {
        when: 'Building both paths from different sources'
            Path simplePath = from(path)
        then: 'Both paths can be resolved properly'
            simplePath.toFile().exists()
        where:
            path << [fileURI, fileURIToString]
    }

    def 'Failing to get a filesystem because lack of scheme'() {
        when: 'Trying to get a path from an URI without scheme part'
            from('/unknown-file-system')
        then: 'To throw a IOException'
            thrown(IOException)
    }

    def 'Once you have got the first path. Can you resolve the rest ?'() {
        when: 'Building root path'
            Path simplePath = from(fileParentDirURI)
        then: 'We should be able to resolve any files from it'
            simplePath.resolve('file.txt').toFile().exists()
    }

    URI getFileParentDirURI() {
        return new File("src/test/resources/file.txt").parentFile.toURI()
    }

    URI getFileURI() {
        return new File("src/test/resources/file.txt").toURI()
    }

    String getFileURIToString() {
        return fileURI.toString()
    }

}
