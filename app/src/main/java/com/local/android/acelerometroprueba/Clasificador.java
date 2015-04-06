package com.local.android.acelerometroprueba;

/**
 * Created by FESEJU on 01/04/2015.
 */
public class Clasificador {

    private Muestra[] valores;
    private long peaktime;

    private long peaktimemas;
    private int marcadorPeak=0;
    private int marcadorPeakMas;

    private int marcadorIE=0;
    private long IE=0;

    private int marcadorIS=0;
    private long IS=0;

    /**
     * Calcula los 8 valores necesarios para el clasificador
     *
     * @param peaktime tiempo de pico seguido de 2500 ms sin picos.
     * @param valores array con todos los valores capturados de aceleración
     */
    Clasificador(long peaktime,Muestra[] valores){
        this.peaktime=peaktime;
        this.valores=valores;

        peaktimemas=peaktime+1000000000; //nanosegundos
        marcadorPeakMas= valores.length-1;

        calcularIE();
        calcularIS();
    }

    /**
     * Calcula el tiempo de fin de impacto. Última aceleracion mayor a 1.5 g dentro del intervalo [peaktime, peaktime + 1000]
     * @return
     */
    private void calcularIE(){
       for(int i=0;i<valores.length;i++){
            long eltiempo=valores[i].getTiempo();
            if(eltiempo>=peaktime){
                marcadorPeak=i;
            }
            if(eltiempo>=peaktimemas){
                marcadorPeakMas=i;
                break;
            }
        }

        for(int j=marcadorPeakMas; j==marcadorPeak; j--){
            if(valores[j].getAceleracion()>1.5) {
                IE=valores[j].getTiempo();
                marcadorIE=j;
                break;
            }
        }
    }

    /**
     * Calcula el tiempo de Inicio del impacto. Tiempo de primera aceleracion > 1.5 precedida de aceleración < 0.8 en
     * intervalo [ IE-1200, Peaktime ]
     * @return
     */
    private void calcularIS() {
        long tiempoIEmenos = IE - 1200000000;
        int marcadorTiempoIEMenos = 0;
        for (int i = 0; i < valores.length; i++) {
            long eltiempo = valores[i].getTiempo();
            if (eltiempo >= tiempoIEmenos) {
                marcadorTiempoIEMenos = i;
                break;
            }
        }
        //buscar un valor <0.8 y despues un valor > 1.5

        //si no aparece < 0.8 o despues >1.5 entonces peaktime.
        boolean elmenor=false;
        boolean elmayor=false;
        for(int j=marcadorTiempoIEMenos;j==marcadorPeak;j++){

            if(!elmenor){
                if(valores[j].getAceleracion()<0.8){
                    elmenor=true;
                }
            }
            if(elmenor && !elmayor){
                if(valores[j].getAceleracion()>1.5){
                    elmayor=true;
                    marcadorIS=j;
                    IS=valores[j].getTiempo();
                }
            }
        }
        if(!elmenor || !elmayor) {
            marcadorIS=marcadorPeak;
            IS=peaktime;
        }
    }


    //calcular AAMV
    private void calcularAAMV(){

    }
    //calcular IDI

    //calcular MPI

    //...

}






