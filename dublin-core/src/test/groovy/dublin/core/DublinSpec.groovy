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

    URI getFileURI() {
        return new File("src/test/resources/file.txt").toURI()
    }

    String getFileURIToString() {
        return fileURI.toString()
    }


}
