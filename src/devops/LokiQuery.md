# Loki Query Language

Examples

#### Format log message with patterns and extract json, then get the rate
- `| json seat = "Seat" [5m]` this line create a new filed called `seat` and assign the value of `Seat` of the json to it. Then we can `sum by (seat)`
```
sum by (seat)(
    rate(
        {app="msp", cluster="prod-monetization"}
        |= "\"AdUnit\":\"msp-android-article-related-native-rank.v0-ctrl"
        | pattern `<_> <_>       1 <_> <message>`
        | line_format `{{.message}}`
        |json
        | __error__ = ""
        | Seat != ""
        | IsCachedAd = 1
        | json seat = "Seat" [5m]
    )
)
```

#### Format log message with patterns and extract json, then get the count
```
sum by (seat)(
    count_over_time(
        {app="msp", cluster="prod-monetization"}
        |= "\"AdUnit\":\"msp-android-article-related-native-rank.v0-ctrl2"
        | pattern `<_> <_>       1 <_> <message>`
        | line_format `{{.message}}`
        |json
        | __error__ = ""
        | Seat != ""
        | IsCachedAd = 1
        | json seat = "Seat"
        [24h]
    )
)
```

#### Parse log as json, then check if a field does not contain a substring
```
{app="mes-consumer"} | json | user_agent != "" and user_agent != ".*Android.*" and user_agent != ".*Darwin.*"
```

#### Calculate rate and group by
```
sum by(summary_seat) (
  rate(
    {app="ssr"} |= "summary" | json | summary_inferResult_pSexual_isOn = "false" | summary_inferResult_pInappropriate_isOn = "false" | summary_inferResult_pBlank_isOn = "false" [10m]
  )
)
/
sum by (summary_seat) (
  rate(
    {app="ssr"} |= "summary" | json [10m]
  )
)
```
