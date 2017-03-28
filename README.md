# Keyword Arguments in Java
1st PAVA Project
## Compilation
Using ant, you only need to run:
```ant```
## Execution
```java -jar keyConstructors.jar <Test>```
## Note regarding Extension "Support repeated keywords in method calls with appropriate semantics."
Since the beggining of the project we thought about this extension, without really considering it as an extension.<br>
So this feature was integrated in the original code. You can check it [here](https://github.com/francisco-polaco/pava-keyword-arguments-java-2017-ist/blob/master/src/ist/meic/pa/TemplateMaker.java) (./src/ist/meic/pa/TemplateMaker.java).<br>
A comment using "#extension" is used to be more easily identified.
## Note regarding tests
To test our project we included the classes in our jar, since we could not point the classpath properly.