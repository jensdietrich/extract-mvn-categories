package nz.ac.wgtn.ecs.apisurfacestudy.dataacquisition.popularmvn;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.io.File;
import java.io.FileWriter;
import java.text.DateFormat;
import java.util.*;
import java.util.regex.Pattern;
import static nz.ac.wgtn.ecs.apisurfacestudy.dataacquisition.popularmvn.SeleniumUtils.extractLinks;

/**
 * Fetch popular categories and top artifacts within those categories from mvnrepository.com/.
 * mvnrepository challenges suspected scraping calls with captchas, the easiest way to circumvent this
 * is to use a browser via the selenium chrome driver.
 * The project contains a driver for chrome-90 for OSX, if rerunning this, the respective property webdriver.chrome.driver
 * needs to be adapted.
 * @author jens dietrich
 */
public class FetchPopularMvnCategoriesAndArtifacts {

    public static final int ARTIFACT_PAGES = 1;
    public static final int CATEGORY_PAGES = 1;
    public static final String POPULAR_CATEGORIES_URL = "https://mvnrepository.com/open-source";
    public static final Pattern ARTIFACT_URI_PATTERN = Pattern.compile("https:\\/\\/mvnrepository.com\\/artifact\\/.+\\/.+");
    public static final Pattern CATEGORY_URI_PATTERN = Pattern.compile("https:\\/\\/mvnrepository.com\\/open-source\\/.+");
    public static final String RESULTS = "results/popular-mvn-categories-and-artifacts.json";
    public static final Logger LOGGER = LogManager.getLogger(FetchPopularMvnCategoriesAndArtifacts.class);

    public static void main(String[] args) throws Exception {

        final String driverSystemProperty = "webdriver.chrome.driver";
        String driver = System.getProperty(driverSystemProperty);

        Preconditions.checkState(driver!=null, "driver system property not set: " + driverSystemProperty);
        Preconditions.checkState(new File(driver).exists(), "driver not found: " + new File(driver).getAbsolutePath());

        // fetch popular categories and top artifacts within those categories
        File resultFile = new File(RESULTS);

        // extract categories
        // the order on the page is irrelevant
        Set<String> categoryLinks = new HashSet<>();
        for (int i = 1; i <= CATEGORY_PAGES; i++) {
            String url = POPULAR_CATEGORIES_URL + (i == 1 ? "" : ("?p=" + i));
            categoryLinks.addAll(extractLinks(url, link -> (CATEGORY_URI_PATTERN.matcher(link).matches())));
        }

        List<Artifact> artifacts = new ArrayList<>();
        // extract links for each category
        for (String categoryURL : categoryLinks) {
            String categoryName = categoryURL.substring(categoryURL.lastIndexOf('/') + 1);
            for (int i = 1; i <= ARTIFACT_PAGES; i++) {
                String url = categoryURL + (i == 1 ? "" : ("?p=" + i));
                Collection<String> artifactLinks = extractLinks(url, link -> ARTIFACT_URI_PATTERN.matcher(link).matches() && !link.endsWith("/usages"));
                int rank = 1;
                for (String artifactLink : artifactLinks) {
                    // com.fasterxml.jackson.core/jackson-databind
                    String relLink = artifactLink.replace("https://mvnrepository.com/artifact/", "");
                    String[] parts = relLink.split("/");
                    assert parts.length == 2;
                    Artifact artifact = new Artifact();
                    artifact.setArtifactId(parts[1]);
                    artifact.setArtifactURL(artifactLink);
                    artifact.setGroupId(parts[0]);
                    artifact.setCategory(categoryName);
                    artifact.setCategoryURL(categoryURL);
                    artifact.setRankInCategory(rank);
                    artifact.getComments().add("category and artifact collected by " + FetchPopularMvnCategoriesAndArtifacts.class.getName() + " on " + DateFormat.getDateTimeInstance().format(new Date()));
                    artifacts.add(artifact);
                    rank = rank+1;
                }
            }
        }

        // export results
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter out = new FileWriter(RESULTS)) {
            gson.toJson(artifacts, out);
        }
        LOGGER.info("Artifact urls gathered written to " + RESULTS);
    }

}
