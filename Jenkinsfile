def deployToKubernetes(env, podLabel) {
    // the yaml file you can declare in the parameters also, or use the env variable to decide which yaml file to use for different envs
    if(!(env && podLabel)) {
        errMsg = "The env and podLabel parameters are required"
        echo "$errMsg"
        throw new RuntimeException(errMsg)
    }
    def contexts = ['dev':'hsbs-dev', 'qa':'hsbs-qa', 'prod':'hsbs-prod']
    def cxt = contexts[env]
    if(!cxt) {
        msg = "Wrong the env parameter: -> $env"
        throw new RuntimeException(msg)
    }
    def title = "Deploy to the Kubernetes: ${env.toUpperCase()} of ${cxt.toUpperCase()}".center(80,"=")
    echo "${title}"
    // kubectl create secret generic app --from-file=application.yml -n $env --dry-run=client -o yaml | kubectl apply -f -
    sh """
        sed -i "s/${IMAGE_NAME}:latest/${IMAGE_FULL_NAME}/g" ${yamFile}
        sed -i "s/__APP_CFG_DEFINED/$(base64 -w 0 ${APP_CFG_FILE})/g" ${yamFile}
        cat ${yamFile}
        kubectl config use-context ${cxt}
        kubectl apply -f ${yamFile} -n ${env}
        sleep 1
        kubectl wait pod/`kubectl get pods -n ${env} -l ${podLabel} --no-headers -o custom-columns='name:metadata.name' --sort-by=.metadata.creationTimestamp | tail -n 1` --for=condition=ready --timeout=120s -n ${env}
        kubectl get all,ep,ingress -n ${env} -l ${podLabel}
    """
    echo 'Successfully deployed.'
}
pipeline {
     agent any
     tools {
            jdk 'JDK21'
     }
     environment {
            IMAGE_NAME = 'web-api'
            POD_LABEL='app=web-api'
            REPOSITORY_NAME = 'web-api'
            DOCKERFILE="Dockerfile"
            yamFile = 'devops/cd/deployment'
            APP_CFG_FILE = 'src/main/resources/application'
            IMAGE_FULL_NAME = "${IMAGE_NAME}:${BRANCH_NAME}-${BUILD_NUMBER}"
            IMAGE_LATEST_NAME = "${IMAGE_NAME}:${BRANCH_NAME}-latest"
            DEPLOY_ENV = 'initial'
            PUSH_IMAGE_HOST = 'web-apirepo.int.repositories.cloud.sap'
            PUSH_IMAGE_PATH = 'web-api/web-api-migration'
            K8S_NS_CXT = ''
     }
     stages {

            stage('Decide DEPLOY_ENV') {
                 steps {
                       script {
                             switch ( BRANCH_NAME ) {
                                    case ~/(^PR-.*)/:
                                        DEPLOY_ENV = 'dev'
                                        K8S_NS_CXT = 'DEV'
                                        break
                                    case ~/(master)/:
                                          DEPLOY_ENV = 'qa'
                                          K8S_NS_CXT = 'QA'
                                          break
                                    case ~/(^v\d.*$)/:
                                          DEPLOY_ENV = 'prod'
                                          K8S_NS_CXT = 'PROD'
                                          break
                             }
                             yamFile = yamFile+"-"+DEPLOY_ENV+".yml"
                             APP_CFG_FILE = APP_CFG_FILE+"-"+DEPLOY_ENV+".yml"
                             echo DEPLOY_ENV
                             IMAGE_FULL_NAME = IMAGE_NAME+"-"+DEPLOY_ENV+":"+BRANCH_NAME+"-"+BUILD_NUMBER
                             echo IMAGE_FULL_NAME
                       }
                 }
            }

            stage('Build') {
                 steps {
                //    sh "mvn clean install -DskipTests=true -s devops/mvn/settings.xml"
                   sh "mvn clean install -DskipTests=true"
                 }
            }

            stage('Upload') {
                steps {
                      sh """
                            sudo docker login -u $DOCKER_REPO_USER -p $DOCKER_REPO_PASS ${PUSH_IMAGE_HOST}
                            sudo docker build -f ${DOCKERFILE} -t ${PUSH_IMAGE_HOST}/${PUSH_IMAGE_PATH}/${IMAGE_FULL_NAME} .
                            sudo docker push ${PUSH_IMAGE_HOST}/${PUSH_IMAGE_PATH}/${IMAGE_FULL_NAME}
                            sudo docker tag ${PUSH_IMAGE_HOST}/${PUSH_IMAGE_PATH}/${IMAGE_FULL_NAME} ${PUSH_IMAGE_HOST}/${PUSH_IMAGE_PATH}/${IMAGE_LATEST_NAME}
                            sudo docker push ${PUSH_IMAGE_HOST}/${PUSH_IMAGE_PATH}/${IMAGE_LATEST_NAME}
                      """
                      sh '''docker rmi -f $(docker images | grep web-api | awk '{print $3}')'''
                }
            }

             stage('Deploy to DEV') {
                  when {
                        expression { return DEPLOY_ENV.equalsIgnoreCase('dev') }
                    }
                  steps {
                    script {
                        deployToKubernetes(DEPLOY_ENV, POD_LABEL)
                    }
                  }
            }

            stage('Deploy to QA') {
                  when {
                        expression { return DEPLOY_ENV.equalsIgnoreCase('qa') }
                    }
                  steps {
                    script {
                        deployToKubernetes(DEPLOY_ENV, POD_LABEL)
                    }
                  }
            }

            stage('Deploy to PROD') {
                  when {
                        expression { return DEPLOY_ENV.equalsIgnoreCase('prod') }
                  }
                steps {
                    script {
                        deployToKubernetes(DEPLOY_ENV, POD_LABEL)
                    }
                }
            }
      }

      post {
         always {
             deleteDir() /* clean up our workspace */
         }
         success {
            echo 'This will run only if successful'
         }
         changed {
             echo 'This will run only if the state of the Pipeline has changed'
         }
         failure {
             emailext body: '$DEFAULT_CONTENT', recipientProviders: [developers(), requestor()], subject: '$DEFAULT_SUBJECT'
         }
     }
}
