def call(String imageName, String dockerfilePath = '.') {
    sh """
        echo "Building Docker image: ${imageName}"
        docker build -t ${imageName} ${dockerfilePath}
    """
}
