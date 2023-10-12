GITHUB_REPO_URL = "https://github.com/NovahubIntern2023/office-tracking-be"
BUILD_BRANCH = "main"

START_MSG = "Deployment started"
SUCCESS_MSG = "Deployment succeeded"
FAILED_MSG = "Deployment failed"


def remote = [:]
remote.name = "officetrackingdev"
remote.host = "192.168.1.2"
remote.allowAnyHosts = true

node {
    checkout scm

    stage('Test') {
        setBuildStatus("PENDING", START_MSG);
        slackSend color: "gray", message: "*office-tracking-backend*: ${START_MSG}"
    }


    withCredentials([
        usernamePassword(credentialsId: 'office-tracking-server', passwordVariable: 'password', usernameVariable: 'username')
    ]) {
        remote.user = username
        remote.password = password

        stage("Deploy") {
            try {
                sshCommand remote: remote, command: """
                    .  ~/.profile
                    cd /home/officetracking/apps/office-tracking-be
                    git checkout ${BUILD_BRANCH}
                    git pull origin ${BUILD_BRANCH}
                    docker-compose -f docker-compose.prod.yml  --env-file .env up -d --build
                """

                setBuildStatus("SUCCESS", SUCCESS_MSG);
            }
            catch (exc) {
                setBuildStatus("FAILURE", FAILED_MSG);
                error exc.toString()
            }
        }
    }
}

void setBuildStatus(String state, String message) {
  step([
      $class: "GitHubCommitStatusSetter",
      reposSource: [$class: "ManuallyEnteredRepositorySource", url: GITHUB_REPO_URL],
      contextSource: [$class: "ManuallyEnteredCommitContextSource", context: "ci/jenkins/build-status"],
      errorHandlers: [[$class: "ChangingBuildStatusErrorHandler", result: "UNSTABLE"]],
      statusResultSource: [ $class: "ConditionalStatusResultSource", results: [[$class: "AnyBuildResult", message: message, state: state]] ]
  ]);
}