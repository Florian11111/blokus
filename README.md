<p align="center" style="margin:0; padding:0;">
  <h1 align="center" style="margin:0; padding:0;"><img src="https://static.wikia.nocookie.net/logopedia/images/a/a7/Blokus.png" align="center" width="250px"></img></h1>
</p>

## Inhaltsverzeichnis

1. [Intro](#intro)
2. [Spielprinzip](#spielprinzip-1)
3. [Installation](#installation)
4. [Testing](#testing)
5. [Quellen](#quellen)

## Intro

Blokus. Ein Rundenbasierter, zeitloser Klassiker im zweidimensionalen Raum, einfach zu verstehen und noch einfacher zu spielen.

## Spielprinzip [1]

Im Zweipersonenspiel verfügt jeder Spieler über zwei Sätze von 21 Spielsteinen, die sich aus kleinen Quadraten zusammensetzen. Der eine Spieler hat blaue und rote Steine, der andere gelbe und grüne. Dabei kommt jede Form, die aus 1–5 Quadraten besteht, in jeder Farbe genau so oft vor, wie Spieler zu 4 fehlen. Das Brett besteht aus einem 20 × 20 Spielfeld.<br>

Die Spielsteine werden abwechselnd reihum gelegt, wobei in den Ecken begonnen wird. Die Reihenfolge ist im Uhrzeigersinn: Blau – Gelb – Rot – Grün. Der erste Stein jeder Farbe muss so gesetzt werden, dass das Eckfeld besetzt wird. Steine einer Farbe müssen sich immer über Eck berühren, niemals jedoch entlang einer Seite. An fremde Steine kann man dagegen beliebig anlegen.<br>

Es wird so lange gespielt, bis keine Steine mehr gesetzt werden können.<br>

Ziel ist es, möglichst viele Spielsteine auf dem Brett abzulegen. Da das Spielfeld gerade mal ausreichend Platz für alle Steine bietet, werden die Räume schnell eng. Wer am Schluss die wenigsten Punkte übrig hat, hat gewonnen. Dabei zählt jedes kleine Quadrat eines eigenen Spielsteins einen Punkt.
<div align="center">
  <img src="https://upload.wikimedia.org/wikipedia/commons/1/16/BlockusFinalBoardCloseUp.jpg" height=200px>
  <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/2/2a/Blokus_starting_in_Tampere.jpg/2560px-Blokus_starting_in_Tampere.jpg" height=200px>
</div>

## Installation

install <a href="https://www.scala-sbt.org/download.html" target="_blank">sbt</a><br>
```git clone https://github.com/Florian11111/blokus.git```<br>
dann Ordner öffnen und ```sbt run```

(Für Mac mit M1/M2/M3 chips bitte Branch "mac-arm" auswählen)

## Testing

[![Scala CI](https://github.com/Florian11111/blokus/actions/workflows/scala.yml/badge.svg)](https://github.com/Florian11111/blokus/actions/workflows/scala.yml)
[![Coverage Status](https://coveralls.io/repos/github/Florian11111/blokus/badge.svg?branch=main)](https://coveralls.io/github/Florian11111/blokus?branch=main)

Manuell:<br>
einfach coverage.sh ausführen

## Quellen
[1] https://de.wikipedia.org/wiki/Blokus
