## `ads-platform`
- how many services
  - how to run each locally
    - send request and see response
  - any deprecated src code? can we remove
- data
  - what data does server generate? Any log, events?
  - where data is sent to, in what format?
  - dags
    - the overall architecture of these dags
    - how data is layered?
    - what are they used for
    - relationship with `nova-event-service`
    - how are they deployed?
      - where is airflow cluster located?
    - online offline
- how to serve models
- bidding logic
  - inputs and how to handle inputs
  - unit tests
- billing logic
  - how to calculate bill
  - how and when to charge
- pacing
- bias
  - new AB by Joseph
  - programatic creative
- ad manager
  - org
    - account
      - campaign

## Reporting
- charging
- real time reporting
  - click
  - impression
  - 

## `models`
- how many models are there
- where to train
- data to train
- how to serve models

## `nova-event-service`
- what events
  - impression
  - click
  - conversion, pixel postback
  - viewability
  - webview page load?
    - what events
- Handle all event?
- where data sent to, how to consume
- run locally
  - send req and see resp
  - 

## ETL
- kafka
  - self hosted? or MSK?
- s3 parquets
  - PII, data compliance
- jobs to consume data from kafka directly
- table architecture
- data ingest to scylla
- who consume data from scylla
- scylla
  - cluster monitoring, maintaining
  - 

## Nova-sdk
- data
  - what data does server generate? Any log, events?
  - where data is sent to, in what format?
  - realtime or offline?
  - 
  
