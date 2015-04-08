package com.local.android.acelerometroprueba;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.LinkedList;

/**
 * Clase para construir la red neuronal completa.
 * Se trata de un perceptrón multicapa.
 *
 * Created by SAMUAN on 06/04/2015.
 */
public class Red {

    private double[] vector_entrada;
    private double[] vector_parcial;
    private double[] vector_salida;

    private double[][] sinapsisA; //relaciona la entrada con la capa oculta
    private double[][] sinapsisB; //relaciona la capa oculta con la capa de salida

    private double[] biasA; //capa oculta
    private double[] biasB; //capa de salida

    private double[] target;
    private double[] errorA; //errores en la capa oculta
    private double[] errorB; //errores en la capa de salida

    private double ratioAprendizaje;
    private int numEntradas;
    private int numCapaOculta;
    private int numCapaSalida;

    public Red(){

    }

    //cargar datos archivo externo?
    //guardar datos en archivo externo cuando termine entrenamiento
    //pasar archivo con valores de entrenamiento.
    //indicar ciclos de entrenamiento , tantos ciclos con estos datos. ?







    /**
     * Calcula los valores de salida de la red completa.
     *
     */
    public void calcular(){
        //vector entrada por sinapsisA
        double suma=0;
        for(int i=0;i<sinapsisA.length;i++){ //i es cada neurona de esta capa
            suma=0;
            for(int j=0;j<sinapsisA[i].length;j++) { //j es numero de entradas.
                suma +=vector_entrada[j]*sinapsisA[i][j];
                Log.i("CALCULAR","calculo: "+vector_entrada[j]+" "+sinapsisA[i][j]+" "+suma );
            }
            suma=suma+biasA[i];
            vector_parcial[i]=logsig(suma);
        }

        //vector parcial por sinapsisB
        for(int i=0;i<sinapsisB.length;i++){
            suma=0;
            for(int j=0;j<sinapsisB[i].length;j++) {
                suma +=vector_parcial[j]*sinapsisB[i][j];
            }
            suma=suma+biasB[i];
            vector_salida[i]=logsig(suma);
        }
    }

    /**
     * Función de activación
     * Se utiliza una función sigmoidal.
     *
     * @param val valor de x
     * @return resultado
     */
    private double logsig(double val){
        return 1/(1+Math.exp(-val));
    }

    /*********** MÉTODOS PARA ENTRENAMIENTO *******/

    /**
     * Inicia los pesos con valores en el intervalo (-0.5,0.5)
     *
     */
    public void iniciarSinapsis(){
        for(int i=0;i<sinapsisB.length;i++){
            for(int j=0;j<sinapsisB[0].length;j++) {
                sinapsisB[i][j]=Math.random()-0.5;
                sinapsisA[i][j]=Math.random()-0.5;
            }
        }
        grabar();
    }

    /**
     * Iniciar los datos básicos de la red.
     * Se utiliza al preparar la red para aprendizaje.
     *
     * @param ratioAprendizaje
     * @param numEntradas Cantidad de datos de entrada
     * @param numCapaOculta Cantidad de neuronas en capa oculta
     * @param numCapaSalida Cantidad de neuronas en capa de salida
     */
    public void iniciarRed(double ratioAprendizaje,int numEntradas, int numCapaOculta, int numCapaSalida ){
        this.ratioAprendizaje=ratioAprendizaje;
        this.numEntradas=numEntradas;
        this.numCapaOculta=numCapaOculta;
        this.numCapaSalida=numCapaSalida;

        vector_entrada=new double[this.numEntradas];
        vector_parcial=new double[this.numEntradas];
        vector_salida=new double[this.numEntradas];

        sinapsisA=new double[numEntradas][numCapaOculta];
        sinapsisB=new double[numCapaOculta][numCapaSalida];

        biasA=new double[numCapaOculta];
        biasB=new double[numCapaSalida];

        target=new double[numCapaSalida];
        errorA=new double[numCapaOculta];
        errorB=new double[numCapaSalida];
    }

    /**
     *
     * algoritmo de entrenamiento
     *
     * ejecución hacia adelante
     * calculo errores en ultima capa
     * modificación pesos de sinapsis B
     * calculo error capa oculta
     * modificación pesos sinapsis A
     */
    public void entrenamiento(){
        //iniciar sinapsis
        //caturar datos de prueba + numero de iteraciones

        //cada entrada será 8 valores de entrada + 4 de salida.
        //setVector_entrada
        //setTarget

       //repetir varias veces , una por cada valor de entrada , target.
        //hasta que el error sea cercano a cero o se acaben las entradas.
        calcular();
        calcularErroresNeuronaSalida();
        modificarPesosSinapsisB();
        calcularErrorCapaOculta();
        modificarPesosSinapsisA();
    }


    /**
     * Calcula los errores en la última capa.
     */
    public void calcularErroresNeuronaSalida(){
        for(int i=0;i<vector_salida.length;i++){
            errorB[i]=vector_salida[i]*(1-vector_salida[i])*(target[i]-vector_salida[i]);
            Log.i("SALIDA","error "+i+" "+errorB[i]);
        }
    }

