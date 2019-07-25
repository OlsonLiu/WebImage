import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import java.awt.image.BufferedImage
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import java.util.stream.Collectors
import java.util.stream.StreamSupport
import javax.imageio.ImageIO

class NlegsDL {
  companion object {
    val driver = ChromeDriver()
    val src = "D:\\"
  }

  fun downloadAll(siteUrl: String) {
//    System.setProperty("webdriver.chrome.driver", "E:\\working\\kotlin\\chromedriver.exe")
    try {
      driver.get(siteUrl)
      var urls = driver.findElements(By.cssSelector("table.table.table-hover > tbody > tr > td > a"))
      val urlList: List<String> = StreamSupport.stream(urls.spliterator(), true)
          .map { t: WebElement? -> t?.getAttribute("href").toString().trim() }.collect(Collectors.toList())

      for ((index, url) in urlList.withIndex()) {
        downloadWholePage(url)
        println("${index}th download complete")
      }
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

    //click and wait
    var elements = driver.findElements(By.cssSelector(".col-md-12.col-lg-12.panel.panel-default .panel-body a"))
    for ((index, webElement) in elements.withIndex()) {
      val urlStr = webElement.getAttribute("href").toString().trim()
      var url = URL(urlStr)

      var connection: HttpURLConnection = url.openConnection() as HttpURLConnection  // kotlin cast
//      connection.addRequestProperty(
//          "User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)")
      connection.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)")
      connection.addRequestProperty("Referer", urlStr)
      val savedImg: BufferedImage = ImageIO.read(connection.inputStream)

      // you must have D:\\temp  path
      ImageIO.write(savedImg, "jpg", File(file, "$index.jpg"))
    }
    driver.navigate().back()
  }
}