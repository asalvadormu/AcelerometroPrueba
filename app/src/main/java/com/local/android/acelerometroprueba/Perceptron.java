package com.local.android.acelerometroprueba;

/**
 * Clase que representa una neurona.
 * Realiza la función de red y la función de activación.
 *
 * Created by SAMUAN on 06/04/2015.
 */
public class Perceptron {

}
/*  private Double logsig(double val)
    {
        return 1/(1 + Math.exp(-val));
    }*/
/*
/**
 * Class Perceptron
 *
public class Perceptron
{

    //
    // Fields
    //
    public  Double[]   entradas;
    private Double[]   pesos;
    public  Double     bias;
    private Double     wBias;
    public  Double     salida;
    public  double     sigma;
    public  double     rata;
    private Activacion fnTransf;


//    private enum FuncionesTransferencia {HARDLIM, HARDLIMS, POSLIN, PURELIN, SATLIN, SATLINS, LOGSIG, TANSIG, UNDEFINED};
//    private FuncionesTransferencia funcion = FuncionesTransferencia.UNDEFINED;

    //
    // Constructors
    //
    public Perceptron (int inputs, String funcion)
    {
        entradas = new Double[inputs];
        pesos    = new Double[inputs];
        wBias    = 0d;
        salida   = 0d;
        sigma    = 0d;
        rata     = 1d;   // valor inicial con tolerancia 100%    rango [0..1]
        fnTransf = new Activacion(funcion);

        for(int i=0; i<inputs; i++)
            entradas[i] = pesos[i] = 0.0;

    }

    //
    // Methods
    //
    public Double calcular()
    {
        Double suma = 0.0;

        for(int i=0; i<this.entradas.length; i++)
            suma += this.entradas[i] * this.pesos[i];

        this.salida = suma + this.bias;

        return this.fnTransf.exec(this.salida);
    }

    public Double backPropagation(double Sigma)  // valor Sigma debe provenir del controlador del ciclo principal
    {
        // ver la forma de ir registrando los valoes de los pesos y salidas de cada iteracion
        for(int i = 0; i<this.pesos.length && this.pesos[i] != null; i++)
        {
            double peso = pesos[i];
            double delta = rata * Sigma * salida;
            pesos[i] = peso * delta;
        }

        return null;  // evaluar si debe retornar valor
    }
    //
    // Accessor methods
    //
    public Double getBias() {
        return bias;
    }

    public void setBias(Double bias) {
        this.bias = bias;
    }

    public Double getSalida() {
        return salida;
    }

    public void setSalida(Double salida) {
        this.salida = salida;
    }

    public Double getwBias() {
        return wBias;
    }

    public void setwBias(Double wBias) {
        this.wBias = wBias;
    }

    public void setPeso(int idx, Double valor)
    {
        this.pesos[idx] = valor;
    }

    public Double getPeso(int idx)
    {
        return this.pesos[idx];
    }

    public double getRata() {
        return rata;
    }

    public void setRata(double rata) {
        this.rata = rata;
    }

    public double getSigma() {
        return sigma;
    }

    public void setSigma(double sigma) {
        this.sigma = sigma;
    }

    /*
     * Algoritmo de retropropagación
     *
     * El procedimiento de retropropagación es una forma relativamente eficiente
     * de calcular qué tanto se mejora el desempeño con los cambios individuales
     * en los pesos. Se conoce como procedimiento de retropropagación porque,
     * primero calcula cambios en la capa final, reutiliza gran parte de los
     * mismos cálculos para calcular los cambios de los pesos de la penúltima
     * capa y, finalmente, regresa a la capa inicial.
     *
     * Al comparar la señal de salida con una respuesta deseada o salida objetivo,
     * d(t), se produce una señal de error, e(t), energía de error. Señal de error
     * en la neurona de salida j en la iteración t
     *         e(t)=d(t) - y(t)
     * donde t denota el tiempo discreto.
     *
    public double setSigma(double input, double output)   // usado en regla de aprendizaje
    {
        double error = output - input;
        this.sigma = this.fnTransf.train(salida) * error;
        return this.sigma;
    }

    //
    // Other methods
    //

    public String getConfiguracion()
    {
        return null;
    }

    public void setConfiguracion()
    {

    }
}
Hide details
Change log
r69 by miguelwelch on Jan 13, 2011   Diff
        implementando algoritmo de entrenamiento
        perceptron multicapa via backpropagation
        Go to:
        Project members, sign in to write a code review
        Older revisions
        r68 by miguelwelch on Jan 12, 2011   Diff
        r67 by miguelwelch on Jan 12, 2011   Diff
        r66 by miguelwelch on Dec 30, 2010   Diff
        All revisions of this file
        File info
        Size: 6414 bytes, 193 lines
        View raw file
        */