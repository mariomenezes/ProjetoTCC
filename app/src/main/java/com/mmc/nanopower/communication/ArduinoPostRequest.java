package com.mmc.nanopower.communication;

/**
 * Created by mario on 07/09/16.
 */

        import java.io.BufferedReader;
        import java.io.InputStreamReader;
        import java.net.URI;

        import org.apache.http.HttpResponse;
        import org.apache.http.client.HttpClient;
        import org.apache.http.client.methods.HttpGet;
        import org.apache.http.impl.client.DefaultHttpClient;

        import android.os.AsyncTask;
        import android.util.Log;
        import android.widget.Switch;

public class ArduinoPostRequest extends AsyncTask<String, Void, Boolean> {

    @Override
    protected Boolean doInBackground(String... params) {

        String URL = "http://10.10.0.50/";
        String linha = "";
        Boolean Erro = true;

        if (params.length > 0)
            // faço qualquer coisa com os parâmetros

            try {
                Log.d("Executando", "ARDUINO TESTE");

                HttpClient client = new DefaultHttpClient();
                HttpGet requisicao = new HttpGet();
                requisicao.setHeader("Content-Type",
                        "text/plain; charset=utf-8");
                requisicao.setURI(new URI(URL + params[0]));
                HttpResponse resposta = client.execute(requisicao);
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        resposta.getEntity().getContent()));
                StringBuffer sb = new StringBuffer("");

                while ((linha = br.readLine()) != null) {
                    sb.append(linha);
                }

                br.close();

                linha = sb.toString();

                Log.d("RESPOSTA ", linha);

                Erro = false;

            } catch (Exception e) {
                Erro = true;
            }

        return Erro;
    }

}
