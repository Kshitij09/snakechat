package cc.snakechat.domain.model.common

sealed interface ContentId {
    val id: String
}

class PostId(override val id: String) : ContentId
class CommentId(override val id: String) : ContentId