include "testdata.thrift"

namespace java com.thrifttest.thrift

service ThriftTestService {
	testdata.TestData sendTest(1:testdata.TestData data);
	void ping();
}
