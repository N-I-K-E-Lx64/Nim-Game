# Nim-Game

## How to play
Das Spiel wird mithilfe von REST-Requests gespielt. Zur Steuerung stehen 2 verschiedene Endpoints zur Verfügung, über die das Spiel entweder gestart oder die zu ziehende Anzahl an Streichhölzern übermittelt werden kann.

Endpoints:
- `/start` : Über diesen Endpoint kann das Spiel gestartet werden. Gleichzeitig kann er auch dazu genutzt werden, den Spielzustand zurückzusetzen, um eine neue Runde zu spielen.

  Parameter : Boolean `strategy` - Legt fest, ob der Comuter optimal (*true*) oder random (*false*) spielen soll
  ```JSON
  {
    "strategy": true
  }
  ```
- `/draw` : Über diesen Endpoint übermittelt der Menschliche Spieler die Anzahl an Streichhölzern, die er in seinem aktuellen Zug ziehen möchte.

  Parameter : Integer `matches` - Anzahl an Streichhölzern
  ```JSON
  {
    "matches": 2
  }
  ```
  
Das Spiel muss zunächst über eine passende Anfrage an den `/start` Endpoint gestartet werden. Erst danach können über den `/draw` Endpoint Streichhölzer gezogen werden. 
  
Der Server ist über localhost unter dem Port 8080 erreichbar!
 

