package bjtu.network;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpClient {
    private String result = "";

    public String doPost(String url,String method){
        HttpURLConnection conn = null;
        try{
            URL requestURL = new URL(url);
            conn = (HttpURLConnection) requestURL.openConnection();
            conn.setRequestMethod(method);
            conn.setConnectTimeout(60000);

            InputStream iStream = conn.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(
                    iStream,"UTF-8"));
            String tempLine = null;
            while((tempLine = rd.readLine())!=null){
                this.result = tempLine.toString();
                System.out.println(tempLine);
            }
            rd.close();
            iStream.close();
        }catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }finally{
            if(null != conn){
                conn.disconnect();
            }
        }
        return result;
    }
}
