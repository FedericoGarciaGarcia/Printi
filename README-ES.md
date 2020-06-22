<img align="left" width="80" height="80" src="https://raw.githubusercontent.com/FedericoGarciaGarcia/EasyKeyToPrint/development/source/images/icon.png" alt="Resume application project app icon">

[English](https://github.com/FedericoGarciaGarcia/Printi/tree/development)
|
[日本語](https://github.com/FedericoGarciaGarcia/Printi/blob/development/README-JP.md)
|
[Español](https://github.com/FedericoGarciaGarcia/Printi/blob/development/README-ES.md)

# Printi [v1.0]

Aplicación Java para imprimir capturas de pantalla.

## Características

* ***Impresoras***: elegir la impresora a usar. También se pueden usar impresoras PDF.
* ***Color***: escala de grises o a color.
* ***Orientación***: vertical u horizontal.
* ***Aspecto***: mantener proporciones o estirar.
* ***Barra de tareas***: mostrar u ocultar la barra de tareas del sistema operativo en la captura.
* ***Notificaciones***: mostrar u ocultar notificaciones.
* ***Print Dialog***: mostrar u ocultar diálogo de impresión.
* ***Idioma***: inglés, español o japonés. Se pueden añadir más idiomas manualmente.
* ***Guardar configuración***: la configuración se guarda tras cada cambio.
* ***Portable***: no es necesario instalar.

## Instalación

Es necesario tener [Java](https://java.com/en/download/) instalado.

### Windows

Download the *Printi* folder in *releases/windows* and put in desired location. You can start the application by running *printi.exe*.

### Linux and MAC

Download the *Printi* folder in *releases/java* and put in desired location. You can start the application by running *printi.jar*.

## How to use

* Run the application. It will appear in the tray.
* Right click the tray icon and select your printer and other settings.
* Press the PRINT SCREEN key.
* The screen will be printed.

In case you want to take a screenshot without printing, hold SHIFT and press the PRINT SCREEN key.

## Languages

More languages can be added manually. Added languages can be changed in the application.

* Go to the *languages* folder.
* Copy the *english.txt* file and paste it with a different name.
* Open the new pasted file.
* Translate every text below the texts in brackets. Do not change the text in brackets.

## Source code

You can also download the source code and compile it yourself.

Download the *source* folder and cd to it:

```
cd source
```

Compile with:

```
javac -cp ".;libs/jnativehook-2.1.0.jar" *.java
```

Run with:

```
java -cp ".;libs/jnativehook-2.1.0.jar" EasyKeyToPrint
```

## Author

Federico Garcia Garcia

## Acknowledgments

*PageFormatFactory* class made by:
* *Object Refinery Ltd, Pentaho Corporation and Contributors*