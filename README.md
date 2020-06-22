![Java](https://img.shields.io/badge/java-%3E%3D1.8-green)

<img align="left" width="100" height="100" src="https://raw.githubusercontent.com/FedericoGarciaGarcia/EasyKeyToPrint/development/source/images/icon.png" alt="Resume application project app icon">

[English](https://github.com/FedericoGarciaGarcia/Printi/tree/development)
|
[日本語](https://github.com/FedericoGarciaGarcia/Printi/blob/development/README-JP.md)
|
[Español](https://github.com/FedericoGarciaGarcia/Printi/blob/development/README-ES.md)

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
* ***Portable***: no installation is needed.

## Installation

Make sure you have [Java](https://java.com/en/download/) installed in your system.

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