package io.github.jensdietrich.mvn.categories;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Version {

    public final static Pattern MAJOR_MINOR_OPTMICRONANO_SEPBYDOT = Pattern.compile("(\\d+\\.\\d+(\\.\\d+(\\.\\d+(\\.\\d+)?)?)?)");
    public final static Pattern MAJOR_MINOR_OPTMICRONANO_SEPBYUDRSCR = Pattern.compile("(\\d+_\\d+(_\\d+(_\\d+(_\\d+)?)?)?)");

    private int major = -1;
    private int minor = -1;
    private int micro = -1;
    private int nano = -1;
    private String prefix = "";
    private String suffix = "";
    private String tag = null;

    private Version() {}

    @Nonnull
    public static Version from(String tag) {
        Version version = null;
        Matcher matcher = MAJOR_MINOR_OPTMICRONANO_SEPBYDOT.matcher(tag);
        if (matcher.find()) {
            String core = matcher.group(1);
            version = from(tag,core,core.split("\\."));
        }
        else {
            matcher = MAJOR_MINOR_OPTMICRONANO_SEPBYUDRSCR.matcher(tag);
            if (matcher.find()) {
                String core = matcher.group(1);
                version = from(tag, core, core.split("_"));
            }
            else {
                version = new Version();
                version.suffix = tag;
                version.tag = tag;
            }
        }


        assert version!=null;
        return version;
    }


    private static Version from(String tag, String core, String... versionNumbers) {
        Version version = new Version();
        version.tag = tag;
        int start = tag.indexOf(core);
        for (int i=0;i<versionNumbers.length;i++) {
            int value = Integer.parseInt(versionNumbers[i]);
            if (i==0) {
                version.setMajor(value);
            }
            else if (i==1) {
                version.setMinor(value);
            }
            else if (i==2) {
                version.setMicro(value);
            }
            else if (i==3) {
                version.setNano(value);
            }
        }

        String prefix = tag.substring(0,start);
        version.setPrefix(prefix);
        String suffix = tag.substring(start+core.length());
        version.setSuffix(suffix);

        return version;
    }

    public boolean isPrerelease() {
        String suffix2 = suffix.toLowerCase();
        return  suffix2.contains("alpha") ||
                suffix2.contains("beta") ||
                suffix2.contains("rc") ||
                suffix2.contains("-try-") || // used in jackson
                suffix2.contains("internal") || // fastjson
                suffix.contains("CR") || // jboss/hibernate -- candidate release
                Pattern.matches("(-|\\.|_)M\\d",suffix) // milestone
                ;

    }

    public String getTag() {
        return tag;
    }

    public int getMajor() {
        return major;
    }

    public void setMajor(int major) {
        this.major = major;
    }

    public int getMinor() {
        return minor;
    }

    public void setMinor(int minor) {
        this.minor = minor;
    }

    public int getNano() {
        return nano;
    }

    public void setNano(int nano) {
        this.nano = nano;
    }

    public int getMicro() {
        return micro;
    }

    public void setMicro(int micro) {
        this.micro = micro;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Version version = (Version) o;
        return major == version.major &&
                minor == version.minor &&
                micro == version.micro &&
                nano == version.nano &&
                Objects.equals(prefix, version.prefix) &&
                Objects.equals(suffix, version.suffix);
    }

    @Override
    public int hashCode() {
        return Objects.hash(major, minor, micro, nano, prefix, suffix);
    }

    @Override
    public String toString() {
        return "Version{" + tag + '}';
    }
}
