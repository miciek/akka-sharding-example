# akka-sharding-example

A simple implementation of akka sharding. Presentation can be found here:

http://www.slideshare.net/miciek/sane-sharding-with-akka-cluster

## Benchmarking
You can test both applications on your local machine by using included `resources/URLs.txt` file and the following command:

```
cat src/main/resources/URLs.txt | parallel -j 5 'ab -ql -n 2000 -c 1 -k {}' | grep 'Requests per second'
```

Please compare `Requests per second`.

It uses:
- `ab` - Apache HTTP server benchmarking tool
- `parrallel` - GNU Parallel - The Command-Line Power Tool