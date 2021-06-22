# HelloPrompt

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/2f022b61c14a433891b47ee329bc7900)](https://app.codacy.com/gh/asas1asas200/HelloPrompt?utm_source=github.com&utm_medium=referral&utm_content=asas1asas200/HelloPrompt&utm_campaign=Badge_Grade_Settings)

Prompt some informations on your screen when opened terminal.

![demo](https://i.imgur.com/ChaCjsi.png)

## Prerequirements

-   git 2.31.1
-   gradle 7.0.2
-   opanjdk 15.0.2

## Build

```sh
git clone https://github.com/asas1asas200/HelloPrompt.git
cd HelloPrompt
./gradlew build
./gradlew run # or run in quiet mode $ ./gradlew run -q 
```

## Get full function of this app

### Google Calendar Support

1.  Register an API credentials [Google Calendar API](https://console.cloud.google.com/marketplace/product/google/calendar-json.googleapis.com)
2.  Download the credentials and rename it as `credentials.json`.
3.  Put it into `src/main/resources/`

### Weather Support

1.  Register an API token by steps in [開發指南](https://opendata.cwb.gov.tw/devManual/insrtuction)

2.  Create a file at `src/main/resources`.

3.  Rename as `authority_key.csv`.

4.  Input text like this
    ```csv
    授權碼,{YOUR_TOKEN_HERE}
    ```

## Run it when open the terminal

```sh
./gradlew shadowJar
cp build/libs/HelloPrompt-1.0-all.jar ~/Desktop
echo 'java -jar ~/Desktop/HelloPrompt-1.0-all.jar' >> ~/.bashrc
```

## Thanks for

-   [schachmat/wego](https://github.com/schachmat/wego)
-   [manytools.org](https://manytools.org/hacker-tools/ascii-banner/)
