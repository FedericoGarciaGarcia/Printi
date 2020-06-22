<img align="left" width="100" height="100" src="https://raw.githubusercontent.com/FedericoGarciaGarcia/EasyKeyToPrint/development/source/images/icon.png" alt="Resume application project app icon">

[English](https://github.com/FedericoGarciaGarcia/Printi/tree/development)
|
[日本語](https://github.com/FedericoGarciaGarcia/Printi/blob/development/README-JP.md)
|
[Espanol](https://github.com/FedericoGarciaGarcia/Printi/blob/development/README-ES.md)

# Printi [v1.0]

Lightweight application to print screenshots made in Java.

## Features

* ***Printers***: select the printer to use. PDF printers are also valid.
* ***Color***: grayscale or full color.
* ***Paper orientation***: landscape or portrait.
* ***Fit***: maintain aspect ratio or stretch to fit.
* ***Taskbar***: show or hide the operating system's taskbar in the screenshot.
* ***Notifications***: show or hide printing notifications.
* ***Print Dialog***: show or hide printing dialog.
* ***Language***: English, Spanish or Japanese. More languages can be added by the user.
* ***Save settings***: settings are saved every time.
* ***No installation***: place it wherever you want and run it.

More features on the way:

* ***Do no print screenshot***: Hold the SHIFT key and press the PRINT SCREEN key to take a screenshot without printing.
* ***Title***: place a title on the document.
* ***Date***: place the current date on the document.

## Installation

Make sure you have [Java](https://java.com/en/download/) installed in your system.

### Windows

Download the *EasyKeyToPrint* folder in *releases/windows* and put in desired location. You can start the application by running *EasyKeyToPrinte.exe*.

### Linux and MAC

Download the *EasyKeyToPrint* folder in *releases/java* and put in desired location. You can start the application by running *EasyKeyToPrinte.jar*.

## How to use

* Run the application. It will appear in the tray.
* Right click the tray icon and select your printer and other settings.
* Press the PRINT SCREEN key.
* The screen will be printed.

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