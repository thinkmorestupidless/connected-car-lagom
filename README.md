# connected-car-lagom

This is designed to be the 'micro-services' part of the Connected Car demo.

Consumes `RawTelemety` events from Kafka - on the `connectedcar` topic, stores them in a PersistentEntity (id of the entity is the carId) and then publishes processed telemetry data back out to a different topic for processing.

## Quickstart

Simply run the Lagom services with

```bash
sbt runAll
```

Then you can push data via the `kafka-console-producer` with

```bash
./kafka-console-producer.sh --broker-list 127.0.0.1:9092 --topic connectedcar
```

to start the producer and example telemetry

```bash
{"powerconsumption":"70","speed":"30","motortemp":"91","carid":"12695","driver":"Heidi Decker","location":"37.851040,-122.250970","id":"1519814371940","status":"HEALTHY","event_timestamp":"2018-02-28T10:39:31.940Z","batterylevel":"98"}
```