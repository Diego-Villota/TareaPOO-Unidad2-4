# Catálogo de contenidos audiovisuales

Proyecto de POO basado en el ejemplo de la Unidad 2: amplía un catálogo para demostrar herencia y relaciones entre objetos. No utiliza dependencias externas.

## Clases y relaciones

- `Pelicula`, `SerieDeTV`, `Documental`, `PodcastAudiovisual` y `MakingOf` heredan de `ContenidoAudiovisual` y muestran sus detalles de forma polimórfica.
- Una película agrega actores, que pueden existir independientemente y compartirse.
- Una serie compone temporadas: las crea internamente y devuelve una vista de solo lectura.
- Un documental se asocia con investigadores; un podcast puede compartir uno de ellos. Un making of se asocia con una película.
- `PruebaAudioVisual` crea y muestra instancias de todas las clases. El diagrama completo está en `diagrama.md`.

## Importación y ejecución en Eclipse

1. Instale un JDK (Java 8 o superior) y Eclipse IDE for Java Developers.
2. Clone el repositorio: `git clone URL_DE_SU_REPOSITORIO`.
3. En Eclipse, seleccione **File > Import > General > Existing Projects into Workspace**, indique la carpeta clonada y finalice la importación.
4. Abra `src/main/java/poo/PruebaAudioVisual.java` y seleccione **Run As > Java Application**. Revise los resultados en la consola.

Desde una terminal en Windows PowerShell, también puede compilar y ejecutar así:

```powershell
New-Item -ItemType Directory -Force bin | Out-Null
javac -encoding UTF-8 -d bin (Get-ChildItem src/main/java -Recurse -Filter *.java | ForEach-Object FullName)
java -cp bin poo.PruebaAudioVisual
```

El ejemplo imprime cinco tipos de contenido, el reparto, dos temporadas y el investigador. El proyecto original se obtuvo de https://github.com/CS-Programacion-Orientada-Objetos/poo_unidad1.
