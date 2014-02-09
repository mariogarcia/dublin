package dublin.core;

import static dublin.core.Dublin.from;

import spock.lang.Unroll
import spock.lang.Specification

import org.apache.commons.vfs2.FileObject

/**
 *
 */
class DublinSpec extends Specification {

    def 'Building mounting points on Dublin and copying a local file into it'() {
        given: 'Two different FS'
            def localFileSystem = at(fileParentDirURI).build()
            def dublinFileSystem=
                at("dublin://hal/").
                    mount('tmp://simple/', on("/t/")).
                    mount('ram://simple/', on("/r/")).
                build()
        when: 'Copying a given local file to dublin mappings'
            FileObject dublinFileObject = dublinFileSystem.resolveFile('/t/file.txt')
            FileObject localFileObject = localFileSystem.resolveFile('file.txt').

            localFileObject.moveTo(dublinFileObject)
        then: 'Both paths can be resolved properly'
            dublinFileObject.exists()
            localFileObject.exists()
    }

    def 'Failing to get a filesystem because lack of scheme'() {
        when: 'Trying to get a path from an URI without scheme part'
            at('/unknown-file -system')
        then: 'To throw a IOException'
            thrown(IOException)
    }

    def 'Once you have got the first path. Can you resolve the rest ?'() {
        when: 'Building root path'
            FileObject simplePath = at(fileParentDirURI)
        then: 'We should be able to resolve any files from it'
            simplePath.resolveFile('file.txt').exists()
    }

    URI getFileParentDirURI() {
        return new File("src/test/resources/file.txt").parentFile.toURI()
    }

}
