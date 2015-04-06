package com.local.android.acelerometroprueba;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.LinkedList;


public class MainActivity extends ActionBarActivity implements SensorEventListener {

    private SensorManager mSensor;
    private TextView tvSensorValue, tvVendorValue, tvVersionValue,
            tvPowerValue, tvValueX, tvValueY, tvValueZ, tvTiempo, tvModulo;

    private float gravedad=9.8066f;

    private LinkedList<Muestra> lista;
    private int tamaLista=250;

    private long pt=0; //peak time
    private long contadorTiempo=0;
    private String estado="muestreo";
    Muestra[] datos=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);

        tvSensorValue = (TextView) findViewById(R.id.sensorNameValue);
        tvVendorValue = (TextView) findViewById(R.id.sensorVendorValue);
        tvVersionValue = (TextView) findViewById(R.id.sensorVersionValue);
        tvPowerValue = (TextView) findViewById(R.id.sensorPowerValue);

        tvValueX = (TextView) findViewById(R.id.valueX);
        tvValueY = (TextView) findViewById(R.id.valueY);
        tvValueZ = (TextView) findViewById(R.id.valueZ);

        tvTiempo = (TextView) findViewById(R.id.tiempo);
        tvModulo = (TextView) findViewById(R.id.modulo);

        mSensor = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor.registerListener(this, mSensor.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),  20000);
        // 20000 microsegundos. => 20 milisegundos

        mostrarInformacion();

        lista=new LinkedList<Muestra>();

        Log.i("Acelerometro","Creado");
    }

    /**
     * Método para iniciar servicio de muestreo
     *
     * @param
     * @return
     */
    private void iniciarServicio(){
      /*
      Version con intentservice
      Intent muestreo=new Intent(getApplicationContext(), MuestreoService.class);
       // muestreo.setData(); pasar datos si es necesario.
        startService(muestreo);*/


        //version con service

      /*  Thread t = new Thread(){
            public void run(){
                getApplicationContext().bindService(
                        new Intent(getApplicationContext(), MyAndroidUpnpServiceImpl.class),
                        serviceConnection,
                        Context.BIND_AUTO_CREATE
                );
            }
        };
        t.start()*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // TODO Auto-generated method stub
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            refreshValues(event);
        }
    }

    private void mostrarInformacion() {
        // List<Sensor> sensorList =
        // mSensor.getSensorList(Sensor.TYPE_ACCELEROMETER);
        Sensor sensor = mSensor.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        tvSensorValue.setText(sensor.getName());
        tvVendorValue.setText(sensor.getVendor());
        tvVersionValue.setText(String.valueOf(sensor.getVersion()));
        tvPowerValue.setText(String.valueOf(sensor.getPower()));

    }

    private void refreshValues(SensorEvent event) {
        float values[] = event.values;

        float x = values[0];
        float y = values[1];
        float z = values[2];

        float xg=x/gravedad;
        float yg=y/gravedad;
        float zg=z/gravedad;

    /*    tvValueX.setText(String.valueOf(x)+" "+String.valueOf(xg));
        Log.i("refreshValues", "Valor del Eje X: " + String.valueOf(x));
        tvValueY.setText(String.valueOf(y)+" "+String.valueOf(yg));
        Log.i("refreshValues", "Valor del Eje Y: " + String.valueOf(y));
        tvValueZ.setText(String.valueOf(z)+" "+String.valueOf(zg));
        Log.i("refreshValues", "Valor del Eje Z: " + String.valueOf(z));*/

        long tiempo=event.timestamp;
        tvTiempo.setText(String.valueOf(tiempo));
       // Log.i("refreshValues","Tiempo de medida: "+String.valueOf(tiempo));

        //calculo de modulo de vector aceleracion
        double modulo=Math.sqrt(    Math.pow(xg,2) + Math.pow(yg,2)+ Math.pow(zg,2)   );
        tvModulo.setText(String.valueOf(modulo));

        cargarMuestra(new Muestra(tiempo,modulo));

        if(estado.equals("muestreo")){
            if(modulo>2) iniciarPostpeak(tiempo);
        }

        if(estado.equals("postpeak")){
            contadorTiempo=tiempo-pt;
            if(modulo>2) iniciarPostpeak(tiempo);
            if(contadorTiempo>2500000000l){
                //generar array de valores.
                datos=new Muestra[lista.size()];
                lista.toArray(datos);
                iniciarActivityTest();
                Log.i("Acelerometro","iniciar activity test "+tiempo);
            }
        }


        Log.i("refreshValues","ContadorTiempo: "+contadorTiempo);



    }

    private void iniciarPostpeak(long tiempo){
        contadorTiempo=0;
        pt=tiempo;
        estado="postpeak";
        Log.i("Acelerometro","iniciar post peak "+tiempo);
    }

    private void iniciarActivityTest(){
        //capturar datos de lista
        estado="activitytest";
        Log.i("Acelerometro","iniciar activity test");

        //calcular AAMV , media de las diferencias.
        long tiempoInicioCalculo=pt+1000000000; //se toma desde 1 sg a 2.5 sg despues del impacto
        int marcador=0;
        double difTotal=0;

        for(int i=0;i<datos.length;i++){
            //buscar el dato con tiempo > tiempoInciocalculo
           if( datos[i].getTiempo()>tiempoInicioCalculo ){
               marcador=i;
               break;
           }
        }
        for(int j=marcador;j<datos.length-2;j++){
            double dif=Math.abs( datos[j].getAceleracion() - datos[j+1].getAceleracion() );
            difTotal=difTotal+dif;
        }
        difTotal=difTotal/(datos.length-marcador);
        Log.i("Acelerometro","difTotal: "+difTotal);
        //dependiendo del valor difTotal se envian los datos a clasificador o se considera "no caida".
        estado="muestreo";
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        mSensor.unregisterListener(this);

       // imprimirMuestra();
        imprimirArray();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        mSensor.registerListener(this,   mSensor.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),   SensorManager.SENSOR_DELAY_UI);
    }


    //********* METODOS CONTROL LISTA *******************/

    private void cargarMuestra(Muestra muestra){
        lista.add(muestra);
        if(lista.size()>tamaLista) lista.poll();
    }

    private void imprimirMuestra(){
        for(int i=0;i<lista.size();i++){
           Muestra mu= lista.get(i);
            Log.i("Muestra", "Muestra "+i+" tiempo: +"+mu.getTiempo()+" acele: "+mu.getAceleracion());
        }

        long dif=lista.get(lista.size()-1).getTiempo()- lista.get(0).getTiempo();
       Log.i("Muestra","diferencia tiempo: "+dif );
    }

    private void imprimirArray(){
        if(datos!=null){
            for(int i=0;i<datos.length;i++){
                Muestra mu=datos[i];
                Log.i("Muestra", "Dato: "+i+" tiempo: +"+mu.getTiempo()+" acele: "+mu.getAceleracion());
            }
        }else{
            Log.i("Muestra","datos nulos ");
        }
    }



    /********** calculos valores para clasificador ****************/
    //si hay que pasar datos a clasificador entonces calcular las siguientes caracteristicas.
    //pasar pt,
    //pasar lista de datos
    //calcular IE, IS
    //calcular AAMV, IDI, MPI, MVI, PDI, ARI, FPI, SCI.


    private void calcularIE(long peaktime,Muestra[] valores){

        long ptpos=peaktime+1000000000; //nanosegundos
        int marcadorPeak=0;
        int marcadorPeakMas=valores.length-1;
        long tiempoIE=0;

        for(int i=0;i<valores.length;i++){
            long eltiempo=valores[i].getTiempo();
            if(eltiempo>=peaktime){
                marcadorPeak=i;
            }
            if(eltiempo>=ptpos){
                marcadorPeakMas=i;
                break;
            }
        }

        for(int j=marcadorPeakMas; j==marcadorPeak; j--){
            if(valores[j].getAceleracion()>1.5) {
                tiempoIE=valores[j].getTiempo();
                break;
            }
        }

    }

    private void calcularIS(long peaktime,Muestra[] valores){

    }
}
