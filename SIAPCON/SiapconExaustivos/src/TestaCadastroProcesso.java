import org.testng.annotations.*;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.openqa.selenium.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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

// classe que testa o cadastro de um processo
public class TestaCadastroProcesso {
	
	static String driverPath = "/home/maya/�?rea de Trabalho/ferramentas/Selenium/";	
	WebDriver driver;
	
	@BeforeClass
	 public void setUp() {
		  // code that will be invoked when this test is instantiated
	   	 System.out.println("--- Begin tests Chrome ---");
	   	 System.setProperty("webdriver.chrome.driver",driverPath+"chromedriver");
	   	 driver  = new ChromeDriver();
	 	 driver.manage().window().maximize();
	}
	
	 public String geraNumeroProcesso(){
		 Integer size = 13;
		 Random r = new Random();
		 String output = "";
		 
		 for (int i=0;i<size;i++){
			 output = output + Integer.toString(r.nextInt(9));
		 }
		 
		 return output;
	 }
	
	 // loga no SIAPCON
	 @Test ( groups = "login")
	 public void testaLoginSiapcon() throws Exception {
		 
		 driver.navigate().to("http://52.1.49.37/SIAPCON_SPRINT11/");
		 
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
	 
	 
	 // Testa se msg de obrigatoriedade está sendo exibida para o campo numero do processo na tela de cadastro de processo
	 @Test (dependsOnGroups = "login", groups = "required")
	 public void testaMsgNumero() throws Exception {
		 
		 driver.navigate().to("http://52.1.49.37/SIAPCON_SPRINT11/ListarProcessos.jsf?(Not.Licensed.For.Production)=");
		 
		 driver.navigate().to("http://52.1.49.37/SIAPCON_SPRINT11/ProcessoDetail.jsf?processoId=0&(Not.Licensed.For.Production)=");
		 
		 // gera um tempo de espera para a página carregar e o elemento ser renderizado
		 driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		 
		 try{
			 
			 // insere um numero de processo valido
			 WebElement numero = driver.findElement(By.id("RichWidgets_wt95:wtMainContent:wtnumeroProcessoAtualWidget"));
			 numero.sendKeys("000");
			 
			 // clica no botão submit
			 WebElement submit = driver.findElement(By.id("RichWidgets_wt95:wtMainContent:wt38"));
			 submit.click();
			 
			 // Espera até que o elemento que contêm a msg de erro esteja vísivel
			 WebDriverWait wait = new WebDriverWait(driver, 30); 
			 wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("Feedback_Message_Error")));
			 
			 // testa se a msg de obrigatoriedade está sendo exibida
			 if (!driver.findElement(By.className("Feedback_Message_Error")).isDisplayed()){ Assert.fail("Não está exibindo msg");}

		 }catch (Exception e){
			 throw(e);
		 }
		 
		 driver.navigate().to("http://52.1.49.37/SIAPCON_SPRINT11/ProcessoDetail.jsf?processoId=0&(Not.Licensed.For.Production)=");
		 
		 // insere um numero de processo valido
		 WebElement numero = driver.findElement(By.id("RichWidgets_wt95:wtMainContent:wtnumeroProcessoAtualWidget"));
		 numero.sendKeys("00000.000000/00");
		
