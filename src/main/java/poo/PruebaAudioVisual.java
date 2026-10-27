package poo;
import uni1a.*;

public class PruebaAudioVisual {
    public static void main(String[] args) {
        Actor actor = new Actor("Sam Worthington");
        Investigador investigadora = new Investigador("Ana Torres", "Astronomia");
        Pelicula pelicula = new Pelicula("Avatar", 162, "Ciencia ficcion", "20th Century Studios");
        pelicula.agregarActor(actor);
        SerieDeTV serie = new SerieDeTV("Cosmos: una odisea", 45, "Divulgacion", 0);
        serie.agregarTemporada(1, 13);
        serie.agregarTemporada(2, 13);
        Documental documental = new Documental("El universo", 50, "Ciencia", "Astronomia");
        documental.agregarInvestigador(investigadora);
        PodcastAudiovisual podcast = new PodcastAudiovisual("Conversaciones del cosmos", 30, "Divulgacion", "Ciencia Hoy", investigadora);
        MakingOf makingOf = new MakingOf("Detras de Avatar", 25, "Documental", "Luis Perez", pelicula);
        ContenidoAudiovisual[] contenidos = { pelicula, serie, documental, podcast, makingOf };
        for (ContenidoAudiovisual contenido : contenidos) contenido.mostrarDetalles();
    }
}
