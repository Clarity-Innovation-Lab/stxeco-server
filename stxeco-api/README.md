# DAO Micro Service

Indexes and caches contract data to make the api client application faster
and more flexible.

## Build

Build from the root directory

```bash
./build.sh
```

or directly..

```bash
mvn -Dmaven.test.skip=true clean install
docker-compose build
docker-compose up -d
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