		 try{
			 			 
			 // clica no botão submit
			 WebElement submit = driver.findElement(By.id("RichWidgets_wt95:wtMainContent:wt38"));
			 submit.click();
			 
			 // espera até que o elemento que contêm a msg de erro esteja visível
			 WebDriverWait wait = new WebDriverWait(driver, 30); 
			 wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("Feedback_Message_Error")));
			 
			 if (!driver.findElement(By.className("Feedback_Message_Error")).isDisplayed()){ Assert.fail("Não está exibindo msg");}

		 }catch (Exception e){
			 throw(e);
		 }
	 }
	 
	 // gera número de processo randômico
	 String proc = geraNumeroProcesso();
	 
	 // Ao terminar a sequência de testes anterior cadastra um processo e testa se cadastrou
	 @Test (dependsOnGroups = "required", groups = "salva")
	 public void testaInsercaoProcesso() {
		 
		 driver.navigate().to("http://52.1.49.37/SIAPCON_SPRINT11/ProcessoDetail.jsf?processoId=0&(Not.Licensed.For.Production)=");
		
		 driver.navigate().to("http://52.1.49.37/SIAPCON_SPRINT11/ProcessoDetail.jsf?processoId=0&(Not.Licensed.For.Production)=");
		 
		 try{
			 			 	 
			 // insere um numero de processo valido
			 WebElement numero = driver.findElement(By.id("RichWidgets_wt95:wtMainContent:wtnumeroProcessoAtualWidget"));
			 numero.sendKeys(proc);
			 
			 // insere um tipo de processo valido
			 WebElement tipo = driver.findElement(By.id("RichWidgets_wt95:wtMainContent:wttipoProcessoWidget"));
			 tipo.sendKeys("a");
			 
			 // clica no botão submit
			 WebElement submit = driver.findElement(By.id("RichWidgets_wt95:wtMainContent:wt38"));
			 submit.click();
			 
			 // espera até que o botão de encerrar cadastro esteja visível
			 WebDriverWait wait = new WebDriverWait(driver, 60); 
			 wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("corbranca")));
			 
			 if (!driver.findElement(By.className("corbranca")).isDisplayed()){ Assert.fail("Não exibiu o botão encerrar cadastro");}
		 }catch (Exception e){ throw(e);}
	 }
	 
	// Ao terminar a sequência de testes anterior busca o processo criado na caixa de entrada
	@Test (dependsOnGroups = "salva", groups = "salvou" )
	public void testaSeInseriuProcesso() throws Exception {
		
		  driver.navigate().to("http://52.1.49.37/SIAPCON_SPRINT11/ListarProcessos.jsf?(Not.Licensed.For.Production)=");
		  
		  try{
			  
			 // insere orgao do processo na busca
			 WebElement orgao = driver.findElement(By.id("RichWidgets_wt88:wtMainContent:wtnumeroProcessoOrgaoWidget"));
			 orgao.sendKeys(proc.substring(0,5));
			 
			 // insere numero do processo na busca
			 WebElement numero = driver.findElement(By.id("RichWidgets_wt88:wtMainContent:wtnumeroProcessoNumeroWidget"));
			 numero.sendKeys(proc.substring(5,11));
				  
			 // insere ano do processo na busca
			 WebElement ano = driver.findElement(By.id("RichWidgets_wt88:wtMainContent:wtnumeroProcessoAnoWidget"));
			 ano.sendKeys(proc.substring(11,13));
			 		 
			 // acha o input do submit e clica para fazer a pesquisa
			 WebElement submit3 = driver.findElement(By.xpath("//input[@type='submit']"));
			 submit3.click(); 
					 
			 // para esperar a resposta do Ajax impede o Selenium de prosseguir
			 Thread.sleep(60000);
			 
			 WebElement table = driver.findElement(By.id("RichWidgets_wt88:wtMainContent:wtProcessoTableAjaxRfrsh"));
			 
			 WebElement a = driver.findElement(By.id("RichWidgets_wt88:wtMainContent:wtProcessoTable:0:wt7")); 
			 
			 String processo = proc.substring(0,5)+"."+proc.substring(5,11)+"/"+proc.substring(11,13);
		
			 if (!a.getText().toString().contains(processo)){Assert.fail("Não cadastrou!");}
	
			 
		 }catch (Exception e){ throw(e);}
	}
			 	 
	 // Ao terminar a sequência de testes anterior fecha o navegador
	 @AfterClass (dependsOnGroups = "salvou")
	 public void tearDown() {
		
		if(driver!=null) {
			System.out.println("Closing chrome browser");
			driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
			driver.quit();
		}
	 }

}
