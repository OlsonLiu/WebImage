
import org.apache.commons.validator.routines.UrlValidator
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import org.springframework.util.StringUtils
import java.io.ByteArrayInputStream
import java.io.File
import java.nio.ByteBuffer
import javax.imageio.ImageIO

/* @author  OLSON.LIU
 * @since   2018/9/17
 * @version 1.0
 */
class WebSite {
    companion object {
        var storeImagePath = "D:\\temp"
        val urlScheme = arrayOf("https", "http")
    }

    constructor()
    constructor(localPath: String) {
        //TODO find why this location cannt be replaced
        storeImagePath = if (!existDirectory(localPath)) localPath else storeImagePath
    }

    fun grabWholeHtml(url: String) {
        if (validateUrl(url)) {
            var html: Document = Jsoup.connect(url)
                    .userAgent("Chrome")
                    .cookie("auth", "token")
                    .timeout(3000)
                    .get()
            val imgElements: Elements = html.getElementsByTag("img")
            for ((index, eachEle) in imgElements.withIndex()) {

                val imgUrl = eachEle.absUrl("src")
                download(imgUrl, index.toString() + ".jpg")
            }
        }
    }

    private fun download(imgUrl: String, fileName: String) {
        var imgBytes: ByteArray? = ByteArray(0)
        try {
            imgBytes = Jsoup.connect(imgUrl)?.userAgent("Chrome")
                    ?.cookie("auth", "token")
                    ?.timeout(3000)?.ignoreContentType(true)?.execute()?.bodyAsBytes()
        } catch (e: Exception) {
            e.printStackTrace()
            //TODO why this always process empty string
            println("$imgUrl : connect fail")
        }
        var buffer = ByteBuffer.wrap(imgBytes)
        moveToLocal(buffer, "$storeImagePath", fileName)
    }

    private fun moveToLocal(buffer: ByteBuffer, savePath: String, fileName: String) {
        if (buffer.array().size < 15000) return // skip little icon
        val filePath = "$savePath\\$fileName"
        val img = ImageIO.read(ByteArrayInputStream(buffer.array()))
        //TODO only do the jpg now, need more extensibility
        ImageIO.write(img, "jpg", File(filePath))
    }

    private fun validateUrl(url: String): Boolean {
        val validator = UrlValidator(urlScheme)
        return validator.isValid(url)
    }

    private fun existDirectory(localPath: String): Boolean {
        return !StringUtils.isEmpty(localPath) && File(localPath).isDirectory
    }
}