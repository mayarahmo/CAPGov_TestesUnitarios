import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

public class DefaultThrowTreatment {
	
	DefaultThrowTreatment(){
		
	}
	
	// este método verifica se um elemento com 
	public void verifyElementPresenceById(WebDriver driver, String id){
		
		try{driver.findElement(By.id(id));}
		catch(NoSuchElementException e){Assert.fail("Elemento com id : "+id+" não foi encontrado ");}
		catch(Exception e ){ throw(e); }
	}

}
