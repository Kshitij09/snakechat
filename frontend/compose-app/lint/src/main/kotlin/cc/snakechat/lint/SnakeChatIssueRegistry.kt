package cc.snakechat.lint

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.client.api.Vendor
import com.android.tools.lint.detector.api.CURRENT_API

class SnakeChatIssueRegistry : IssueRegistry() {

    override val issues = listOf(
        DesignSystemDetector.ISSUE,
    )

    override val api: Int = CURRENT_API

    override val minApi: Int = 12

    override val vendor: Vendor = Vendor(
        vendorName = "SnakeChat",
        feedbackUrl = "https://github.com/Kshitij09/snakechat/issues",
        contact = "https://github.com/Kshitij09/snakechat",
    )
}
