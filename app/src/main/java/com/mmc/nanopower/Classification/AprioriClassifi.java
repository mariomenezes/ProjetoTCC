package com.mmc.nanopower.Classification;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;

import com.mmc.nanopower.R;

import java.io.InputStream;

import weka.associations.Apriori;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

/**
 * Created by mario on 07/09/16.
 */
public class AprioriClassifi  extends AsyncTask<String, Void, Boolean> {

    /**
     * Expects a dataset as first parameter. The last attribute is used
     * as class attribute.
     *
     * @throws Exception	if something goes wrong
     */

    @Override
    protected Boolean doInBackground(String... params) {


        // load data
        //Instances data = DataSource.read(args[0]);
        //Instances data = ConverterUtils.DataSource.read("/home/mario/Documentos/UFAL/TCC/IA/supermarket.arff");
        Instances data = null;
        try {
            //data = ConverterUtils.DataSource.read("res/dataBase.arff");


            //data = ConverterUtils.DataSource.read("/storage/emulated/0/database.arff");
            data = ConverterUtils.DataSource.read("/storage/emulated/0/baseTesteConv.arff");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //data.setClassIndex(data.numAttributes() - 1);
        //data.setClassIndex(data.numAttributes() - 1);

        // build associator
        Apriori apriori = new Apriori();
        //apriori.setClassIndex(data.classIndex());
        try {
            apriori.buildAssociations(data);
        } catch (Exception e) {
            e.printStackTrace();
        }


        // output associator TODO
        Log.d("Saida", "" + apriori);
        //System.out.println(apriori);

        return true;
    }


}
