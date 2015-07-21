
## Automated testing architecture

The Web Apollo unit testing framework uses the grails testing guidelines extensively, which can be reviewed here: http://grails.github.io/grails-doc/2.4.3/guide/testing.html


Our basic methodology is to run the full test suite with the apollo command:

```
    apollo test
```


More specific tests can also be run for example by running specific commands for `grails test-app`

```
    grails test-app :unit-test
```

This runs ALL of the tests in “test/unit”. If you want to test a specific function then write it something like this:

```
    grails test-app org.bbop.apollo.FeatureService :unit 
```



### Notes about the test suites:

1. @Mock includes any domain objects you’ll use.  Unit tests don’t use the database.
2. setup() is run for each test (*we believe*) 
3. when: “” then: “”   You have to have both or it is not a test. 
4. Notice the valid groovy notation  .name == “Chr3”, it implies the Java .equals() function . . everywhere . . . . groovy rox

```
    @TestFor(FeatureService)
    @Mock([Sequence,FeatureLocation,Feature])
      class FeatureServiceSpec extends Specification {
      void setup(){}
      void "convert JSON to Feature Location"(){
    
      when: "We have a valid json object"
      JSONObject jsonObject = new JSONObject()
      Sequence sequence = new Sequence(name: "Chr3",seqChunkPrefix: "abc",seqChunkSize: 20,start:1,end:100,length:99,sequenceDirectory: "/tmp").save(failOnError: true)
      jsonObject.put(FeatureStringEnum.FMIN.value,73)
      jsonObject.put(FeatureStringEnum.FMAX.value,113)
      jsonObject.put(FeatureStringEnum.STRAND.value, Strand.POSITIVE.value)

    
      then: "We should return a valid FeatureLocation"
      FeatureLocation featureLocation = service.convertJSONToFeatureLocation(jsonObject,sequence)
      assert featureLocation.sequence.name == "Chr3"
      assert featureLocation.fmin == 73
      assert featureLocation.fmax == 113
      assert featureLocation.strand ==Strand.POSITIVE.value
    } }
```

There are 3 “special” types of things to test, which are all important and reflect the grails special functions: Domains, Controllers, Services.  They will all be in the “test” directory and all be suffixed with “Spec” for a Spock test.




