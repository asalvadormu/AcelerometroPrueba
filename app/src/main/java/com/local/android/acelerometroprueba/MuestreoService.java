package com.local.android.acelerometroprueba;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by FESEJU on 31/03/2015.
 */
public class MuestreoService extends Service {

    @Override
    public int onStartCommand(Intent intent,int flags, int startId){
        //TODO hacer algo

        //iniciar sensor .
        //guardar datos


        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        //TODO retorno comunicacion

        return null;
    }



    @Override
    public void onDestroy(){
        //parar sensor.
    }


}
