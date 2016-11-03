package com.mmc.nanopower.communication;

/**
 * Created by mario on 07/09/16.
 */

        import java.io.BufferedReader;
        import java.io.InputStreamReader;
        import java.net.URI;
        import java.util.ArrayList;

        import org.apache.http.HttpResponse;
        import org.apache.http.client.HttpClient;
        import org.apache.http.client.methods.HttpGet;
        import org.apache.http.impl.client.DefaultHttpClient;

        import android.os.AsyncTask;
        import android.util.Log;
        import android.widget.Switch;

public class ArduinoPostRequest extends AsyncTask<String, Void, Boolean> {

    SaveState saveState = null;
    @Override
    protected Boolean doInBackground(String... params) {

        ArrayList<String> arrayLiga = new ArrayList<>();
        ArrayList<String> arrayDesliga = new ArrayList<>();

        String URL = "http://10.10.0.50/";
        String linha = "";
        boolean Erro = true;

        saveState = SaveState.getInstancia();

        //TODO verificar se é aqui que será desativado o fuzzyActive
        if(params[0] == "fuzzy"){
            if(saveState.getFuzzyActiveT1()) {
                if (saveState.getTomada1())
                    arrayLiga.add("t1h");
                else
                    arrayDesliga.add("t1l");
            }
            if(saveState.getFuzzyActiveT2()) {
                if (saveState.getTomada2())
                    arrayLiga.add("t2h");
                else
                    arrayDesliga.add("t2l");
            }
            if(saveState.getFuzzyActiveT3()) {
                if (saveState.getTomada3())
                    arrayLiga.add("t3h");
                else
                    arrayDesliga.add("t3l");
            }

        }

        //else
            if (params.length > 0)
                // faço qualquer coisa com os parâmetros

                try {
                    for(int i = 0; i < arrayLiga.size(); ++i){

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
                }

                    for(int i = 0; i < arrayDesliga.size(); ++i){

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
                    }

                } catch (Exception e) {
                    Erro = true;
                }
            saveState.setFuzzyActiveT1(false);
            saveState.setFuzzyActiveT2(false);
            saveState.setFuzzyActiveT3(false);

            return Erro;
    }

}
