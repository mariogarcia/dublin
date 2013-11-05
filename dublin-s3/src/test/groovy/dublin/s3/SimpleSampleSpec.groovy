package dublin.s3

import spock.lang.Specification

import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.S3Object
import com.amazonaws.services.s3.model.GetObjectRequest
import com.amazonaws.services.s3.model.AmazonS3Exception

class SimpleSampleSpec extends Specification {

    /*
    def 'Get a simple file from S3'() {
        given: 'A configured client'
            def s3Client = new AmazonS3Client()
            def bucketName = 'somebucket'
            def key = 'somekey'
        when: 'Using the S3 client service'
            def requestObject = new GetObjectRequest(bucketName, key)
            def responseObject = s3Client.getObject(requestObject)
        then: 'The retrieved response should be of type S3Object'
            thrown(AmazonS3Exception)
    }
    */

}
