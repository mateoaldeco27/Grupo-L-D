package org.example;

import java.util.ArrayList;

public class LectorManager {

    public LectorManager(String[] resultadoDatosFila, String[] pronosticoDatosFila) {

        this.resultadoDatosFila = resultadoDatosFila;
        this.pronosticoDatosFila = pronosticoDatosFila;

    }

    private String[] resultadoDatosFila;
    private String[] pronosticoDatosFila;

    public void programa() {

        ArrayList<Partido> partidos = new ArrayList<>();

        ArrayList<Participante> participantes = new ArrayList<>();

        ArrayList<Ronda> rondas = new ArrayList<>();

        ArrayList<Fase> fases = new ArrayList<>();

        // agrego id
        int id = 0;
        int indicePartido;
        int resultadoRondaFinal = 0;
        int resultadoFaseFinal = 0;

        // ronda predeterminada
        Ronda nuevaRonda = new Ronda();
        rondas.add(nuevaRonda);
        // fase predetermindada
        Fase nuevaFase = new Fase();
        fases.add(nuevaFase);

        // Extracción de datos del .csv y armado de objetos Equipo y Partido
        for (int i = 1; i < pronosticoDatosFila.length; i++) {

            String[] pronosticoColumna_Fila = pronosticoDatosFila[i].split(";");


            // creo participantes con if
            if (id != Integer.parseInt(pronosticoColumna_Fila[0])) {
                Participante nuevoParticipante = new Participante(Integer.parseInt(pronosticoColumna_Fila[0]), pronosticoColumna_Fila[1]);
                participantes.add(nuevoParticipante);
                id++;
            }

            //este indice va de 1 a 6 (la cantidad de partidos por participantes)
            indicePartido = i;
            while (indicePartido > resultadoDatosFila.length - 1) {
                indicePartido = indicePartido - (resultadoDatosFila.length-1);
            }

            // Extracción de datos de la fila de resultados.csv
            String[] resultadoColumna_Fila = resultadoDatosFila[indicePartido].split(";");


            // Armado de objetos equipo1, equipo2 y nuevoPartido, con base a las posiciones de las columnas de resultados.csv
            Equipo equipo1 = new Equipo(Integer.parseInt(resultadoColumna_Fila[3]), resultadoColumna_Fila[4], resultadoColumna_Fila[5]);
            Equipo equipo2 = new Equipo(Integer.parseInt(resultadoColumna_Fila[9]), resultadoColumna_Fila[8], resultadoColumna_Fila[10]);
            Partido nuevoPartido = new Partido(equipo1, Integer.parseInt(resultadoColumna_Fila[6]), Integer.parseInt(resultadoColumna_Fila[7]), equipo2);

            //agregamos los partidos a su respectivo arraylist
            if (partidos.size() < resultadoDatosFila.length - 1 ) {
                partidos.add(nuevoPartido);
            }

            // Extracción de datos de la fila, armado del objeto pronostico, y seteo de expectativa; con base al archivo pronostico.csv
            Pronostico nuevoPronostico = new Pronostico(nuevoPartido, equipo1, equipo2);

            nuevoPronostico.setParticipante(participantes.get(id -1));

            //llamamos a los metodos que se encargan de procesar los datos y mostrarlos por consola
            mostramePorConsola(nuevoPartido, indicePartido, participantes, id, nuevoPronostico, pronosticoColumna_Fila);
            ronda(indicePartido, id, resultadoRondaFinal, nuevoPartido, rondas, resultadoColumna_Fila, participantes);
            fase(indicePartido, id, resultadoFaseFinal, nuevaRonda, fases, resultadoColumna_Fila, participantes);
        }

        listaGanadores(participantes);
    }

    public static void mostramePorConsola(Partido nuevoPartido, int indicePartido, ArrayList<Participante> participantes,
                                          int idParticipantes, Pronostico nuevoPronostico, String[] infoPronostico) {

        System.out.println("\n▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");

        // Muestra número de partidos e información
        System.out.println(nuevoPartido.mostrameDatosPartido(indicePartido));

        // (nombre) espera que...
        nuevoPronostico.ComprobarDatos(infoPronostico, participantes, idParticipantes);

        // Equipo que ganó el partido
        nuevoPartido.GanadorPartido(indicePartido);

        // Salida personalizada por cada participante
        nuevoPronostico.AcertoElPronostico(participantes, idParticipantes);

        System.out.println("De momento, " + participantes.get(idParticipantes - 1).getNombre() + " tiene "
                + participantes.get(idParticipantes -1).getPuntos() + " punto/s.");

        System.out.println("▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");

    }

