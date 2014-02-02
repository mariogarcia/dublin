/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dublin.tck

import static dublin.core.Dublin.from

import java.nio.file.Paths
import java.nio.file.Files
import java.nio.file.LinkOption
import java.nio.file.FileSystem
import java.nio.file.FileSystems

import spock.lang.Specification
import org.codehaus.groovy.runtime.InvokerHelper

class PathCompatibilitySpec extends Specification {

    def pathType
    def scheme

    def 'Building a new path only with the file system'() {
        when: 'Building a path with passing it as parameter'
            def testingPath = getAbsolutePath()
        then: 'We should be able to ask whether it exists or not'
            Files.exists(testingPath, LinkOption.NOFOLLOW_LINKS)
    }

    def 'Building a new path only with the file system and the URI'() {
        given: 'The file system'
            def fileSystem = createFileSystem()
        when: 'Building a path with passing a URI and the file system as parameter'
            def args = [specificationURI, fileSystem] as Object[]
            def testingPath = InvokerHelper.invokeConstructorOf(pathType, args)
        then: 'We should be able to ask whether it exists or not'
            Files.exists(testingPath, LinkOption.NOFOLLOW_LINKS)
    }

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
            path.startsWith("$scheme:/modules")
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
        return FileSystems.getFileSystem(specificationURI)
    }

    def getAbsolutePath() {
        return from(specificationURI)
    }

    def getRelativePath() {
        Paths.get("something")
    }

}
