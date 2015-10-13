# akka-sharding-example

A simple implementation of akka sharding. 

## Benchmarking
You can test both applications on your local machine by using included `resources/URLs.txt` file and the following command:

### SingleNodedApp

Just run it from your IDE and then:

```
cat src/main/resources/URLs.txt | parallel -j 5 'ab -ql -n 2000 -c 1 -k {}' | grep 'Requests per second'
```

### ShardedApp
First build the application:

```mvn clean package```

This will build `target/SortingDecider-1.0-SNAPSHOT-uber.jar`. You will be able to run two nodes by overriding default values:
- first node: `java -jar target/SortingDecider-1.0-SNAPSHOT-uber.jar`
- second node: `java -Dclustering.port=2552 -Dapplication.exposed-port=8081 -jar target/SortingDecider-1.0-SNAPSHOT-uber.jar`

For benchmarking sharded application (which is the default in the project) you need to use haproxy:
```haproxy -f src/main/resources/haproxy.conf````

and then use different `shardedURLs.txt` file:

```
cat src/main/resources/shardedURLs.txt | parallel -j 5 'ab -ql -n 2000 -c 1 -k {}' | grep 'Requests per second'
```

It uses:
- `ab` - Apache HTTP server benchmarking tool
- `parrallel` - GNU Parallel - The Command-Line Power Tool
