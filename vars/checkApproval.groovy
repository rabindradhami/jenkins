def call() {
    def initiatorUserId
    def approverUserId

    stage('Get Initiator') {
        wrap([$class: 'BuildUser']) {
            initiatorUserId = env.BUILD_USER_ID
            echo "Initiated by: ${initiatorUserId} (${env.BUILD_USER})"
        }
    }

    stage('Approval') {
        approverUserId = input(
            message: 'Do you approve this action?',
            ok: 'Approve',
            submitterParameter: 'APPROVER'
        )
    }

    stage('Validate Approver') {
        echo "Initiator: ${initiatorUserId}"
        echo "Approver : ${approverUserId}"

        if (initiatorUserId == approverUserId) {
            error("Approval failed: Initiator and approver cannot be the same user.")
        } else {
            echo "Approval succeeded: Different user approved the build."
        }
    }
}
