data class UrlWithTitle(val url: String, val title: String) {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as UrlWithTitle

    if (url != other.url) return false
    if (title != other.title) return false

    return true
  }

  override fun hashCode(): Int {
    var result = url.hashCode()
    result = 31 * result + title.hashCode()
    return result
  }
}