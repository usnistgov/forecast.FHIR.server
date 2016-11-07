# Forecast FHIR Server
Depends on `fhir.forecast, fhir.emf, tchforecastconn-code`

It is an http server that supports rest calls and is based on the well known libraries jetty, jersey and jackson.

## Startup
`cd forecast.FHIR.server`

`$> gradle`

### result
`> Building 91% > :run`
Server listens on port 8080
The file forecast.yml contains much of the server configuation.  It includes the aforementioned port.  It also includes the logger output levels, and a mapping of the various forecasters to their respecive parameters.

## Shutdown
`ctl-c`

## Operations
Server currently logs everything to the console.
