package dublin.core;

import static dublin.core.D.at

import spock.lang.Unroll
import spock.lang.Specification

import org.apache.commons.vfs2.FileObject

/**
 *
 */
class DublinSpec extends Specification {

    def 'Building mounting points on Dublin and copying a local file into it'() {
        given: 'Two different FS'
            def local =
                at('file://src/test/resources/file.txt')
            def dublin =
                at("dublin://hal/") {
                    mount 'tmp://simple/' on '/tmp/'
                    mount 'ram://simple/' on '/ram/'
                }
        and: 'Establishing which files are source and destination'
            FileObject src = local.resolve('file.txt')
            FileObject dst =
                dublin.
                    resolve('/t/').
                    resolve('file.txt')
        when: 'Copying source to destination'
            src >> dst
        then: 'Both paths can should exist'
            dst.exists()
            src.exists()
    }

    def 'Failing to get a filesystem because lack of scheme'() {
        when: 'Trying to get a path from an URI without scheme part'
            at('/unknown-file -system')
        then: 'To throw a IOException'
            thrown(IOException)
    }

    def 'Once you have got the first path. Can you resolve the rest ?'() {
        when: 'Building root path'
            def testFS = at(fileParentDirURI)
        then: 'We should be able to resolve any files from it'
            testFS.resolveFile('file.txt').exists()
    }

    URI getFileParentDirURI() {
        return new File("src/test/resources/file.txt").parentFile.toURI()
    }

}
