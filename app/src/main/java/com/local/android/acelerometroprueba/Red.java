package com.local.android.acelerometroprueba;

/**
 * Clase para construir la red neuronal completa.
 * Se trata de un perceptrón multicapa.
 *
 * Created by SAMUAN on 06/04/2015.
 */
public class Red {

}

/*/**
 * Class Red
 *
public class Red {

    //
    // Fields
    //
    private int entradas = 0;
    private int salidas  = 0;
    private int nCapas;
    private Perceptron[][] capas;
    private Double[][] sinapsis;

    //
    // Constructors
    //
    public Red (int[] inputs, int outputs, String[] funciones)
    {
        nCapas   = inputs.length;
        entradas = inputs[0];
        salidas  = outputs;
        int max  = 0;

        for(int i=0; i<nCapas; i++)
            max = inputs[i]>max ?inputs[i] :max;

        capas    = new Perceptron[nCapas][max];
        sinapsis = new Double[nCapas+1][max];
        limpiarSinapsis();

        for(int i=0; i<nCapas; i++)
            for(int j=0; j<max; j++)
                capas[i][j] = j<inputs[i] ?new Perceptron(inputs[i], funciones[i]) :null;
    }

    public Red(String xml)
    {

    }

    //
    // Methods
    //
    private void limpiarSinapsis()
    {
        for(int i=0; i<sinapsis.length; i++)
            for(int j=0; j<sinapsis[i].length; j++)
                sinapsis[i][j] = null;
    }

    public Double simular(Double[] inputs)
    {
        for(int n=0; n<inputs.length && n<sinapsis[0].length; n++)
            sinapsis[0][n] = inputs[n];

        for(int i=0; i<nCapas; i++)
        {
            for(int j=0; j<capas[i].length && capas[i][j] != null; j++)
            {
                for(int n=0; n<sinapsis[i].length && sinapsis[i][n] != null; n++)
                    capas[i][j].entradas[n] = sinapsis[i][n];

                sinapsis[i+1][j] = capas[i][j].calcular();
            }
        }

        return null;
    }

    //
    // Accessor methods
    //

    public int getEntradas() {
        return entradas;
    }

    public void setEntradas(int entradas) {
        this.entradas = entradas;
    }

    public int getNcapas() {
        return nCapas;
    }

    public void setNcapas(int nCapas) {
        this.nCapas = nCapas;
    }

    public int getSalidas() {
        return salidas;
    }

    public void setSalidas(int salidas) {
        this.salidas = salidas;
    }

    //
    // Other methods
    //

}
*/