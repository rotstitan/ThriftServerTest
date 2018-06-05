include "testdata.thrift"

namespace java com.thrifttest.thrift

service ThriftTestService {
	testdata.TestData sendTest(1:testdata.TestData data);
	void ping();
}
service CaculateService {
	i32 add(1:i32 a1,2:i32 a2);
}
