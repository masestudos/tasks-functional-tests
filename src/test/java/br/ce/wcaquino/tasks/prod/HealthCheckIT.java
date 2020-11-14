package br.ce.wcaquino.tasks.prod;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class HealthCheckIT {

	@Test
	public void healthCheck() throws MalformedURLException {
		DesiredCapabilities cap = DesiredCapabilities.chrome();
		//acessa aplicação de produção e verificar que o build está lá
		WebDriver driver = new RemoteWebDriver(new URL("http://192.168.99.100:4444/wd/hub/"), cap);
		try {
			driver.navigate().to("http://192.168.99.100:9999/tasks"); //leva a home
			//estratégia de espera evita problema entre a execução
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS); //espera até 10s
			String version = driver.findElement(By.id("version")).getText();
			Assert.assertTrue(version.startsWith("build"));//cada vez que executa o build é um número diferente
		} finally {//garante que independente de erro o driver será fechado
			driver.quit();//fecha a janela e libera o grid
		}
	}
}
