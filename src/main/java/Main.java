import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Main {
    public static final String url1 = "https://stackoverflow.com/questions/12470141/server-returned-http-response-code-400";
    public static final String url2 = "https://stackoverflow.com/questions/12470141/server-returned-http-response-code-400";
    public static final String url3 = "https://cwiki.apache.org//confluence/display/MAVEN/MojoFailureException";
    public static final String url4 = "http://www.baeldung.com/java-8-parallel-streams-custom-threadpool";

    public static void main(String[] args) {

        HTMLOperation helper = HTMLHelper.getInstance();
        helper.showAllLinksByUrl(url1);
        helper.showAllLinksByUrl(url2);
        helper.showAllLinksByUrl(url3);
        helper.showAllLinksByUrl(url4);

    }
}

interface HTMLOperation {
    Set<String> findUrlInHtml(String htmlPageContent, String regex);

    String loadHtmlPageByUrl(String siteStringUrl);

    Set<String> getAllLinksByUrl(String url);

    void showAllLinksByUrl(String url);

    Set<String> getAllLinksByUrl(String url, String pattern);

}

class HTMLHelper implements HTMLOperation {
    //private String defaultPattern;
    private Set<String> linsk;

    private static HTMLHelper instance = new HTMLHelper();
    private String defaultRegex;


    private HTMLHelper() {
        this.linsk = new HashSet<>();
        this.defaultRegex = "(https?:\\/\\/)([\\w\\.]+)\\.([a-z]{2,6}\\.?)(\\/[\\w\\.]*)";
    }

    public static synchronized HTMLHelper getInstance() {
        if (instance == null) {
            return new HTMLHelper();
        } else {
            return instance;
        }
    }

    public Set<String> getAllLinksByUrl(String url) {

        return getAllLinksByUrl(url, defaultRegex);
    }

    @Override
    public void showAllLinksByUrl(String url) {
        if (getAllLinksByUrl(url) != null) {
            Iterator<String> iterator = getAllLinksByUrl(url).iterator();
            while (iterator.hasNext()) {
                System.out.println(iterator.next());
            }
            System.out.println();
        }
    }

    @Override
    public Set<String> getAllLinksByUrl(String url, String pattern) {
        StringBuilder builder = new StringBuilder();
        String htmlContent = loadHtmlPageByUrl(url);
        if (htmlContent != null) {
            Set<String> urls = findUrlInHtml(htmlContent, pattern);
            return urls;
        } else {
            return null;
        }
    }

    @Override
    public Set<String> findUrlInHtml(String htmlPageContent, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(htmlPageContent);
        Set<String> temp = new HashSet<>();
        while (m.find()) {

            temp.add(m.group());

        }
        return temp;
    }

    @Override
    public String loadHtmlPageByUrl(String siteStringUrl) {
        try {
            URL url = new URL(siteStringUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_FORBIDDEN) {
                System.err.println("Error, Server returned HTTP response code: 403 URL " + connection.getURL());
                return null;
            } else {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));
                StringBuilder resultHtml = new StringBuilder();
                String buffer = null;
                while ((buffer = reader.readLine()) != null) {
                    resultHtml
                            .append(buffer)
                            .append("\n");
                }
                return resultHtml.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setPattern(String pattern) {

        this.defaultRegex = pattern;
    }

}