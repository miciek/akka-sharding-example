# akka-sharding-example

A simple implementation of akka sharding. Presentation can be found here:

http://www.slideshare.net/miciek/sane-sharding-with-akka-cluster

## Benchmarking
You can test both applications on your local machine by using included `resources/URLs.txt` file and the following command:

```
cat URLs.txt | parallel 'ab -c 2 -n 100 {}'
```

Please compare `Requests per second`.

It uses:
- `ab` - Apache HTTP server benchmarking tool
- `parrallel` - GNU Parallel - The Command-Line Power Tool