package br.ce.wcaquino.taks.functional;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class TasksTest {
	
	public WebDriver acessarAplicacao() throws MalformedURLException {
		System.setProperty("webdriver.chrome.driver","C:\\Users\\-moNGe_\\dev\\java\\seleniumDrivers\\chromedriver.exe");
		//pega um chromedriver que est� na m�quina e se comunica diretamente
		//WebDriver driver = new ChromeDriver(); //agente colocar um chromedriver no path
		/*teste se comunica com o selenium hub (grid) que delega o teste para os n�s e eles se viram na m�quina que eles est�o hospedados
		para pegar o driver e fazer a execu��o dos testes, na pr�tica � a mesma coisa porque todos os n�s est�o na mesma 
		m�quina ent�o a �nica coisa que muda � a forma de comunica��o*/
		DesiredCapabilities cap = DesiredCapabilities.chrome(); //carrega o perfil do chrome e envia para o remotedriver, browser que vai utilizar
		//qunado executa pede para o hub hospedado no endere�o http://192.168.1.100:5555/wd/hub/ criar inst�ncias de chrome para fazer o teste
		WebDriver driver = new RemoteWebDriver(new URL("http://192.168.99.100:4444/wd/hub/"), cap);//docker-machine ip se usar o docker toolbox
		driver.navigate().to("http://192.168.1.100:8001/tasks");
		//estrat�gia de espera evita problema entre a execu��o
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS); //espera at� 10s
		return driver;
	}
	
	@Test
	public void deveSalvarTarefaComSucesso() throws MalformedURLException {
		WebDriver driver = acessarAplicacao();
		
		try {
			//fluxo para quando n�o encontra um elemento, por isso usa o tru para n�o ficar travado
			driver.findElement(By.id("addTodo")).click();
			driver.findElement(By.id("task")).sendKeys("Teste via Selenium");
			driver.findElement(By.id("dueDate")).sendKeys("25/12/2020");
			driver.findElement(By.id("saveButton")).click();
			String message = driver.findElement(By.id("message")).getText();
			Assert.assertEquals("Success!", message);
		} finally {
			driver.quit();
		}
	}
	
	@Test
	public void naoDeveSalvarTarefaSemDescricao() throws MalformedURLException {
		WebDriver driver = acessarAplicacao();
		
		try {
			//fluxo para quando n�o encontra um elemento, por isso usa o tru para n�o ficar travado
			driver.findElement(By.id("addTodo")).click();
			driver.findElement(By.id("dueDate")).sendKeys("25/12/2020");
			driver.findElement(By.id("saveButton")).click();
			String message = driver.findElement(By.id("message")).getText();
			Assert.assertEquals("Fill the task description", message);	
		} finally {
			driver.quit();
		}
	}
	
	@Test
	public void naoDeveSalvarTarefaSemData() throws MalformedURLException {
		WebDriver driver = acessarAplicacao();
		
		try {
			//fluxo para quando n�o encontra um elemento, por isso usa o tru para n�o ficar travado
			driver.findElement(By.id("addTodo")).click();
			driver.findElement(By.id("task")).sendKeys("Teste via Selenium");
			driver.findElement(By.id("saveButton")).click();
			String message = driver.findElement(By.id("message")).getText();
			Assert.assertEquals("Fill the due date", message);
		} finally {
			driver.quit();
		}
	}
	
	@Test
	public void naoDeveSalvarTarefaComDataPassada() throws MalformedURLException {
		WebDriver driver = acessarAplicacao();
		
		try {
			//fluxo para quando n�o encontra um elemento, por isso usa o tru para n�o ficar travado
			driver.findElement(By.id("addTodo")).click();
			driver.findElement(By.id("task")).sendKeys("Teste via Selenium");
			driver.findElement(By.id("dueDate")).sendKeys("25/12/2010");
			driver.findElement(By.id("saveButton")).click();
			String message = driver.findElement(By.id("message")).getText();
			Assert.assertEquals("Due date must not be in past", message);
		} finally {
			driver.quit();
		}
	}
}
