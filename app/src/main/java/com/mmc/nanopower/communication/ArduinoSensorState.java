package com.mmc.nanopower.communication;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

/*Exemplo de saída
* </html>t1ft2ft3f#t32#l4#d5#h19#m5</html>
* */

/**
 * Created by mario on 03/11/16.
 */
public class ArduinoSensorState extends AsyncTask<String, String, Boolean> {

    private SaveState saveState = SaveState.getInstancia();
    int delay = 5000;   // delay de 5 seg.
    int interval = 150000;  // intervalo de 2.5 min.
    Timer timer = new Timer();
    private static final String TAG = "ArduinoSensorState";

    public ArduinoSensorState() {

    }
    @Override
    protected Boolean doInBackground(String... params) {

        //TODO verificar se mesmo enviando comando para tomada, o arduino continua respondendo
        //corretamente o estado dos sensores
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {

                //saveState = SaveState.getInstancia();
                Log.d(TAG, " Entrou no scheduler");
                String URL = "http://10.10.0.50/";
                String linha = "";
                boolean Erro = true;
                /////alteracao teste aqui

                //Sera executada uma tomada por vez pois, o Arduino esta programado para atender
                //Apenas um comando por vez e retorna o resultado, visando melhorar o tempo de
                //respposta e resolver os travamentos que ocorriam com a etrategia de procurar
                //varios comandos na mesma requisicao

                /*TODO, checar a necessidade de verificar o estado atual da tomada antes de mandar
                o comando, ou deixar o arduino cuidar disso. Talvez o Arduino fique sobrecarregado
                */

                //informa se existe um acao de ligar ou desligar pendente
                //e verifica se o estado  atual da tomada eh diferente do comando que se deseja enviar.
                if(saveState.getFuzzyActiveT1() && (saveState.getStateTomada1Arduino() != saveState.getTomada1())){
                    //Caso seja de ligar, será enviado comando de ligar
                    if(saveState.getTomada1()) {
                        Log.d(TAG, " sera enviado ligar tomada 1");
                        URL += "t1h";
                        //Log.d("COMANDO DENTRO DO IF", URL);
                    }
                    //caso contrario desliga
                    else {
                        Log.d(TAG, " sera enviado desligar tomada 1");
                        //TODO, ao enviar um comando de desligar, tentar enviar notificação ao usuário
                        //para confirmar o desligamento. Aguardar tempo até a reposta.
                        URL += "t1l";
                    }
                    //Envio sera executado, nao estara mais pendente
                    saveState.setFuzzyActiveT1(false);
                }
                //TOMADA 2
                else if(saveState.getFuzzyActiveT2() && (saveState.getStateTomada2Arduino() != saveState.getTomada2())){
                    //Caso seja de ligar, será enviado comando de lgiar
                    if(saveState.getTomada2()) {
                        Log.d(TAG, " sera enviado ligar tomada 2");
                        URL += "t2h";
                    }
                    //caso contrario desliga
                    else {
                        Log.d(TAG, " sera enviado desligar tomada 2");
                        URL += "t2l";
                    }
                    //Envio sera executado, nao estara mais pendente
                    saveState.setFuzzyActiveT2(false);
                }
                //Para tomada 3
                else if(saveState.getFuzzyActiveT3() && (saveState.getStateTomada3Arduino() != saveState.getTomada3())){
                    //Caso seja de ligar, será enviado comando de lgiar
                    if(saveState.getTomada3()) {
                        Log.d(TAG, " sera enviado ligar tomada 3");
                        URL += "t3h";
                    }
                    //caso contrario desliga
                    else {
                        Log.d(TAG, " sera enviado desligar tomada 3");
                        URL += "t3l";
                    }
                    //Envio sera executado, nao estara mais pendente
                    saveState.setFuzzyActiveT3(false);
                }

                //////////////////////////

                try {

                    Log.d(TAG , " Enviando comandos as tomadas");
                    Log.d(TAG," Destino: " + URL);

                    HttpClient client = new DefaultHttpClient();
                    HttpGet requisicao = new HttpGet();
                    requisicao.setHeader("Content-Type",
                            "text/plain; charset=utf-8");
                    requisicao.setURI(new URI(URL));
                    HttpResponse resposta = client.execute(requisicao);
                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            resposta.getEntity().getContent()));
                    StringBuffer sb = new StringBuffer("");

                    while ((linha = br.readLine()) != null) {
                        sb.append(linha);
                    }

                    br.close();

                    linha = sb.toString();

                    Log.d(TAG, " recebido: " + linha);


                    updateSensorStatus(linha);


                    Erro = false;


                } catch (Exception except) {
                    Log.e(TAG, " CRASH StackTrace: " + Arrays.toString(except.getStackTrace()));
                    Erro = true;
                }

