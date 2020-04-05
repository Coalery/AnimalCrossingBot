# AnimalCrossingBot

모여봐요 동물의 숲 트위치 봇

[For. 종미니멈](https://www.twitch.tv/derbls)


## Files Informations

* freshwater_files.txt : 민물고기 데이터

* sea_fish.txt : 바닷물고기 데이터

* fishdata.db : 물고기 데이터베이스

* command.db : 커스텀 커맨드 데이터베이스


## Java Files Informations

* Main.java : 메인 클래스

* TwitchBot.java : 트위치 봇 클래스

* Command.java : 커맨드 클래스


## command.db informations

* Table name : command

* Fields

|Field Name|Description|Type|
|:---:|:---|:---:|
|maker|명령어를 만든 사람|String|
|request|반응할 명령어|String|
|response|명령어에 대한 대답|String|


## fishdata.db informations

* Table name : fishdata

* Fields

|Field Name|Description|Type|
|:---:|:---|:---:|
|name|물고기의 이름|String|
|datestr|물고기가 나타나는 기간|String|
|time|물고기가 나타나는 시간대|String|
|location|물고기가 나타나는 위치|String|
|size|물고기의 크기|String|
|price|물고기의 가격(벨)|Integer|
|date01|물고기가 1월에 나오는지 여부|Integer(0 or 1)|
|date02|물고기가 2월에 나오는지 여부|Integer(0 or 1)|
|date03|물고기가 3월에 나오는지 여부|Integer(0 or 1)|
|date04|물고기가 4월에 나오는지 여부|Integer(0 or 1)|
|date05|물고기가 5월에 나오는지 여부|Integer(0 or 1)|
|date06|물고기가 6월에 나오는지 여부|Integer(0 or 1)|
|date07|물고기가 7월에 나오는지 여부|Integer(0 or 1)|
|date08|물고기가 8월에 나오는지 여부|Integer(0 or 1)|
|date09|물고기가 9월에 나오는지 여부|Integer(0 or 1)|
|date10|물고기가 10월에 나오는지 여부|Integer(0 or 1)|
|date11|물고기가 11월에 나오는지 여부|Integer(0 or 1)|
|date12|물고기가 12월에 나오는지 여부|Integer(0 or 1)|

`민물고기 : 강, 연못, 호수, 절벽위, 하구`

`바닷물고기 : 바다, 부두`


# Referenced Libraries

* [PircBot-1.5.0](http://www.jibble.org/pircbot.php)

* [sqlite-jdbc-3.27.2.1](https://github.com/xerial/sqlite-jdbc)