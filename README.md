# DAO Micro Service

Indexes and caches contract data to make the api client application faster
and more flexible.

## Build

Build source (see below to run without java/maven) from the root directory

```bash
./build.sh
```

or directly..

```bash
cd stxeco-api
mvn clean install # or mvn -Dmaven.test.skip=true clean install
cd ..
docker-compose build
docker-compose up -d
```

### Build from Images

Build locally from docker images should be possible as long as the docker images in docker hub are public.

The following may work

```bash
docker-compose -f docker-compose-images.yml pull
docker-compose -f docker-compose-images.yml up -d
```

and to kill the containers..

```bash
docker-compose -f docker-compose-images.yml down
```

## Deployment

Deploy on testnet using

```bash
./deploy-stxeco.sh
```

Deploy on mainnet using

```bash
./deploy-stxeco.sh prod
```

Note: Requires your ssh key to be installed on the server.

## Notes on Java API

The API is written in Java and has no documentation as yet. Quickest way to locate
API end points is to search for the annotation;

```java
@RestController
```

These classes contain all the api mappings. E.g.

```java
@GetMapping(value = "/v2/gh-issues")
// inside
@RestController
public class IssueController { ... }
```

Is a GET end point for fetching github issues. POST,PUT,DELETE mappings are marked by similar annotations - @PostMapping etc.

Path parameters and post body parameters are marked;

```java
@PathVariable String contractId
@RequestBody Object jsonBean
```
