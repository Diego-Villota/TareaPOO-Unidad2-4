# Sistema de Contenido Audiovisual (Código Limpio, SOLID, MVC, CSV y JUnit 5)

Proyecto de **Programación Orientada a Objetos** (Universidad Politécnica Salesiana) que evoluciona el catálogo audiovisual de la Unidad 2 incorporando persistencia en archivos **CSV**, refactorización bajo principios de **Código Limpio** y **SOLID**, arquitectura **MVC** en consola y pruebas unitarias con **JUnit 5**.

Repositorio: https://github.com/Diego-Villota/TareaPOO-Unidad2-5

## Cambios realizados por etapa

### Etapa 1. Manejo de Archivos (CSV)
- Lectura y escritura eficiente en UTF-8 con `BufferedReader` / `BufferedWriter` y *try-with-resources* en `RepositorioContenidoCsv`.
- Soporte completo para `Pelicula` (y sus `Actor`), `SerieDeTV` (y sus `Temporada`), `Documental` (y sus `Investigador`), `PodcastAudiovisual` y `MakingOf`.
- Preservación de identidad de objetos compartidos (`Actor`, `Investigador`, referencia `MakingOf -> Pelicula`) durante la carga mediante `ContextoCargaCsv`.
- Escape y parseo conforme a RFC 4180 en `UtilidadCsv` y manejo de errores mediante `PersistenciaException` (indicando ruta y número de línea en caso de formato inválido).
- Archivo de ejemplo incluido en `data/contenidos.csv`.

### Etapa 2. Refactorización y Código Limpio
- Reorganización de paquetes por responsabilidad (`audiovisual.modelo`, `audiovisual.persistencia`, `audiovisual.vista`, `audiovisual.controlador`) y nombres descriptivos en clases, métodos y variables.
- Eliminación de código duplicado:
  - `ContenidoAudiovisual.formatearDetalle()` aplica *Template Method* para eliminar el formateo repetido y retirar por completo `System.out.println` de las clases del modelo.
  - `Validador` centraliza validaciones de texto, rangos numéricos y referencias no nulas.
- División de métodos largos en métodos pequeños y cohesivos, y eliminación de comentarios redundantes.

### Etapa 3. Aplicación de Principios SOLID
- **SRP (Responsabilidad Única):** Separación clara entre entidades de dominio, gestión del catálogo (`CatalogoAudiovisual`), persistencia CSV, interfaz de consola (`VistaConsola`) y coordinación (`ControladorAudiovisual`).
- **OCP (Abierto/Cerrado):** `AdaptadorCsvContenido` y el registro de adaptadores en `RepositorioContenidoCsv` (con `prioridadEscritura()`) permiten agregar nuevos tipos de contenido sin modificar el repositorio.
- **LSP (Sustitución de Liskov):** Todas las subclases de `ContenidoAudiovisual` cumplen su contrato y son intercambiables de forma polimórfica.
- **ISP (Segregación de Interfaces):** Interfaces pequeñas y específicas (`LectorContenidos`, `EscritorContenidos`, `AdaptadorCsvContenido`, `VistaAudiovisual`).
- **DIP (Inversión de Dependencias):** `ControladorAudiovisual` depende únicamente de abstracciones inyectadas por constructor desde `Aplicacion`.

### Etapa 4. Patrón MVC
- **Modelo (`audiovisual.modelo`):** `ContenidoAudiovisual`, `Pelicula`, `SerieDeTV`, `Documental`, `PodcastAudiovisual`, `MakingOf`, `Actor`, `Temporada`, `Investigador`, `CatalogoAudiovisual`, `Validador`.
- **Vista (`audiovisual.vista`):** `VistaAudiovisual` y `VistaConsola` (menú interactivo en consola).
- **Controlador (`audiovisual.controlador`):** `ControladorAudiovisual`.
- **Persistencia (`audiovisual.persistencia`):** `LectorContenidos`, `EscritorContenidos`, `RepositorioContenidoCsv`, adaptadores CSV, `ContextoCargaCsv`, `UtilidadCsv` y `PersistenciaException`.
- **Punto de entrada:** `audiovisual.Aplicacion`.

### Etapa 5. Pruebas Unitarias (JUnit 5)
- Suite completa en la carpeta `test/` (9 clases de prueba, 22 casos unitarios) cubriendo casos normales, límites y excepciones en modelos, catálogo, utilidades CSV, repositorio CSV, controlador MVC (con dobles de prueba) y vista de consola.

