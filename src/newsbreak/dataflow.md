## Nova

### Request


## Nova-Event-Service

### Click, Impression
- These two events are sent to the same kafka topic
- Denoted by `EventType`
  ```
    AD_EVENT_UNKNOWN,
    AD_EVENT_IMPRESSION,
    AD_EVENT_CLICK,
    AD_EVENT_SKIP_AD,
    AD_EVENT_CONVERSION,
    AD_EVENT_VIEWABLE_IMPRESSION,
    hide_ad,
    unhide_ad
  ```
- Kafka Topic: `nova_ads_event`
- Table
  - `ods.nova_ads_event` -> `dwd.nova_ads_event` cast timestamp from bigint to timestamp
  - 

### Conversion
- Kafka Topic: `nova_ads_conversion`

### Full Event
- Kafka Topic: `nova_ads_full_event`

### Video Event
- Kafka Topic: `nova_ads_event_video`

### NewsLetter
- Kafka Topic: `nova_ads_newsletter_conversion_log`
