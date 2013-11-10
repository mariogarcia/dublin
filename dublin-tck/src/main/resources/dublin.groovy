dublin {

    from {

        "s3-images" {

            type: 's3',
            bucket: 'images'
            username: 'username',
            password: 'password'

        }

        "s3-music" {

            type: 's3',
            bucket: 'music'
            username: 'username',
            password: 'password'

        }

    }

    to {

        "/images" {
            storage: 's3-images',
        }

        "/music" {
            storage: 's3-music'
        }

    }

}
