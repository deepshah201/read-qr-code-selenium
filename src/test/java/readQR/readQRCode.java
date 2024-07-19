package readQR;

import static org.testng.Assert.assertEquals;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

public class readQRCode {

	private static WebDriver driver;
	BufferedImage bufferImage;
	LuminanceSource source;
	BinaryBitmap bitmap;
	Result result;
	String qrInputText = "Read QR code";

	@BeforeSuite
	public void set_up() {
		driver = new ChromeDriver();
		driver.get("https://qaplayground.dev/apps/qr-code-generator/");
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
	}

	@Test
	public void read_qr_code() throws MalformedURLException, IOException, NotFoundException {
		driver.findElement(By.cssSelector("input[type=\"text\"]")).sendKeys(qrInputText);
		driver.findElement(By.tagName("button")).click();
		String qrImageURL = driver.findElement(By.cssSelector(".qr-code > img")).getAttribute("src");
		String qrResult = decode_QR_image(qrImageURL);
		assertEquals(qrResult, qrInputText);
	}

	@AfterSuite
	public void tear_down() {
		driver.quit();
	}

	private String decode_QR_image(String qrImageURL) throws MalformedURLException, IOException, NotFoundException {
		bufferImage = ImageIO.read(new URL(qrImageURL));
		source = new BufferedImageLuminanceSource(bufferImage);
		bitmap = new BinaryBitmap(new HybridBinarizer(source));
		result = new MultiFormatReader().decode(bitmap);
		System.out.println("Decoded result: " + result);
		return result.getText();
	}

}
