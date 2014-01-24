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
package dublin.temp

import java.nio.file.Files
import java.nio.file.Paths
import dublin.tck.PathCompatibilitySpec

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

    def 'Copying a temporary file NIO way'() {
        given: 'A temporal source and a local destination'
            def localSystemFile = Files.createTempFile('dublin_test', 'txt')
            def temporalSystemPath = Paths.get("tmp://something.txt")
        when: 'Copying to another file in the local system'
            Files.copy(temporalSystemPath, localSystemFile)
        then: 'It should work and the destination should exist'
            localSystemFile.toFile().exists() == true
    }

}
