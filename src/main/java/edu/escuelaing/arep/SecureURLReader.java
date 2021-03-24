package edu.escuelaing.arep;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyStore;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The type Secure url reader.
 */
public class SecureURLReader {

    /**
     * Enable secure connection with other servers.
     *
     * @throws Exception the exception
     */
    public static void enableConnection() throws Exception {
        File trustStoreFile = new File("keystore/myTrustStore");
        char[] trustStorePassword = "654321".toCharArray();
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(new FileInputStream(trustStoreFile), trustStorePassword);
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(trustStore);
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmf.getTrustManagers(), null);
        SSLContext.setDefault(sslContext);
        javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier((hostname, sslSession) -> true);
    }

    /**
     * Read url string and execute requests .
     *
     * @param url the url of the endpoint of request
     * @return the string response of the other server
     * @throws IOException the io exception
     */
    public static String readURL(String url) throws IOException {
        URL siteURL = new URL(url);
        StringBuilder result = new StringBuilder();
        HttpURLConnection conn = (HttpURLConnection) siteURL.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        rd.close();
        return result.toString();
    }

}
