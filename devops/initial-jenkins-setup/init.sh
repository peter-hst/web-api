#!/bin/bash

mkdir -p scripts data/{jenkins-data,docker-data,artifactory-data}

chmod 777 data/jenkins-data
chmod 777 data/artifactory-data

cat > plugins.txt << 'EOF'
kubernetes:latest
workflow-aggregator:latest
git:latest
docker-workflow:latest
docker-plugin:latest
blueocean:latest
pipeline-stage-view:latest
docker-commons:latest
docker-build-step:latest
credentials-binding:latest
jfrog:latest
maven-plugin:latest
gradle:latest
EOF

# 创建初始化脚本目录
mkdir -p scripts
cat > scripts/init.groovy << 'EOF'
#!/usr/bin/env groovy
import jenkins.model.*
import hudson.security.*
def instance = Jenkins.getInstance()
instance.setInstallState(InstallState.INITIAL_SETUP_COMPLETED)
def hudsonRealm = new HudsonPrivateSecurityRealm(false)
hudsonRealm.createAccount("admin", "admin@123456")
instance.setSecurityRealm(hudsonRealm)
def strategy = new FullControlOnceLoggedInAuthorizationStrategy()
strategy.setAllowAnonymousRead(false)
instance.setAuthorizationStrategy(strategy)
instance.save()
EOF

docker-compose up -d