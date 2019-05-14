package utils;

import java.util.List;


import org.openqa.selenium.WebElement;

public class UserFunctions {

	public void Dropdown(List<WebElement> radiobuttons, String optionToSelect ) {

		for(WebElement radiobutton: radiobuttons) { 
			if(radiobutton.getAttribute("value").equals(optionToSelect))
				radiobutton.click();
		}
	}

}
