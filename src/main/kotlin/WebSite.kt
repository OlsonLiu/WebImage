import org.apache.commons.validator.routines.UrlValidator
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import org.springframework.util.StringUtils
import java.io.File
import java.nio.file.Files

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
  constructor(localPath:String){
    storeImagePath = if (!existDirectory(localPath)) localPath else storeImagePath
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

  private fun existDirectory(localPath:String):Boolean{
    return !StringUtils.isEmpty(localPath) && File(localPath).isDirectory
  }
}