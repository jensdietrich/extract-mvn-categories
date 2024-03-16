# extract-mvn-categories
Extract artifact categories from mvnrepository.com

The application uses selenium to remnote control a browser, it needs to be started with a system 
variable to set the driver. When using the chrome driver, start the application with:

`java -cp <classpath> -Dwebdriver.chrome.driver="<path-to-driver>"`

See https://www.browserstack.com/guide/run-selenium-tests-using-selenium-chromedriver how to obtain a driver. 

The application is using the chrome driver by default, to change this edit the following line in `SeleniumUtils`:
`WebDriver driver = new ChromeDriver();`. Switching to another browser may also require to
change the `selenium-chrome-driver` dependency in `pom.xml`. 