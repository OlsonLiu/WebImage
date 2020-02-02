
import org.openqa.selenium.By
import org.openqa.selenium.chrome.ChromeDriver
import java.awt.image.BufferedImage
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLConnection
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
    if (!file.isDirectory) file.mkdir()

    var elements = driver.findElements(By.cssSelector(".col-md-12.col-lg-12.panel.panel-default .panel-body a"))
    for ((index, webElement) in elements.withIndex()) {
      var fileNameIndex = index + 1
      if (File(file, "$fileNameIndex.jpg").exists()) continue;

      val urlStr = webElement.getAttribute("href").toString().trim()
      val img = extractNeededParameter(urlStr)
      val imgUrl = "http://nlegs.com/images/${img}.jpg"
      var url = URL(imgUrl)

      var connection: URLConnection = url.openConnection() as HttpURLConnection  // kotlin cast
      connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36")
      connection.addRequestProperty("Referer", urlStr)
      val savedImg: BufferedImage = ImageIO.read(connection.inputStream)

      ImageIO.write(savedImg, "jpg", File(file, "${fileNameIndex}.jpg"))
    }
  }

  private fun extractNeededParameter(hrefString: String): String {
    if (hrefString == null) throw IllegalArgumentException("error format href")
    var scratch: List<String> = hrefString.split("&")
    var imageStr = scratch.filter { (!it.isNullOrEmpty() && it.startsWith("img=")) }[0]
    return imageStr.substringAfter("img=")
  }

  private fun filterGoodPart(siteName: String): Boolean {
    val stars = listOf<String>("IMiss")
    return stars.stream().anyMatch { siteName.contains(it) }
  }
}