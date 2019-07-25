import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import java.awt.image.BufferedImage
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.TimeUnit
import javax.imageio.ImageIO

class SeleniumDL {
    companion object {
        val selenuimHome = ""
    }

    fun download() {
        // your chromedriver.exe location
      System.setProperty("webdriver.chrome.driver", "E:\\working\\kotlin\\chromedriver.exe")
        val driver = ChromeDriver()
      val src = "D:\\"
        try {
          driver.get("https://www.aisinei.net/thread-18885-1-4.html")

          //set image directory
          var directory = driver.title
          directory = directory.substring(0, directory.lastIndexOf("[") - 1)
          val file = File("$src$directory\\")
          file.mkdir()

          //click and wait
            driver.findElement(By.cssSelector("a.xi2.attl_m")).click()
            driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS)

            val listResult: List<WebElement> = driver.findElements(By.cssSelector("div.mbn.savephotop > img"))
            for ((index, webElement) in listResult.withIndex()) {
                val url = URL(webElement.getAttribute("src"))

                // prepare connection
                var connection: HttpURLConnection = url.openConnection() as HttpURLConnection  // kotlin cast
                connection.setRequestProperty(
                        "User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36")
                val savedImg: BufferedImage = ImageIO.read(connection.inputStream)

                // you must have D:\\temp  path
              ImageIO.write(savedImg, "jpg", File(file, "$index.jpg"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            driver!!.close()
        } finally {
            driver!!.close()
        }
    }
}