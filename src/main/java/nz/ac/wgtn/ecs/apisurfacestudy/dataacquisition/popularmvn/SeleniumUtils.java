package nz.ac.wgtn.ecs.apisurfacestudy.dataacquisition.popularmvn;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.function.Predicate;

/**
 * Utilities for accessing the repo using selenium.
 * @author jens dietrich
 */
public class SeleniumUtils {

    public static final Logger LOGGER = LogManager.getLogger(SeleniumUtils.class);
    /**
     * Extract links.
     * @param url -- the page from where to extract
     * @param linkFilter -- only links matching this filter are returned
     * @return LinkedHashSet -- order is retained, duplicates are removed
     */
    public static LinkedHashSet<String> extractLinks(String url, Predicate<String> linkFilter) {

        LOGGER.info("extracting links from: " + url);
        WebDriver driver = new ChromeDriver();
        LinkedHashSet<String> links = new LinkedHashSet<>();
        driver.get(url);
        List<WebElement> elements = driver.findElements(By.xpath("//a[@href]"));
        for (WebElement element:elements) {
            String link = element.getAttribute("href");
            if (linkFilter.test(link)) {
                links.add(link);
            }
        }
        driver.close();
        LOGGER.info("\t links extracted:");
        for (String link:links) {
            LOGGER.info("\t\t"+link);
        }
        return links;
    }
}
