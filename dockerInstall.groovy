def call(){
    sh """
        echo "Installing docker"
        apt install docker.io -y 
    """

}