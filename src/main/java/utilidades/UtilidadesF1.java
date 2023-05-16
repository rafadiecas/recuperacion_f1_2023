package utilidades;

import modelos.*;

import java.util.*;
import java.util.stream.Collectors;

public class UtilidadesF1 {

    public UtilidadesF1() {
    }





    /**
     * Devuelve la lista de pilotos cuya escudería tiene la marca que se pasa como parámetro.
     *
     * @param pilotos
     * @param marca
     * @return
     */
    public static List<Piloto> getPilotosPorMarcaEscuderia(List<Piloto> pilotos, Marca marca){
        return pilotos.stream().filter(p->p.getEscuderia().getMarca().equals(marca)).collect(Collectors.toList());
    }


    /**
     * Devuelve los pilotos agrupados por escudería
     *
     * @param pilotos
     * @return
     */
    public static Map<Escuderia, List<Piloto>>  pilotosPorEscuderia(List<Piloto> pilotos){
        return pilotos.stream().collect(Collectors.groupingBy(Piloto::getEscuderia));
    }


    /**
     * Devuelvo los coches cuya suma de puntuacion -> (velocidadPunta + aceleracion - tiempoMedioParadaBoxes - probabilidadAveria )
     * es mayor a la que se pasa , ORDENADOR POR PUNTUACIÓN DE MAYOR A MENOR
     *
     * @param coches
     * @param minimoPuntuacionRequerida
     * @return
     */
    public static List<Coche> topMejoresCoches(List<Coche> coches, Double minimoPuntuacionRequerida){

        Comparator<Coche> comparator = Comparator.comparing(c-> c.getVelocidadPunta() + c.getAceleracion() -c.getProbabilidadAveria() -c.getTiempoMedioParadaBoxes());

        return coches
                .stream()
                .filter(c-> (c.getAceleracion()+ c.getVelocidadPunta() - c.getProbabilidadAveria() -c.getTiempoMedioParadaBoxes()) >minimoPuntuacionRequerida)
                .sorted(comparator.reversed())
                .collect(Collectors.toList());
    }


    /**
     * Devuelve el porcentaje de victoria de un piloto , que se calcula con la siguiente fórmula:
     *
     * -> puntuación total coche del su escuderia  (velocidadPunta + aceleracion - tiempoMedioParadaBoxes - probabilidadAveria)
     * -> +
     * -> puntosRanking de su escuderia
     * -> +
     * -> puntosRanking piloto
     *
     * @param piloto
     * @return
     */
    public static Double porcentajeVictoriaPiloto(Piloto piloto){
        return piloto.getEscuderia().getPuntosRanking() +
                piloto.getPuntosRanking() +
                piloto.getEscuderia().getCoche().getAceleracion() +
                piloto.getEscuderia().getCoche().getVelocidadPunta() -
                piloto.getEscuderia().getCoche().getTiempoMedioParadaBoxes() -
                piloto.getEscuderia().getCoche().getProbabilidadAveria();
    }


    /**
     * Devuelve el piloto con mayor porcentaje de victoria de los dos que se pasa,
     * el porcentaje de victoria se calcula del ejercicio anterior.
     *
     */
     public static Piloto getMejorPiloto(Piloto piloto1, Piloto piloto2){
        return porcentajeVictoriaPiloto(piloto1) > porcentajeVictoriaPiloto(piloto2) ? piloto1 : piloto2;
    }


    /**
     * Devuelve el mapa de las posiciones obtenidas por las escuderías del ranking de la temporada que se pasa como parámetro,
     * teniendo en cuenta que sólo hay un ranking por temporada.
     *
     * Las claves del mapa son el orden obtenido de mayor a menor , tras ordenar las temporadas del ranking por su "posicionEnRanking"
     *
     * @param rankingEscuderias
     * @param temporada
     * @return
     */
    public static Map<Integer,Escuderia> getRankigPorEscuderias(List<RankingEscuderias> rankingEscuderias, Integer temporada){
        Map<Integer, Escuderia>  mapa = new HashMap<>();
        rankingEscuderias
                .stream()
                .filter(r->r.getTemporada().equals(temporada))
                .findFirst()
                .ifPresent(p->p.getEscuderias().forEach(e-> mapa.put(e.getPosicionEnRanking(),e)));
        return mapa;
    }


    /**
     * Devuelve un mapa de los pilotos con la suma de puntos que tengan de las carreras que se pasa como parámetro.
     * Los puntos se obtienen sacando la posición en la que queda el piloto del mapa de "posiciones" y de los puntos
     * que correspondan a esa posición en el mapa "puntosPorPosicion"
     *
     * @param carreras
     * @return
     */
    public static Map<Piloto, Double> totalPuntuacion(List<Carrera> carreras){

        Map<Piloto, Double> mapaFinal = new HashMap<>();

        for(Carrera c : carreras){

            for(Integer i : c.getPosiciones().keySet()){

                if(mapaFinal.containsKey(c.getPosiciones().get(i))){
                    mapaFinal.replace(c.getPosiciones().get(i), mapaFinal.get(c.getPosiciones().get(i))+ c.getPuntosPorPosicion().get(i));
                }else{
                    mapaFinal.put(c.getPosiciones().get(i),c.getPuntosPorPosicion().get(i));
                }

            }


        }

        return mapaFinal;
    }

}
