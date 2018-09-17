import org.apache.commons.validator.routines.UrlValidator
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import org.springframework.util.StringUtils
import java.io.File
import java.nio.ByteBuffer

/* @author  OLSON.LIU
 * @since   2018/9/17
 * @version 1.0
 */
class WebSite {
  companion object {
    var storeImagePath = "C:\\"
    val urlScheme = arrayOf("https", "http")
  }

  constructor()
  constructor(localPath: String) {
    storeImagePath = if (!existDirectory(localPath)) localPath else storeImagePath
  }

  fun grabWholeHtml(url: String) {
    if (validateUrl(url)) {
      val html: Document = Jsoup.connect(url).get()
      val imgElements: Elements = html.getElementsByTag("img")
      for (eachEle in imgElements) {
        val imgUrl = eachEle.absUrl("src")
        println("the image src : $imgUrl")
      }
    }
  }

  private fun download(imgUrl:String){
    val imgBytes  = Jsoup.connect(imgUrl).ignoreContentType(true).execute().bodyAsBytes()
    var buffer = ByteBuffer.wrap(imgBytes)
  }

  private fun moveToLocal(buffer: ByteBuffer){
    var imgCnt = 1

  }

  private fun validateUrl(url: String): Boolean {
    val validator = UrlValidator(urlScheme)
    return validator.isValid(url)
  }

  private fun existDirectory(localPath: String): Boolean {
    return !StringUtils.isEmpty(localPath) && File(localPath).isDirectory
  }
}