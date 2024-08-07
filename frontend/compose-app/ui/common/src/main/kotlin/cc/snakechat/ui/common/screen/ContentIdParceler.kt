package cc.snakechat.ui.common.screen

import android.os.Parcel
import cc.snakechat.domain.model.common.CommentId
import cc.snakechat.domain.model.common.ContentId
import cc.snakechat.domain.model.common.PostId
import kotlinx.parcelize.Parceler

class ContentIdParceler : Parceler<ContentId> {
    override fun ContentId.write(parcel: Parcel, flags: Int) {
        val identifier = when (this) {
            is CommentId -> TYPE_COMMENT
            is PostId -> TYPE_POST
        }
        parcel.writeInt(identifier)
        parcel.writeString(id)
    }

    override fun create(parcel: Parcel): ContentId {
        return when (parcel.readInt()) {
            TYPE_POST -> PostId(parcel.readString()!!)
            TYPE_COMMENT -> CommentId(parcel.readString()!!)
            else -> throw IllegalArgumentException("Unknown content id type")
        }
    }

    companion object {
        private const val TYPE_POST = 0
        private const val TYPE_COMMENT = 1
    }
}