    public static void listaGanadores(ArrayList<Participante> participantes) {

        // Se crea un clon de la colección de Participantes para no perderlos cuando se eliminen.
        ArrayList<Participante> participantesAux = (ArrayList<Participante>) participantes.clone();

        System.out.println("\n-------------------------\n\t   ¡GANADORES!\n");

        int maxPuntaje = participantesAux.get(0).getPuntosFinalesFase();
        int minPuntaje = participantesAux.get(0).getPuntosFinalesFase();

        int indexMaxPuntaje = 0;
        int indexMinPuntaje = 0;

        System.out.println("↓↓↓↓↓ MAYOR PUNTAJE ↓↓↓↓↓");
        System.out.println("-------------------------");

        // El primer ciclo recorre a todos los participantes.
        for (int i = 0; i < participantesAux.size(); i++) {
            // El segundo ciclo recorre a los participantes que quedan en la colección, ya que una vez se busca el mayor puntaje se elimina al particiopante.
            for (int j = 0; j < participantesAux.size(); j++) {

                if (participantesAux.get(j).getPuntosFinalesFase() >= maxPuntaje) {
                    maxPuntaje = participantesAux.get(j).getPuntosFinalesFase();
                    indexMaxPuntaje = j;

                } else {
                    minPuntaje = participantesAux.get(j).getPuntosFinalesFase();
                    indexMinPuntaje = j;
                }
            }
            System.out.println(participantesAux.get(indexMaxPuntaje).getNombre() + "\t|\t" + maxPuntaje + "\tPUNTO/S" + "\n-------------------------");

            // Se elimina al participante con mayor puntaje.
            participantesAux.remove(indexMaxPuntaje);

            // Se setea el valor del puntaje máximo/mínimo a la primer persona que aparece en la lista modificada.
            maxPuntaje = participantesAux.get(0).getPuntosFinalesFase();
            minPuntaje = participantesAux.get(0).getPuntosFinalesFase();

            // El iterador del primer ciclo vuelve a cero para que no se salte a la primera persona que queda en la colección luego de la eliminación
            i = 0;
        }
        System.out.println(participantesAux.get(0).getNombre() + "\t|\t" + minPuntaje + "\tPUNTO/S\n-------------------------");
        System.out.println("↑↑↑↑↑ MENOR PUNTAJE ↑↑↑↑↑");
    }

    public static void ronda(int i,int idParticipantes, int resultadoRondaFinal, Partido nuevoPartido,ArrayList<Ronda> rondas, String[] resultadoColumna_Fila, ArrayList<Participante> participantes) {

        // divido el iterador por la cantidad de partidos por ronda(3)
        int resto = i % rondas.get(0).getPartidosxronda();
        if (resto == 0) {
            //agrega el partido a la ronda
            rondas.get(rondas.size()-1).agregarPartido(nuevoPartido);
            //cada 3 partidos crea una nueva ronda y la añade a la arraylist rondas
            System.out.println("♦♦♦♦ Acá hay una nueva ronda ♦♦♦♦");
            Ronda nuevaRonda = new Ronda();
            rondas.add(nuevaRonda);
            nuevaRonda.setResultadoRondaParcial(participantes.get(idParticipantes -1).getPuntos());

            System.out.println("EL RESULTADO DE ESTA RONDA ES: "+ nuevaRonda.getResultadoRondaParcial());

            nuevaRonda.sumaResultadoRondaFinalParticipante(participantes, idParticipantes,resultadoRondaFinal);

            participantes.get(idParticipantes-1 ).setPuntos(0);

            System.out.println("EL RESULTADO FINAL DE LAS RONDAS ES: "+participantes.get(idParticipantes -1).getPuntosFinalesRonda());

        }
        else {
            if (Integer.parseInt(resultadoColumna_Fila[2]) <= rondas.get(0).getPartidosxronda())
            {
                rondas.get(rondas.size()-1).agregarPartido(nuevoPartido);
            }
        }

    }

    public static void fase(int i, int idParticipantes, int resultadoFaseFinal, Ronda nuevaRonda, ArrayList<Fase> fases, String[] resultadoColumna_Fila, ArrayList<Participante> participantes) {

        // divido el iterador por la cantidad de rondas por fase(2)
        int resto = i % fases.get(0).getPartidoxfase();
        if (resto == 0) {
            //agrega la ronda a la fase
            fases.get(fases.size()-1).agregarRonda(nuevaRonda);
            //cada 2 rondas crea una nueva fase y la añade a la arraylist fases
            System.out.println("♦♦♦♦ Acá hay una nueva fase ♦♦♦♦");
            Fase nuevaFase = new Fase();
            fases.add(nuevaFase);
            nuevaFase.setResultadoFaseParcial(participantes.get(idParticipantes -1).getPuntosFinalesRonda());

            System.out.println("EL RESULTADO DE ESTA FASE ES: " + nuevaFase.getResultadoFaseParcial());

            nuevaFase.sumaResultadoFaseFinalParticipante(participantes, idParticipantes, participantes.get(idParticipantes -1).getPuntosFinalesFase());

            participantes.get(idParticipantes -1).resetPuntosFinalesRonda();

            System.out.println("EL RESULTADO FINAL DE LAS FASES ES: "+participantes.get(idParticipantes -1).getPuntosFinalesFase());

        }
        else {
            if (Integer.parseInt(resultadoColumna_Fila[1]) <= fases.get(0).getPartidoxfase())
            {
                fases.get(fases.size()-1).agregarRonda(nuevaRonda);

            }
        }
    }
}