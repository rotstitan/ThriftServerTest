namespace java com.vngcorp.log.thrift

struct LogEntry {
	1: required string gameId,
	2: required string serviceId, 
	3: required string message,
}
