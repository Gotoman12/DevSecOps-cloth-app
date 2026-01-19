pipeline{
    agent any
    tools{
        jdk "java-17"
        maven "maven"
    }
    stages{
        stage("Git-Checkout"){
            steps{
                git url:"https://github.com/Gotoman12/DevSecOps-cloth-app.git", branch:"master"
            }
        }
        stage("Build"){
            steps{
                sh 'mvn build'
            }
        }
        stage("Test"){
            steps{
                sh 'mvn test'
            }
        }
        stage("Package"){
            steps{
                sh 'mvn clean package'
            }
        }
        stage("Unit Testing"){
            steps{
                sh 'mvn jacoco:report'
            }
        }
    }
}