    /**
     * Modifica los pesos de la sinapsis entre la capa oculta y la capa de salida.
     * WAα = WAα + η δα outA   WAβ = WAβ + η δβ outA
     * WBα = WBα + η δα outB   WBβ = WBβ + η δβ outB
     * WCα = WCα + η δα outC   WCβ = WCβ + η δβ outC
     */
    public void modificarPesosSinapsisB(){
        for(int i=0;i<sinapsisB.length;i++){
            for(int j=0;j<sinapsisB[0].length;j++){
                sinapsisB[i][j]=sinapsisB[i][j]+ratioAprendizaje*errorB[j]*vector_parcial[i];
                Log.i("MODIFICAR"," "+i+" "+j+" "+sinapsisB[i][j]);
            }

        }
    }

    /**
     * Calcula los errores de la capa oculta utilizando los errores de la capa de salida
     *
     * δA = outA (1 – outA) (δα WAα + δβ WAβ)
     δB = outB (1 – outB) (δα WBα + δβWBβ)
     δC = outC (1 – outC) (δα WCα + δβWCβ)
     */
    public void calcularErrorCapaOculta(){
        double suma=0;
        for(int i=0;i<sinapsisB.length;i++){
            suma=0;
            for(int j=0;j<sinapsisB[0].length;j++) {
                suma=suma+errorB[j]*sinapsisB[i][j];
            }
            errorA[i]=vector_parcial[i]*(1-vector_parcial[i])*suma;
        }
    }

    /**
     * Modifica los pesos de la sinapsis entre los datos de entrada y la capa oculta.
     *
     * WλA = WλA + ηδA inλ     WΩA = WΩA + ηδA inΩ
     * WλB = WλB + ηδB inλ     WΩB = WΩB + ηδB inΩ
     * ...
     */
    public void modificarPesosSinapsisA(){
        for(int i=0;i<sinapsisA.length;i++){
            for(int j=0;j<sinapsisA[0].length;j++){
                sinapsisA[i][j]=sinapsisA[i][j]+ratioAprendizaje*errorA[i]*vector_entrada[j];
            }
        }
    }

    /**
     * Este método graba los datos de los pesos a un archivo externo
     * Se usa una vez realizado el entrenamiento para usarlos en la red definitiva
     * de la aplicación.
     */
    private void grabar(){
        try {
            Log.i("GRABAR","Inicio grabar");

            File path= Environment.getExternalStorageDirectory();
                File file=new File(path,"pesos.txt");

                FileOutputStream out=new FileOutputStream(file);
                OutputStreamWriter salida=new OutputStreamWriter(out);

                String textoArchivo="Pesos entrada capa oculta \r\n";

                for(int i=0;i<sinapsisA.length;i++) {
                    for(int j=0;j<sinapsisA[0].length;j++){
                        textoArchivo += " "+sinapsisA[i][j] ;
                    }
                    textoArchivo += "\r\n";
                }
                textoArchivo += "Pesos capa oculta capa salida \r\n";
                for(int i=0;i<sinapsisB.length;i++) {
                    for(int j=0;j<sinapsisB[0].length;j++){
                        textoArchivo += " "+sinapsisB[i][j] ;
                    }
                    textoArchivo += "\r\n";
                }
                salida.write(textoArchivo);
                salida.flush();
                salida.close();
            Log.i("GRABAR","Fin grabar "+file.getAbsolutePath()+" "+file.getName());

        }catch(Exception e) {
            Log.i("GRABAR","fallo al grabar: "+e.getMessage());
            e.printStackTrace();
        }
    }





    /************* MÉTODOS GET SET ****************************/

    public double[] getVector_entrada() {
        return vector_entrada;
    }

    public void setVector_entrada(double[] vector_entrada) {
        this.vector_entrada = vector_entrada;
    }

    public double[] getVector_salida() {
        return vector_salida;
    }

    public double[] getVector_parcial() {
        return vector_parcial;
    }

    public void setVector_parcial(double[] vector_parcial) {
        this.vector_parcial = vector_parcial;
    }

    public void setVector_salida(double[] vector_salida) {
        this.vector_salida = vector_salida;
    }

    public double[] getBiasA() {
        return biasA;
    }

    public void setBiasA(double[] biasA) {
        this.biasA = biasA;
    }

    public double[] getBiasB() {
        return biasB;
    }

    public void setBiasB(double[] biasB) {
        this.biasB = biasB;
    }

    public double[][] getSinapsisB() {
        return sinapsisB;
    }

    public void setSinapsisB(double[][] sinapsisB) {
        this.sinapsisB = sinapsisB;
    }

    public double[][] getSinapsisA() {
        return sinapsisA;
    }

    public void setSinapsisA(double[][] sinapsisA) {
        this.sinapsisA = sinapsisA;
    }

    public double[] getTarget() {
        return target;
    }

    public void setTarget(double[] target) {
        this.target = target;
    }

    public void imprimirSinapsis(){
        for(int i=0;i<sinapsisA.length;i++) {
            String linea="";
            for (int j = 0; j < sinapsisA[0].length; j++) {
                linea += " "+sinapsisA[i][j];
            }
            Log.i("SINAPSIS",linea);
        }
    }

    public void imprimirVectorParcial(){
        for(int i=0;i<vector_parcial.length;i++){
            Log.i("PARCIAL"," "+vector_parcial[i]);
        }
    }

}
