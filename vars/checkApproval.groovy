def call() {
    def initiatorUserId
    def approverUserId

    // Get initiator user
    wrap([$class: 'BuildUser']) {
        initiatorUserId = env.BUILD_USER_ID
        echo "Initiated by: ${initiatorUserId} (${env.BUILD_USER})"
    }

    // Get approver input
    approverUserId = input(
        message: 'Does the diff look correct?',
        submitterParameter: 'APPROVER'
    )

    // Validate approver
    echo "Initiator: ${initiatorUserId}"
    echo "Approver : ${approverUserId}"

    if (initiatorUserId == approverUserId) {
        error("Approval failed: Initiator and approver cannot be the same user.")
    } else {
        echo "Approval succeeded: Different user approved the build."
    }
}
