
<h4 align="center">
  Java implementation of the Cypher Protocol</a>
</h4>

# Building the source

Building cypher requires `git` and 64-bit version of `Oracle JDK 1.8` to be installed, other JDK versions are not supported yet. Make sure you operate on `Linux` and `MacOS` operating systems.

Clone the repo and switch to the `master` branch

```bash
$ git clone https://github.com/tronprotocol/java-tron.git
$ cd cypher
```

then run the following command to build cypher, the `FullNode.jar` file can be found in `cypher/build/libs/` after build successful.

```bash
$ ./gradlew clean build -x test
```

# Running cypher

Running cypher requires 64-bit version of `Oracle JDK 1.8` to be installed, other JDK versions are not supported yet. Make sure you operate on `Linux` and `MacOS` operating systems.

## Hardware Requirements

Minimum:

- CPU with 8 cores
- 16GB RAM
- 2TB free storage space to sync the Mainnet

Recommended:

- CPU with 16+ cores(32+ cores for a super representative)
- 32GB+ RAM(64GB+ for a super representative)
- High Performance SSD with at least 2.5TB free space
- 100+ MB/s download Internet service

## Running a full node for mainnet

Full node has full historical data, it is the entry point into the CYPHER network , it can be used by other processes as a gateway into the CYPHER network via HTTP and GRPC endpoints. You can interact with the CYPHER network through full node：transfer assets, deploy contracts, interact with contracts and so on. `-c` parameter specifies a configuration file to run a full node:

```bash
$ nohup java -Xms9G -Xmx9G -XX:ReservedCodeCacheSize=256m \
             -XX:MetaspaceSize=256m -XX:MaxMetaspaceSize=512m \
             -XX:MaxDirectMemorySize=1G -XX:+PrintGCDetails \
             -XX:+PrintGCDateStamps  -Xloggc:gc.log \
             -XX:+UseConcMarkSweepGC -XX:NewRatio=2 \
             -XX:+CMSScavengeBeforeRemark -XX:+ParallelRefProcEnabled \
             -XX:+HeapDumpOnOutOfMemoryError \
             -XX:+UseCMSInitiatingOccupancyOnly  -XX:CMSInitiatingOccupancyFraction=70 \
             -jar FullNode.jar -c main_net_config.conf >> start.log 2>&1 &
```

## Running a super representative node for mainnet

Adding the `--witness` parameter to the startup command, full node will run as a super representative node. The super representative node supports all the functions of the full node and also supports block production. Before running, make sure you have a super representative account and get votes from others. Once the number of obtained votes ranks in the top 27, your super representative node will participate in block production.

Fill in the private key of super representative address into the `localwitness` list in the `main_net_config.conf`. Here is an example:

```
 localwitness = [
    <your_private_key>
 ]
```

then run the following command to start the node:

```bash
$ nohup java -Xms9G -Xmx9G -XX:ReservedCodeCacheSize=256m \
             -XX:MetaspaceSize=256m -XX:MaxMetaspaceSize=512m \
             -XX:MaxDirectMemorySize=1G -XX:+PrintGCDetails \
             -XX:+PrintGCDateStamps  -Xloggc:gc.log \
             -XX:+UseConcMarkSweepGC -XX:NewRatio=2 \
             -XX:+CMSScavengeBeforeRemark -XX:+ParallelRefProcEnabled \
             -XX:+HeapDumpOnOutOfMemoryError \
             -XX:+UseCMSInitiatingOccupancyOnly  -XX:CMSInitiatingOccupancyFraction=70 \
             -jar FullNode.jar --witness -c main_net_config.conf >> start.log 2>&1 &
```

## Quick Start Tool

An easier way to build and run cypher is to use `start.sh`. `start.sh` is a quick start script written in the Shell language. You can use it to build and run cypher quickly and easily.

Here are some common use cases of the scripting tool

- Use `start.sh` to start a full node with the downloaded `FullNode.jar`
- Use `start.sh` to download the latest `FullNode.jar` and start a full node.
- Use `start.sh` to download the latest source code and compile a `FullNode.jar` and then start a full node.

For more details, please refer to the tool [guide](./shell.md).

## Run inside Docker container

One of the quickest ways to get `java-tron` up and running on your machine is by using Docker:

```shell
$ docker run -d --name="java-tron" \
             -v /your_path/output-directory:/java-tron/output-directory \
             -v /your_path/logs:/cypher/logs \
             -p 8090:8090 -p 18888:18888 -p 50051:50051 \
             tronprotocol/java-tron \
             -c /java-tron/config/main_net_config.conf
```

This will mount the `output-directory` and `logs` directories on the host, the docker.sh tool can also be used to simplify the use of docker, see more [here](docker/docker.md).
