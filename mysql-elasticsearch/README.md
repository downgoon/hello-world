``` bash
$ ./mysqles-runner.sh
Usage: mysqles-runner.sh <statefile.json>

$ ./mysqles-runner.sh statefile-passport-user.json
[14:36:30,545][INFO ][importer.jdbc            ][pool-3-thread-1] strategy standard: settings = {elasticsearch.cluster=elasticsearch, elasticsearch.host=localhost, elasticsearch.port=9300, index=passport, password=passport, sql.0.statement=select a.id as _id, nick, headface, gender, mobile, email, emailchk, u.status as status, u.createtime as createtime from access a, user u where a.id = u.id, type=user, url=jdbc:mysql://10.209.44.14:10044/passport, user=passport}, context = org.xbib.elasticsearch.jdbc.strategy.standard.StandardContext@5966e44f
[14:36:30,554][INFO ][importer.jdbc.context.standard][pool-3-thread-1] found sink class org.xbib.elasticsearch.jdbc.strategy.standard.StandardSink@2a95e7d5
[14:36:30,558][INFO ][importer.jdbc.context.standard][pool-3-thread-1] found source class org.xbib.elasticsearch.jdbc.strategy.standard.StandardSource@62f93788
[14:36:30,608][INFO ][org.xbib.elasticsearch.helper.client.BaseTransportClient][pool-3-thread-1] creating transport client on Linux Java HotSpot(TM) 64-Bit Server VM Oracle Corporation 1.8.0_60-b27 25.60-b23 with effective settings {autodiscover=false, client.transport.ignore_cluster_name=false, client.transport.nodes_sampler_interval=5s, client.transport.ping_timeout=5s, cluster.name=elasticsearch, flush_interval=5s, host.0=localhost, max_actions_per_request=10000, max_concurrent_requests=8, max_volume_per_request=10mb, name=importer, port=9300, sniff=false}
[14:36:30,630][INFO ][org.elasticsearch.plugins][pool-3-thread-1] [importer] modules [], plugins [helper], sites []
[14:36:30,995][INFO ][org.xbib.elasticsearch.helper.client.BaseTransportClient][pool-3-thread-1] trying to connect to [localhost/127.0.0.1:9300]
[14:36:31,101][INFO ][org.xbib.elasticsearch.helper.client.BaseTransportClient][pool-3-thread-1] connected to [{Typeface}{K_5OFFkCTkuEpfE5MDFBeA}{10.213.44.189}{localhost/127.0.0.1:9300}]
```
