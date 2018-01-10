import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static final String url1 = "https://stackoverflow.com/questions/12470141/server-returned-http-response-code-400";
    public static final String url2 = "https://stackoverflow.com/questions/12470141/server-returned-http-response-code-400";
    public static final String url3 = "https://cwiki.apache.org//confluence/display/MAVEN/MojoFailureException";
    public static final String url4 = "https://www.dns-shop.ru/";
    public static final String regex = "(https?:\\/\\/)([\\w\\.]+)\\.([a-z]{2,6}\\.?)(\\/[\\w\\.]*)";
    public static final String testGex = "<[Aa][\\s]{1}[^>]*[Hh][Rr][Ee][Ff][^=]*=[ '\\\"\\s]*([^ \\\"'>\\s#]+)[^>]*>";
    public static final String easyRegex = "<a href=\"([^\"]+)";

    public static final Pattern PATTERN = Pattern.compile(easyRegex);


    public static void main(String[] args) throws IOException {

/*
        Set<String> hrefs = read();
        Set<String> result = new HashSet<>();
        Iterator<String> iterator = hrefs.iterator();
        while (iterator.hasNext()) {
            String temp = iterator.next();
            //result.add(temp.replaceAll("<a href=\"", ""));
            if (temp.contains("http") || temp.contains("https")) {

                result.add(temp.replaceAll("<a href=\"", ""));
            }
        }
        result.forEach(System.out::println);*/
        HTMLHelper helper = HTMLHelper.getInstance();
        Set<String> test1 = helper.getAllLinksByUrl(url4, easyRegex);
        System.out.println("Test");
        test1.forEach(System.out::println);
        System.out.println("Test");
        Set<String> test2 = helper.getAllLinksByUrl(url4, easyRegex);
        test2.forEach(System.out::println);
        System.out.println("Test");
        Set<String> test3 = helper.getAllLinksByUrl(url4, easyRegex);
        test3.forEach(System.out::println);

    }

    public static Set<String> read() throws IOException {
        ClassLoader loader = ClassLoader.getSystemClassLoader();
        File file = new File(loader.getResource("test").getFile());
        BufferedReader reader = new BufferedReader(new FileReader(file));
        Set<String> tempSet = new HashSet<>();
        while (reader.ready()) {
            tempSet.add(reader.readLine());
        }
        return tempSet;
    }

    public static String getClearUrl(String link) {

        return new String();
    }
}

interface HTMLOperation {
    Set<String> findUrlsInHtml(String htmlPageContent, String regex);

    String loadHtmlPageByUrl(String siteStringUrl);

    Set<String> getAllLinksByUrl(String url);

    void showAllLinksByUrl(String url);

    Set<String> getAllLinksByUrl(String url, String pattern);

    String cleanUrl(String badUrl);

}

class HTMLHelper implements HTMLOperation {
    //private String defaultPattern;
    private Set<String> linsk;
    private static HTMLHelper instance = new HTMLHelper();
    private String defaultRegex;
    private String htmlContentPage;


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
        String htmlContent = loadHtmlPageByUrl(url);
        if (htmlContent != null) {
            Set<String> urls = findUrlsInHtml(htmlContent, pattern);
            return urls;
        } else {
            return null;
        }
    }

    @Override
    public String cleanUrl(String badUrl) {
        if (badUrl.contains("http") || badUrl.contains("https")) {
            return badUrl.replaceAll("<a href=\"", "");
        }
        return null;
    }

    @Override
    public Set<String> findUrlsInHtml(String htmlPageContent, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(htmlPageContent);
        Set<String> temp = new HashSet<>();
        while (m.find()) {
            if (m.group().contains("http") || m.group().contains("https")) {
                temp.add(m.group().replaceAll("<a href=\"", ""));
            }
        }
        return temp;
    }

    public String getHtmlContentPage() {
        return htmlContentPage;
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
                this.htmlContentPage = resultHtml.toString();

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