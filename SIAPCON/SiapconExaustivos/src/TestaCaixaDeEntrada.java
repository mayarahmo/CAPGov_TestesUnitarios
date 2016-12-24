import org.testng.annotations.*;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.openqa.selenium.Keys;

import com.gargoylesoftware.htmlunit.javascript.host.Console;

// classe que testa o cadastro de um processo
public class TestaCaixaDeEntrada {
	
	// System.getProperty("user.dir") pega o caminho do projeto
	static String driverPath = System.getProperty("user.dir")+"/Drivers/";
    static String sprintNumber = "2";
	WebDriver driver;
	
	@BeforeClass
	 public void setUp() {
		  // code that will be invoked when this test is instantiated
	   	 System.out.println("--- Begin tests Chrome ---");
	   	 System.setProperty("webdriver.chrome.driver",driverPath+"chromedriver.exe");
	   	 driver  = new ChromeDriver();
	 	 driver.manage().window().maximize();
	}

	 // loga no SIAPCON
	 @Test ( groups = "login")
	 public void testaLoginSiapcon() throws Exception {
		 
		 driver.navigate().to("http://52.1.49.37/SIAPCON_SPRINT1"+sprintNumber);
		 
		 driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		 
		 DefaultThrowTreatment def = new  DefaultThrowTreatment();
		 
		 // verifica se os elementos existem
		 def.verifyElementPresenceById(driver, "RichWidgets_wt33:wtMainContent:wtUserNameInput");
		 def.verifyElementPresenceById(driver, "RichWidgets_wt33:wtMainContent:wtPasswordInput");
		 def.verifyElementPresenceById(driver, "RichWidgets_wt33:wtMainContent:wtLoginButton");
		 
		 // caso exista escreve
		 WebElement inputUser = driver.findElement(By.id("RichWidgets_wt33:wtMainContent:wtUserNameInput"));
		 WebElement inputPasswd = driver.findElement(By.id("RichWidgets_wt33:wtMainContent:wtPasswordInput"));
		 WebElement buttonLogin = driver.findElement(By.id("RichWidgets_wt33:wtMainContent:wtLoginButton"));
		 
		 // escreve as informações=
		 inputUser.sendKeys("claudiana.coelho");
		 inputPasswd.sendKeys("123456");
		 
		 // clica no botão
		 buttonLogin.click();
	 }
	 
	 
	 // Testa paginação
	 @Test (dependsOnGroups = "login", groups = "pagination")
	 public void testaPagination() throws Exception {
		 
		 driver.navigate().to("http://52.1.49.37/SIAPCON_SPRINT1"+sprintNumber+"/ListarProcessos.jsf?(Not.Licensed.For.Production)=");
		 
		 try{
			 
			 // carrega a lista de processos
			 List<WebElement> table = driver.findElements(By.xpath(".//a[contains(@id,'RichWidgets_wt88:wtMainContent:wtProcessoTable:')]"));
			 
			 if (!(table.size() == 20)){Assert.fail("Número de processos deveria ser 20 para esta configuração de paginação mas é"+Integer.toString(table.size()));}
			 
			 // a combobox que obtêm as opções de paginação
			 WebElement combobox = driver.findElement(By.id("RichWidgets_wt88:wtMainContent:wt48"));
			 combobox.sendKeys("40");
			 
			 // clica no botão de pesquisar
			 WebElement btnPesquisar = driver.findElement(By.id("RichWidgets_wt88:wtMainContent:wt121"));
			 btnPesquisar.click();
			 
			 // espera as requisições Ajax Terminarem
			 Thread.sleep(100000);
			 
			 table = driver.findElements(By.xpath(".//a[contains(@id,'RichWidgets_wt88:wtMainContent:wtProcessoTable:')]"));
			 
			 if (!(table.size() == 40)){Assert.fail("Número de processos deveria ser 40 para esta configuração de paginação mas é"+Integer.toString(table.size()));}

			 // mudar a combox para a opção de 60 itens
			 combobox.sendKeys("60");
			 
			 // clica no botão de pesquisa
			 btnPesquisar.click();
			 
			 // espera as requisições Ajax terminarem
			 Thread.sleep(100000);
			 
			 // recarrega a lista de processos
			 table = driver.findElements(By.xpath(".//a[contains(@id,'RichWidgets_wt88:wtMainContent:wtProcessoTable:')]"));
			 
			 if (!(table.size() == 60)){Assert.fail("Número de processos deveria ser 60 para esta configuração de paginação mas é"+Integer.toString(table.size()));}

		 }catch (Exception e){
			 throw(e);
		 }
		 
	 }
	 
	 // Ao terminar a sequência de testes anterior fecha o navegador
	 @AfterClass (dependsOnGroups = "pagination")
	 public void tearDown() {
		
		if(driver!=null) {
			System.out.println("Closing chrome browser");
			driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
			driver.quit();
		}
	 }

}
