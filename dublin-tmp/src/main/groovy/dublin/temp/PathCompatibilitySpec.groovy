package dublin.temp

import java.nio.file.Paths
import java.nio.file.FileSystem
import java.nio.file.FileSystems

import spock.lang.Specification

class PathCompatibilitySpec extends Specification {

    def pathType
    def scheme

    def 'Checking whether a given path is absolute or not [absolutePath]'() {
        expect: 'All expectations succeed'
            absolutePath.isAbsolute() == true
    }

    def 'Checking whether a given path is absolute or not [relativePath]'() {
        expect: 'All expectations succeed'
            relativePath.isAbsolute() == false
    }

    def 'Getting a given path file name'() {
        given: 'An absolute path'
            def rootPath = absolutePath.root
            def relative = rootPath.resolve('something.txt')
        when: 'Asking for the paths filename'
            def fileName = relative.fileName.toString()
        then: 'File name is only the last segment of the path'
            fileName == 'something.txt'
    }

    def 'Getting the parent path from a given path'() {
        given: 'An absolute path'
            def rootPath = absolutePath.root
            def relative = rootPath.resolve('something.txt')
        when: 'Asking for the relative path parent'
            def parent = relative.parent
        then: 'Parent path should be the same path as the root path'
            parent.toFile().toString() == rootPath.toFile().toString()
    }

    def 'Getting the parent path from the rootPath'() {
        given: 'An absolute path'
            def rootPath = absolutePath.root
        expect: 'It has no parent'
            rootPath.parent == null
    }

    def 'Get how many fragments are in the path'() {
        given: 'A long path'
            def path = Paths.get("${scheme}:/modules/first/something.txt")
        expect: 'Four fragments. It usually includes protocol fragment'
            path.nameCount == 4
    }

    def 'Get a given fragment literal by its position'() {
        given: 'A long path'
            def path = Paths.get("${scheme}:/modules/first/something.txt")
        expect: 'To be first'
            path.getName(2).toString() == 'first'
    }

    def 'Get given subpath by passing start and end indexes'() {
        given: 'A long path'
            def path = Paths.get("${scheme}:/modules/first/something.txt")
        expect: 'To be first'
            path.subpath(1, 3).toString() == 'modules/first'
    }

    def 'Checking whether a given path starts with a given path'() {
        given: 'A long path'
            def path = Paths.get("${scheme}:/modules/first/something.txt")
        expect: 'the paths starts with'
            path.startsWith('tmp:/modules')
    }

    def 'Checking whether a given path ends with a given path'() {
        given: 'A long path'
            def path = Paths.get("${scheme}:/modules/first/something.txt")
        expect: 'The path ends with'
            path.endsWith('first/something.txt')
    }

    def 'Resolving a given path from a previous one'() {
        given: 'A root path'
            def path = Paths.get("${scheme}:/modules")
            def expected = "${scheme}:/modules/something.txt"
        expect: 'The path ends with'
            path.resolve('something.txt').toString() == expected
            path.resolve(Paths.get('something.txt')).toString() == expected
    }

    def 'Resolving a given sibling path from a previous one'() {
        given: 'A root path'
            def path = Paths.get("${scheme}:/modules")
            def expected = "${scheme}:/something.txt"
        expect: 'The path ends with'
            path.resolveSibling('something.txt').toString() == expected
            path.resolveSibling(Paths.get('something.txt')).toString() == expected
    }

    def createFileSystem() {

        FileSystems.newFileSystem(specificationURI, [:] )

    }

    def getAbsolutePath() {

        createFileSystem().rootDirectories.first()

    }

    def getRelativePath() {

        Paths.get("${scheme}://something")

    }

    def getSpecificationURI() {

        URI.create("${scheme}://authority")

    }
}