## Estructura del proyecto

```text
TareaPOO-Unidad2-5/
├── data/
│   └── contenidos.csv
├── src/main/java/audiovisual/
│   ├── Aplicacion.java
│   ├── controlador/
│   │   └── ControladorAudiovisual.java
│   ├── modelo/
│   │   ├── Actor.java
│   │   ├── CatalogoAudiovisual.java
│   │   ├── ContenidoAudiovisual.java
│   │   ├── Documental.java
│   │   ├── Investigador.java
│   │   ├── MakingOf.java
│   │   ├── Pelicula.java
│   │   ├── PodcastAudiovisual.java
│   │   ├── SerieDeTV.java
│   │   ├── Temporada.java
│   │   └── Validador.java
│   ├── persistencia/
│   │   ├── AdaptadorCsvContenido.java
│   │   ├── AdaptadorDocumentalCsv.java
│   │   ├── AdaptadorMakingOfCsv.java
│   │   ├── AdaptadorPeliculaCsv.java
│   │   ├── AdaptadorPodcastCsv.java
│   │   ├── AdaptadorSerieCsv.java
│   │   ├── ContextoCargaCsv.java
│   │   ├── EscritorContenidos.java
│   │   ├── LectorContenidos.java
│   │   ├── PersistenciaException.java
│   │   ├── RepositorioContenidoCsv.java
│   │   └── UtilidadCsv.java
│   └── vista/
│       ├── VistaAudiovisual.java
│       └── VistaConsola.java
├── test/audiovisual/
│   ├── controlador/ControladorAudiovisualTest.java
│   ├── modelo/
│   │   ├── CatalogoAudiovisualTest.java
│   │   ├── DocumentalTest.java
│   │   ├── EntidadesYPolimorfismoTest.java
│   │   ├── PeliculaTest.java
│   │   └── SerieDeTVTest.java
│   ├── persistencia/
│   │   ├── RepositorioContenidoCsvTest.java
│   │   └── UtilidadCsvTest.java
│   └── vista/VistaConsolaTest.java
├── diagrama.md
├── .classpath
├── .project
└── README.md
```

## Formato del archivo CSV (`data/contenidos.csv`)

```csv
tipo,id,titulo,duracion,genero,campo_extra,relaciones
PELICULA,1,Avatar,162,Ciencia ficcion,20th Century Studios,Sam Worthington;Zoe Saldana
SERIE,3,"Cosmos: una odisea",45,Divulgacion,2,1:13;2:13
DOCUMENTAL,5,El universo,50,Ciencia,Astronomia,Ana Torres|Astronomia;Carlos Mendez|Astrofisica
PODCAST,7,Conversaciones del cosmos,30,Divulgacion,Ciencia Hoy,Ana Torres|Astronomia
MAKING_OF,8,Detras de Avatar,25,Documental,Luis Perez,1
```

## Cómo clonar y ejecutar el proyecto

### 1. Clonar el repositorio

```bash
git clone https://github.com/Diego-Villota/TareaPOO-Unidad2-5.git
cd TareaPOO-Unidad2-5
```

### 2. Importar y ejecutar en Eclipse IDE

1. Abra **Eclipse IDE for Java Developers** (JDK 11 o superior; probado en JDK 25).
2. Seleccione **File > Import > General > Existing Projects into Workspace**.
3. Elija la carpeta `TareaPOO-Unidad2-5` y haga clic en **Finish**.
   - El archivo `.classpath` ya incluye `src/main/java`, `test` y la librería **JUnit 5** (`JUNIT_CONTAINER/5`).
4. Para ejecutar la aplicación: clic derecho en `src/main/java/audiovisual/Aplicacion.java` > **Run As > Java Application**.
5. Para ejecutar todas las pruebas unitarias: clic derecho sobre la carpeta `test` > **Run As > JUnit Test**.

### 3. Compilar y ejecutar desde terminal (Windows PowerShell)

```powershell
New-Item -ItemType Directory -Force bin | Out-Null
javac -encoding UTF-8 -d bin (Get-ChildItem src/main/java -Recurse -Filter *.java | ForEach-Object FullName)
java -cp bin audiovisual.Aplicacion
```

### 4. Diagrama de clases

El diagrama UML actualizado se encuentra en [diagrama.md](diagrama.md).
