package cc.snakechat.strings

class SnakeStrings(
    val home: String,
    val explore: String,
    val live: String,
    val videos: String,
    val viewAllComments: (Long) -> String = { count ->
        if (count == 1L) {
            "View 1 comment"
        } else {
            "View $count comments"
        }
    },
    val follow: String,
    val posts: String,
    val followers: String,
    val following: String,
    val userNotFound: String,
    val noInternet: String,
    val somethingWentWrong: String,
)

object Locales {
    const val EN = "en"
}
