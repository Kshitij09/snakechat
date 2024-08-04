package cc.snakechat.domain.common

interface DomainError

object Failure : DomainError
object NoInternet : DomainError