                try {
                    Thread.sleep(60000);
                    // Do some stuff
                    Log.d(TAG, " DORMIU");
                } catch (Exception e) {
                    e.getLocalizedMessage();
                    Erro = true;
                }
                Log.d(TAG, " ACORDOU");



                    Log.d(TAG," EXECUTANDO");
                }
        }, delay, interval);

        return true;

     }
    //}

    private void teste(String read){
        if(read.contains("#t")){
            Log.d(TAG, "TEMPO" + read);
        }
    }

    public void updateSensorStatus(String linha) {

        char t_temp;

        t_temp = linha.charAt(2);
        saveState.setStateTomada1Arduino((t_temp == 'o'));
        t_temp = linha.charAt(5);
        saveState.setStateTomada2Arduino((t_temp == 'o'));
        t_temp = linha.charAt(8);
        saveState.setStateTomada3Arduino((t_temp == 'o'));

        String t,l,d,h,m;

        int idx1,idx2,idx3,idx4,idx5,idx6;

        idx1 = linha.indexOf('!');
        idx2 = linha.indexOf('@');
        idx3 = linha.indexOf('#');
        idx4 = linha.indexOf('$');
        idx5 = linha.indexOf('%');
        idx6 = linha.indexOf('&');

        t = linha.substring(idx1 + 1, idx2);
        l = linha.substring(idx2 + 1, idx3);
        d = linha.substring(idx3 + 1, idx4);
        h = linha.substring(idx4 + 1, idx5);
        m = linha.substring(idx5 + 1, idx6);

        Log.d(TAG, "TEMP: " + t);
        Log.d(TAG, "LUZ: " + l);
        Log.d(TAG, "DIA: " + d);
        Log.d(TAG, "HORA: " + h);
        Log.d(TAG, "MINUTO: " + m);

        Log.d(TAG, " UPDATE SENSOR");

//        Double temp = Double.valueOf(t);
//        Double luz = Double.valueOf(l);
//        Double dia = Double.valueOf(d);
//        Double hora = Double.valueOf(h);
//        Double min = Double.valueOf(m);
//
        double temp = Double.valueOf(t);
        double luz = Double.valueOf(l);
        double dia = Double.valueOf(d);
        double hora = Double.valueOf(h);
        double min = Double.valueOf(m);

        saveState.setTemperatura(temp);
        saveState.setLuz(luz);
        saveState.setDiaSemana(dia);
        saveState.setHora(hora);
        saveState.setMinuto(min);


        Log.d(TAG, "CHEGOU AO FINAL");
    }

    @Override
    protected void onProgressUpdate(String... params) {
        //Log.d("DORMIU", " UPDATE");

        // Runs on UI thread after publishProgress(Progress...) is invoked
        // from doInBackground()
        //return true;


      //  Log.d("ACORDOU", " UPDATE");
        //updateSensorStatus();
    }

    @Override
    protected void onPostExecute(Boolean result) {
      //  Log.d("TESTE", " DORMIU");
        // Runs on the UI thread after doInBackground()


    }

}
