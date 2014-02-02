package dublin.core;

import static dublin.core.Dublin.from;

import java.nio.file.Path
import spock.lang.Specification

/**
 *
 */
class DublinSpec extends Specification {

    def 'Building a path from both a String and an URI'() {
        when: 'Building both paths from different sources'
            String src = 'file:///'
            String file = new File('./src/test/resources/file.txt').absolutePath
            URI sourceURI = URI.create(src)
            Path simplePathWithURI = from(sourceURI).resolve(file)
            Path simplePathWithString = from(src).resolve(file)
            println simplePathWithString.toFile().absolutePath
        then: 'Both paths are the same'
            simplePathWithString.toFile().exists()
            simplePathWithURI.toFile().exists()
    }

}
