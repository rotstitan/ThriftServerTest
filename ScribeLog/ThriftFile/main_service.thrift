include "logentry.thrift"

namespace java com.vngcorp.log.thrift

service MainService {
	i32 sendLog(1: required logentry.LogEntry entry);
	bool ping();
}
