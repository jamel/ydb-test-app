### Example of usage YDB Java SDK

##### Install prerequisites

```
$ sudo apt-get update
$ sudo apt-get install git openjdk-11-jdk
```

##### Build

```
$ git clone https://github.com/jamel/ydb-test-app.git
$ cd ydb-test-app
$ ./mvnw package
```

##### Run

```
$ ./mvnw -q exec:java -Dexec.args="<accountId> <keyId> <private_key_file_path> <endpoint> <database>"
```

Where: 
  * accountId             - identificator of service account
  * keyId                 - identificator of generated key
  * private_key_file_path - path to a file where private part of the key is located
  * endpoint              - YDB endpoint
  * database              - database name


After run the program will output something like this: 
```
SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
SLF4J: Defaulting to no-operation (NOP) logger implementation
SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.
WARNING: An illegal reflective access operation has occurred
WARNING: Illegal reflective access by com.google.protobuf.UnsafeUtil (file:/home/jamel/.m2/repository/com/google/protobuf/protobuf-java/3.6.1/protobuf-java-3.6.1.jar) to field java.nio.Buffer.address
WARNING: Please consider reporting this to the maintainers of com.google.protobuf.UnsafeUtil
WARNING: Use --illegal-access=warn to enable warnings of further illegal reflective access operations
WARNING: All illegal access operations will be denied in a future release

--[ DescribeTables ]--
/global/b1gbkpesobgv2i2266vg/etn002qef8mecrnph3ae/series:
    series_id: Uint64? (PK)
    title: Utf8?
    series_info: Utf8?
    release_date: Uint64?

/global/b1gbkpesobgv2i2266vg/etn002qef8mecrnph3ae/seasons:
    series_id: Uint64? (PK)
    season_id: Uint64? (PK)
    title: Utf8?
    first_aired: Uint64?
    last_aired: Uint64?

/global/b1gbkpesobgv2i2266vg/etn002qef8mecrnph3ae/episodes:
    series_id: Uint64? (PK)
    season_id: Uint64? (PK)
    episode_id: Uint64? (PK)
    title: Utf8?
    air_date: Uint64?

PRAGMA TablePathPrefix("/global/b1gbkpesobgv2i2266vg/etn002qef8mecrnph3ae");

DECLARE $seriesData AS List<Struct<
    series_id: Uint64,
    title: Utf8,
    series_info: Utf8,
    release_date: Date>>;

DECLARE $seasonsData AS List<Struct<
    series_id: Uint64,
    season_id: Uint64,
    title: Utf8,
    first_aired: Date,
    last_aired: Date>>;

DECLARE $episodesData AS List<Struct<
    series_id: Uint64,
    season_id: Uint64,
    episode_id: Uint64,
    title: Utf8,
    air_date: Date>>;

REPLACE INTO series
SELECT
    series_id,
    title,
    series_info,
    CAST(release_date AS Uint64) AS release_date
FROM AS_TABLE($seriesData);

REPLACE INTO seasons
SELECT
    series_id,
    season_id,
    title,
    CAST(first_aired AS Uint64) AS first_aired,
    CAST(last_aired AS Uint64) AS last_aired
FROM AS_TABLE($seasonsData);

REPLACE INTO episodes
SELECT
    series_id,
    season_id,
    episode_id,
    title,
    CAST(air_date AS Uint64) AS air_date
FROM AS_TABLE($episodesData);

--[ SelectSimple ]--
+-----------+------------------+--------------------+
| series_id |            title |       release_date |
+-----------+------------------+--------------------+
|   Some[1] | Some["IT Crowd"] | Some["2006-02-03"] |
+-----------+------------------+--------------------+

--[ SelectWithParams ]--
+------------------+------------------------+
|     season_title |           series_title |
+------------------+------------------------+
| Some["Season 3"] | Some["Silicon Valley"] |
+------------------+------------------------+
Finished preparing query: PreparedSelectTransaction

--[ PreparedSelect ]--
+-------------+------------+-----------+-----------+--------------------------------+
|    air_date | episode_id | season_id | series_id |                          title |
+-------------+------------+-----------+-----------+--------------------------------+
| Some[16957] |    Some[7] |   Some[3] |   Some[2] | Some["To Build a Better Beta"] |
+-------------+------------+-----------+-----------+--------------------------------+

--[ PreparedSelect ]--
+-------------+------------+-----------+-----------+--------------------------------------+
|    air_date | episode_id | season_id | series_id |                                title |
+-------------+------------+-----------+-----------+--------------------------------------+
| Some[16964] |    Some[8] |   Some[3] |   Some[2] | Some["Bachman's Earnings Over-Ride"] |
+-------------+------------+-----------+-----------+--------------------------------------+

--[ MultiStep ]--
+-----------+------------+---------------------------------+-------------+
| season_id | episode_id |                           title |    air_date |
+-----------+------------+---------------------------------+-------------+
|   Some[5] |    Some[1] |   Some["Grow Fast or Die Slow"] | Some[17615] |
|   Some[5] |    Some[2] |           Some["Reorientation"] | Some[17622] |
|   Some[5] |    Some[3] | Some["Chief Operating Officer"] | Some[17629] |
+-----------+------------+---------------------------------+-------------+

--[ PreparedSelect ]--
+-------------+------------+-----------+-----------+-------------+
|    air_date | episode_id | season_id | series_id |       title |
+-------------+------------+-----------+-----------+-------------+
| Some[18129] |    Some[1] |   Some[6] |   Some[2] | Some["TBD"] |
+-------------+------------+-----------+-----------+-------------+
```
