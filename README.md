# The Project 

> The real environment should not have any `Jenkinsfile` or `devops` directories
>  
>  At most one Dockfile file can exist or several build templates can be centrally managed in DevOps repositories
### The Folder description
```shell
.
|-- Dockerfile
|-- HELP.md
|-- Jenkinsfile
|-- LICENSE
|-- devops                      # Root folder of Devops team
|   |-- cd                      # how to deploy to the k8s
|   |   |-- README.md
|   |   |-- deployment-dev.yml
|   |   |-- deployment-prod.yml
|   |   `-- deployment-qa.yml
|   `-- ci                      # build template and cicd pipeline
|       |-- Dockerfile
|       `-- Jenkinsfile
|-- mvnw
|-- mvnw.cmd
|-- pom.xml
|-- src                         # Root source code folder of developer team
|   |-- main
|   |   |-- java
|   |   `-- resources           # Root configuration folder of environment
|   `-- test                    # Root testing code for QA team
|       `-- java
`-- target
    |-- classes
    |   |-- application-dev.yml
    |   |-- application-prod.yml
    |   |-- application-qa.yml
    |   |-- application.yml
    |   |-- banner.txt
    |   `-- hst
    |-- generated-sources
    |   `-- annotations
    |-- generated-test-sources
    |   `-- test-annotations
    `-- test-classes
        `-- hst
```