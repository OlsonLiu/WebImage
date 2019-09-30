import org.openqa.selenium.By
import org.openqa.selenium.chrome.ChromeDriver
import java.awt.image.BufferedImage
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import java.util.stream.StreamSupport
import javax.imageio.ImageIO
import kotlin.streams.toList

class NlegsDL {
  companion object {
    val driver = ChromeDriver()
    val src = "D:\\"
  }

  fun downloadAll(siteUrl: String) {
    try {
      driver.get(siteUrl)
      var urls = driver.findElements(By.cssSelector("table.table.table-hover > tbody > tr > td > a"))
      val dlObjs: List<UrlWithTitle> = StreamSupport.stream(urls.spliterator(), false).map {
        UrlWithTitle(it?.getAttribute("href").toString().trim(), it?.getAttribute("innerText").toString())
      }.toList()

      dlObjs.stream().filter { filterGoodPart(it.title) }.forEach { downloadWholePage(it.url) }
    } catch (e: Exception) {
      e.printStackTrace()
      driver!!.close()
    } finally {
      driver!!.close()
    }
  }

  fun downloadWholePage(singleSite: String) {
    driver.get(singleSite)
    var directory = driver.title
    directory = directory.substring(0, directory.lastIndexOf("|") - 1)
    val file = File("$src$directory\\")
    file.mkdir()

    var elements = driver.findElements(By.cssSelector(".col-md-12.col-lg-12.panel.panel-default .panel-body a"))
    for ((index, webElement) in elements.withIndex()) {
      val urlStr = webElement.getAttribute("href").toString().trim()
      var url = URL(urlStr)

      var connection: HttpURLConnection = url.openConnection() as HttpURLConnection  // kotlin cast
      connection.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)")
      connection.addRequestProperty("Referer", urlStr)
      val savedImg: BufferedImage = ImageIO.read(connection.inputStream)

      ImageIO.write(savedImg, "jpg", File(file, "$index.jpg"))
    }
  }

  private fun filterGoodPart(siteName: String): Boolean {
    val stars = listOf<String>("IMiss")
    return stars.stream().anyMatch { siteName.contains(it) }
  }
}