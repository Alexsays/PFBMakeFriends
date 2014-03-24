/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pfbmakefriends;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author Tronco
 */
public class Friends extends Observable implements Runnable {

    private String login;
    private String pass;
    private int desde;
    private int hasta;

    public Friends(String login, String pass, int desde, int hasta) {
        this.login = login;
        this.pass = pass;
        this.desde = desde;
        this.hasta = hasta;
    }

    

    @Override
    public void run() {
        String form = "<form accept-charset=\"UTF-8\"></form>";
        String urlBase = "http://playfulbet.com";
        String url_action = "/users/sign_in";
        String method = "post";
        List<String> listaPaginas = new LinkedList<String>();
        DefaultHttpClient httpclient = new DefaultHttpClient();
        try {

            HttpPost httpost = new HttpPost(urlBase + url_action);

            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("utf8", "1"));
            nvps.add(new BasicNameValuePair("authentity_token", "5722GOYTf+HR5a5ytz5VOHWB9fI/pwCAM2CCmZT4bvo="));
            nvps.add(new BasicNameValuePair("user[login]", this.login));
            nvps.add(new BasicNameValuePair("user[password]", this.pass));
            nvps.add(new BasicNameValuePair("user[remember_me]", "1"));
            nvps.add(new BasicNameValuePair("commit", "Enviar"));

            httpost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));

            HttpResponse response = httpclient.execute(httpost);
            HttpEntity entity = response.getEntity();

            System.out.println("Login form get: " + response.getStatusLine());
            EntityUtils.consume(entity);
            System.out.println("Waiting...");
            Thread.sleep(1000);
            /**/
            for (int i = this.desde; i < this.hasta; i++) {
                httpost = new HttpPost(urlBase + "/peticiones?category=friendship&receiver_id=" + i);

                nvps = new ArrayList<NameValuePair>();
                nvps.add(new BasicNameValuePair("authentity_token", "5722GOYTf+HR5a5ytz5VOHWB9fI/pwCAM2CCmZT4bvo="));

                httpost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
                response = httpclient.execute(httpost);
                entity = response.getEntity();

                System.out.println("Friend: " + i + ": " + response.getStatusLine());
                EntityUtils.consume(entity);
                System.out.println("Waiting...");
                setChanged();
                notifyObservers(response.getStatusLine());
                Thread.sleep(10);
            }/**/
            System.out.println("\nFin");

        } catch (Exception ex) {
            System.out.println(ex.toString());
            httpclient.getConnectionManager().shutdown();
            ex.printStackTrace();
        } finally {
            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
            httpclient.getConnectionManager().shutdown();
        }
        //Obtenemos los links de las paginas de apuestas
        //Obtenemos los links de las apuestas
        //Por cada link de apuesta obtenemos los ratios de apuesta y parametrizamos para obtener las mejores apuestas 

    }

    @Override
    public String toString() {
        return super.toString() + ":" + this.login + "," + this.pass + "," + this.desde + "," + this.hasta;
    }
}
