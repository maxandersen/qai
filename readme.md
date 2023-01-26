# OpenAI Quarkus Exception Explainer

Get openai token from https://beta.openai.com/account/api-keys

Then do this:

```
export OPENAI_TOKEN=yoursecretkey
jbang qai.java csrf.txt
```

Try out with other txt files containing a java stacktrace.


Examples:

[cxf.txt](cxf.txt):
```
This stacktrace suggests that the application is encountering an unmarshalling error when it tries to process an XML document. This is likely due to either incorrect XML or an incorrect configuration of the application. 

The solution is to first confirm the XML is valid and then to validate the application's configuration. This could include checking the application's XML bindings and making sure they match the expected format. Additionally, it may be necessary to update the application's XML processing library to the latest version to ensure compatibility issues aren't the cause.
```

[csrf.txt](csrf.txt):
```
This stacktrace suggests that the CSRF (Cross-site request forgery) filter is unable to set the csrf_token property. This is likely due to misconfiguration of the CSRF filter itself or the application code not setting the csrf_token property before issuing the request. 

The solution would be to check the configuration of the CSRF filter, ensure that the application code is properly setting the csrf_token property, and verify that the API calls are properly authorized.
```