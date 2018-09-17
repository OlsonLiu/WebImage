import org.apache.commons.validator.routines.UrlValidator
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

/* @author  OLSON.LIU
 * @since   2018/9/17
 * @version 1.0
 */
class WebSite {
  companion object {
    const val storeImagePath = "D:\\pic";
    val urlScheme = arrayOf("https", "http")
  }

  fun grabWholeHtml(url: String) {
    if (valiadteUrl(url)) {
      val html: Document = Jsoup.connect(url).get()
      val imgElements: Elements = html.getElementsByTag("img")
      for (eachEle in imgElements) {
        val img = eachEle.absUrl("src")
        println("the image src : $img")
      }
    }
  }

  private fun valiadteUrl(url: String): Boolean {
    val validator = UrlValidator(urlScheme)
    return validator.isValid(url)
  }
}