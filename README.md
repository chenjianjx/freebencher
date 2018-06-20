# freebencher

## A Quick Example

pom.xml

````
 


	<dependencies>
		<dependency>
			<groupId>com.github.chenjianjx</groupId>
			<artifactId>freebencher</artifactId>
			<version>2.0.0</version>
		</dependency>
		...
	</dependencies>	

````

Basically, you'll just need to write a callback method.
````
	@Test
	public void testLogin() {
		
		final List<User> userList = new ArrayList<User>();
		...//preparing userList to be used as test data.

		FbJobResult result = Freebencher.benchmark(new FbTarget() { //the behavior
			@Override
			public boolean invoke() {
				User user = userList.get(RandomUtils
						.nextInt(userList.size()));
				int statusCode = remoteServiceToTest.doLogin(user.getUsername(), user.getClearPassword());
				return statusCode == 200;
			}
		}, 5, // concurrency,
				50 // number of tests to run
				);

		System.out.println(result.report());
	}

````

You will get ApacheBench-like output:
````
Test started.
Awaiting termination...
Test completed.
Concurrency:             5
Time taken for tests:    119ms
Successful tests:        50
Failed tests:            0
Tests per second:        420.16806722689074
Mean time per test:      11.38ms
Percentage of the test finished within a certain time (ms)
50%:                     11
60%:                     12
70%:                     12
80%:                     13
90%:                     14
95%:                     14
98%:                     14
99%:                     16
100%:                    16

````

## Why freebencher

1. __Simpler__ compared to GUI tools such as JMeter. 
2. More __Parameterizable__ compared to Apache ab, which can take only one fixed URL.
3. Most important, it is __Embeddable__. You __integrate the testing framework into your own java project__, so you can freely use your own business classes. On the contrary, other tools such as JMeter requires you to package your code into their library. The packaging and integration is very troublesome and cannot be automated. __This is why freebencher was written.__   
4. __Freedom to developers__. 
 * You can test any behavior that can be written in Java, not just Http Servers. 
 * You can prepare your test data in any way in Java, as long as the callback method can reference it.  Aren't you sick of generating data files and "browse" it for GUI tools? 
 * You can make assertions of the correctness of the testing results anyway you want using Java code.
