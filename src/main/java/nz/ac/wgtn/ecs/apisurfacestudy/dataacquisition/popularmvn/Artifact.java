package nz.ac.wgtn.ecs.apisurfacestudy.dataacquisition.popularmvn;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Holder for artifact data.
 * @author jens dietrich
 */
public class Artifact {

    private String category = null ; // from https://mvnrepository.com/open-source
    private String categoryURL = null;
    private String artifactId = null;
    private String groupId = null;
    private String artifactURL = null;
    private String metadataURL = null;
    private String metadataLocalFile = null;
    private List<String> comments = new ArrayList<>();
    private int rankInCategory = 0;
    private String latestVersion = null;
    private String latestVersionPOMURL = null;
    private String latestVersionPOMLocalFile = null;
    private String scmConnection = null;
    private String scmURL = null;
    private String localRepo = null;
    private List<String> tags = null;
    private String selectedTag = null;

    public static List<Artifact> loadFrom (File file) throws FileNotFoundException {
        Type listType = new TypeToken<ArrayList<Artifact>>() {}.getType();
        Gson gson = new Gson();
        return gson.fromJson(new FileReader(file),listType);
    }

    public static void saveTo(List<Artifact>  artifacts, File file) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter out = new FileWriter(file)) {
            gson.toJson(artifacts, out);
        }
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getLocalRepo() {
        return localRepo;
    }

    public void setLocalRepo(String localRepo) {
        this.localRepo = localRepo;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategoryURL() {
        return categoryURL;
    }

    public void setCategoryURL(String categoryURL) {
        this.categoryURL = categoryURL;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArtifactURL() {
        return artifactURL;
    }

    public void setArtifactURL(String artifactURL) {
        this.artifactURL = artifactURL;
    }

    public String getMetadataURL() {
        return metadataURL;
    }

    public void setMetadataURL(String metadataURL) {
        this.metadataURL = metadataURL;
    }

    public String getMetadataLocalFile() {
        return metadataLocalFile;
    }

    public void setMetadataLocalFile(String metadataLocalFile) {
        this.metadataLocalFile = metadataLocalFile;
    }


    public String getLatestVersion() {
        return latestVersion;
    }

    public void setLatestVersion(String latestVersion) {
        this.latestVersion = latestVersion;
    }

    public String getLatestVersionPOMURL() {
        return latestVersionPOMURL;
    }

    public void setLatestVersionPOMURL(String latestVersionPOMURL) {
        this.latestVersionPOMURL = latestVersionPOMURL;
    }

    public String getLatestVersionPOMLocalFile() {
        return latestVersionPOMLocalFile;
    }

    public void setLatestVersionPOMLocalFile(String latestVersionPOMLocalFile) {
        this.latestVersionPOMLocalFile = latestVersionPOMLocalFile;
    }


    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }

    public int getRankInCategory() {
        return rankInCategory;
    }

    public void setRankInCategory(int rankInCategory) {
        this.rankInCategory = rankInCategory;
    }

    public String getScmConnection() {
        return scmConnection;
    }

    public void setScmConnection(String scmConnection) {
        this.scmConnection = scmConnection;
    }

    public String getScmURL() {
        return scmURL;
    }

    public void setScmURL(String scmURL) {
        this.scmURL = scmURL;
    }

    public String getSelectedTag() {
        return selectedTag;
    }

    public void setSelectedTag(String selectedTag) {
        this.selectedTag = selectedTag;
    }

    // some derived properties

    public boolean isGitRepo () {
        return this.scmConnection!=null && this.scmConnection.startsWith("scm:git");
    }

    public boolean isSvnRepo () {
        return this.scmConnection!=null && this.scmConnection.startsWith("scm:svn");
    }

    public boolean isCvsRepo () {
        return this.scmConnection!=null && this.scmConnection.startsWith("scm:cvs");
    }

    public boolean isHgRepo () {
        return this.scmConnection!=null && this.scmConnection.startsWith("scm:hg");
    }

    public String getName() {
        return this.getGroupId() + "/" + this.getArtifactId();
    }

    private boolean fileExistsInProjectRootFolder(String name) {
        if (localRepo!=null) {
            File projectFolder = new File(localRepo);
            return new File(projectFolder,name).exists();
        }
        return false;
    }
    public boolean isMavenProject () {
        return fileExistsInProjectRootFolder("pom.xml");
    }

    public boolean isGradleGroovyProject () {
        return fileExistsInProjectRootFolder("build.gradle");
    }

    public boolean isGradleKotlinProject () {
        return fileExistsInProjectRootFolder("build.gradle.kts");
    }

    public boolean isGradleProject () {
        return isGradleGroovyProject() || isGradleKotlinProject();
    }

    public boolean isSbtProject () {
        return fileExistsInProjectRootFolder("build.sbt");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Artifact artifact = (Artifact) o;
        return rankInCategory == artifact.rankInCategory &&
                Objects.equals(category, artifact.category) &&
                Objects.equals(categoryURL, artifact.categoryURL) &&
                Objects.equals(artifactId, artifact.artifactId) &&
                Objects.equals(groupId, artifact.groupId) &&
                Objects.equals(artifactURL, artifact.artifactURL) &&
                Objects.equals(metadataURL, artifact.metadataURL) &&
                Objects.equals(metadataLocalFile, artifact.metadataLocalFile) &&
                Objects.equals(comments, artifact.comments) &&
                Objects.equals(latestVersion, artifact.latestVersion) &&
                Objects.equals(latestVersionPOMURL, artifact.latestVersionPOMURL) &&
                Objects.equals(latestVersionPOMLocalFile, artifact.latestVersionPOMLocalFile) &&
                Objects.equals(scmConnection, artifact.scmConnection) &&
                Objects.equals(scmURL, artifact.scmURL) &&
                Objects.equals(localRepo, artifact.localRepo) &&
                Objects.equals(tags, artifact.tags) &&
                Objects.equals(selectedTag, artifact.selectedTag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(category, categoryURL, artifactId, groupId, artifactURL, metadataURL, metadataLocalFile, comments, rankInCategory, latestVersion, latestVersionPOMURL, latestVersionPOMLocalFile, scmConnection, scmURL, localRepo, tags, selectedTag);
    }
}
