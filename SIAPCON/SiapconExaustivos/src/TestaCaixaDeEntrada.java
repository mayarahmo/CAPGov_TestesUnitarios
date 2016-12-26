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

// classe que testa os comportamentos de paginação das Caixa de Entrada do SIAPCON
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
	 
	 
	 // Testa combobox de paginação
	 /*
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
		 
	 }*/
	 
	 // testa a o botão irpara
	 @Test (dependsOnGroups = "login", groups = "irpara" )
	 public void testaIrPara() throws Exception{
		 
		 driver.navigate().to("http://52.1.49.37/SIAPCON_SPRINT1"+sprintNumber+"/ListarProcessos.jsf?(Not.Licensed.For.Production)=");
		 
		 try{
			 // div que contêm a página atual e o total de páginas
			 WebElement divPagination = driver.findElement(By.id("RichWidgets_wt88:wtMainContent:wtpaginacaoWidget:wtdivPagina"));
			 
			 // string separadora da contagem de página
			 String pageSeparator = "/";
			 
			 // índice da String separadora da contagem de página
			 Integer separatorPosition = divPagination.getText().indexOf(pageSeparator);
			 
			 // forma a string com o total de páginas na Caixa de Entrada
			 String totalPaginas = divPagination.getText().substring(separatorPosition+1, divPagination.getText().length());
			 
			 // gera um inteiro dentro do intervalo de páginas que há
			 Random number = new Random();
			 Integer validPage = number.nextInt(Integer.parseInt(totalPaginas));
			 if (validPage == 0){validPage++;}
		
			 // escreve o inteiro no input de paginação
			 WebElement paginationInput = driver.findElement(By.id("RichWidgets_wt88:wtMainContent:wtpaginacaoWidget:wtirParaPaginaWidget"));
			 paginationInput.clear();
			 paginationInput.sendKeys(validPage.toString());
			 
			 // clica no botão ir para
			 WebElement btnIrPara = driver.findElement(By.id("RichWidgets_wt88:wtMainContent:wtpaginacaoWidget:wt7"));
			 btnIrPara.click();
			 
			 // Dá um tempo para as requisições Ajax terminarem em milisec impedindo o Selenium de prosseguir
			 // Para o DOM terminar de ser recarregado
			 Thread.sleep(60000);
			 
			 // div que contêm a página atual e o total de páginas
			 divPagination = driver.findElement(By.id("RichWidgets_wt88:wtMainContent:wtpaginacaoWidget:wtdivPagina"));
			 
			 // forma a string com a página atual
			 // Pega tudo até a barra pra depois remover o que não é número
			 totalPaginas = divPagination.getText().substring(0, separatorPosition+1);
			 
			 // remove os espaços
			 totalPaginas = totalPaginas.replaceAll(" ", "");
			 // remove as barras
			 totalPaginas = totalPaginas.replaceAll("/", "");
			 // remove os caracteres não numéricos
			 totalPaginas = totalPaginas.replaceAll("[^0-9]", "");
			
			 // compara com a página que foi setada no input
			 if (!(validPage == Integer.parseInt(totalPaginas))){Assert.fail("Número da página atual não é o mesmo setado no input de navegação de páginas");}
		 }catch (Exception e){
			 throw(e);
		 } 
	 }
	 
	 // Ao terminar a sequência de testes anterior fecha o navegador
	 @AfterClass (dependsOnGroups = "irpara")
	 public void tearDown() {
		
		System.out.println("--- Tests Chrome Finished ---");
		if(driver!=null) {
			System.out.println("Closing chrome browser");
			driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
			driver.quit();
		}
	 }

}
